/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.context;

import com.avalon.core.antlr4.condition.ConditionManager;
import com.avalon.core.condition.Condition;
import com.avalon.core.config.ApplicationConfig;
import com.avalon.core.config.PulsarConfig;
import com.avalon.core.db.DataSourceUtil;
import com.avalon.core.db.DynamicDataSource;
import com.avalon.core.db.DynamicJdbcTemplate;
import com.avalon.core.enums.SystemStateEnum;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.field.Many2manyField;
import com.avalon.core.model.FieldHashMap;
import com.avalon.core.model.Man2ManyServiceRecordRow;
import com.avalon.core.model.Record;
import com.avalon.core.module.AbstractModule;
import com.avalon.core.module.ModuleList;
import com.avalon.core.orm.ClassPoolManager;
import com.avalon.core.orm.ORMMapper;
import com.avalon.core.permission.ElevatePermissionEnum;
import com.avalon.core.redis.IRedisLock;
import com.avalon.core.service.AbstractService;
import com.avalon.core.service.ExternalService;
import com.avalon.core.util.*;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.NotFoundException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.GenericWebApplicationContext;

import java.util.*;

@Component()
@Data
public class Context {
    private static final Logger log = LoggerFactory.getLogger(Context.class);
    @Autowired
    private ApplicationConfig applicationConfig;
    @Autowired
    private PulsarConfig pulsarConfig;
    @Autowired
    public ConditionManager conditionManager;

    private final Man2ManyServiceRecordRow man2ManyServiceRecordRow = new Man2ManyServiceRecordRow();

    public void putMany2manyService( Many2manyField field) {
        man2ManyServiceRecordRow.putMany2manyService( field);
    }

    public AbstractService getMany2manyService(String many2manyServiceName) {
        return man2ManyServiceRecordRow.getMany2manyService(many2manyServiceName);
    }

    /**
     * 系统初始化完成，true 准备好，false
     */
    private boolean systemPrepared = false;

    public boolean getSystemPrepare() {
        return systemPrepared;
    }

    public Boolean isPostgres() {
        return applicationConfig.getDataSource().isPostgres();
    }

    public Boolean isMysql() {
        return applicationConfig.getDataSource().isMysql();
    }

    public void setSystemPrepare(boolean prepared) {
        systemPrepared = prepared;
    }

    private final static Set<String> serviceNameSet = new HashSet<>();

    public void addServiceName(String serviceName) {
        serviceNameSet.add(serviceName);
    }

    public Set<String> getServiceNameSet() {
        return serviceNameSet;
    }

    private final static Hashtable<String, AbstractService> serviceClassServiceNameDic = new Hashtable<>(); // 类名，模型名

    public void addServiceName(String serviceClass, AbstractService service) {
        serviceClassServiceNameDic.put(serviceClass, service);
    }

    public Hashtable<String, AbstractService> getServiceClassServiceNameDic() {
        return serviceClassServiceNameDic;
    }


    private static ModuleList moduleList = new ModuleList();

    public void addModule(AbstractModule module) {
        moduleList.add(module);
    }

    public ModuleList getModuleList() {
        return moduleList;
    }

    public static final Map<String, ORMMapper> ormMapperMap = new Hashtable<>(); // 数据库对应的ORM

    public ORMMapper getORMMapper() {
        return ormMapperMap.get(getBaseName());
    }


    @Autowired
    @Lazy
    private DynamicJdbcTemplate jdbcTemplate;

    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL_CONTEXT = new ThreadLocal<>();

    private static final Hashtable<String, Object> globalData = new Hashtable<>();

    public void setGlobalData(String key, Object data) {
        globalData.put(key, data);
    }

    public Object getGlobalData(String key) {
        return globalData.get(key);
    }

    private Map<String, Object> createThreadMap() {
        synchronized (this) {
            Map<String, Object> map = THREAD_LOCAL_CONTEXT.get();
            if (ObjectUtils.isNull(map)) {
                map = new Hashtable<>();
                THREAD_LOCAL_CONTEXT.set(map);
            }
            return map;
        }
    }

    public synchronized void init(String database) {
        log.info("init database {}", database);
        database = database.toLowerCase();
        Map<String, Object> map = createThreadMap();
        map.put("databaseName", database);
        map.put("databaseConfig", applicationConfig.getDataSource());
        THREAD_LOCAL_CONTEXT.set(map);
        map.put("uuid", SnowflakeIdWorker3rd.getInstance().nextId());
        DynamicDataSource bean = SpringContextHolder.getContext().getBean(DynamicDataSource.class);
        if (!bean.contains(database)) {
            DataSourceUtil.addDataSourceToDynamic(database, applicationConfig.getDataSource());
        }


        if (getApplicationConfig().getMultiDb()) {
            if (!database.equals(getDefaultDatabase())) {
                if (!ormMapperMap.containsKey(database)) {
                    synchronized (Context.class) {
                        if (!ormMapperMap.containsKey(database)) {
                            ORMMapper ormMapper = initORM(database);
                            ormMapperMap.put(database, ormMapper);
                        }
                    }
                }
            }
        }

        avalonEvaluationContext = new AvalonEvaluationContext();
        avalonEvaluationContext.setContext(this);
        standardEvaluationContext = new StandardEvaluationContext(avalonEvaluationContext);
    }

    /**
     * 关闭当前数据库连接池
     *
     * @param db
     */
    public void closeDB(String db) {
        DynamicDataSource bean = SpringContextHolder.getContext().getBean(DynamicDataSource.class);
        bean.removeDataSource(db);
    }

    public String getDefaultDatabase() {
        if (isPostgres())
            return "postgres";
        return "mysql";
    }

    /**
     * 备份上下文
     *
     * @return
     */
    public Map<String, Object> backupThreadMap() {
        return createThreadMap();
    }

    /**
     * 恢复上下文
     *
     * @param map
     */
    public void recoverThreadMap(Map<String, Object> map) {
        THREAD_LOCAL_CONTEXT.set(map);
    }

    /**
     * 获取当前线程的唯一标识
     *
     * @return
     */
    public Integer getUUid() {
        Map<String, Object> map = createThreadMap();
        return (Integer) map.get("uuid");
    }

    public void setToken(String token) {
        Map<String, Object> map = createThreadMap();
        map.put(SystemConstant.TOKEN, token);
    }

    public String getToken() {
        Map<String, Object> map = createThreadMap();
        if (!map.containsKey(SystemConstant.TOKEN)) return null;
        return map.get(SystemConstant.TOKEN).toString();
    }

    public void setUserId(Integer userId) {
        Map<String, Object> map = createThreadMap();
        map.put("userId", userId);
        if (ObjectUtils.isNotNull(avalonEvaluationContext)) {
            avalonEvaluationContext.setUserId(userId);
        }
    }

    private final String ElevatePermissionName = "elevatePermission";

    /**
     * 提示临时权限
     *
     * @param elevatePermission 权限类型
     */
    public void addTemporaryElevate(ElevatePermissionEnum elevatePermission) {
        Map<String, Object> map = createThreadMap();
        Map<ElevatePermissionEnum, Integer> elevatePermissionEnums = null;
        if (map.containsKey(ElevatePermissionName)) {
            elevatePermissionEnums = (Map<ElevatePermissionEnum, Integer>) map.get(ElevatePermissionName);
        } else {
            elevatePermissionEnums = new Hashtable<>();
            map.put(ElevatePermissionName, elevatePermissionEnums);
        }
        Integer count = 0;
        if (elevatePermissionEnums.containsKey(elevatePermission)) {
            count = elevatePermissionEnums.get(elevatePermission);
        }
        count++;
        elevatePermissionEnums.put(elevatePermission, count);
    }

    public void addSystemState(SystemStateEnum systemStateEnum) {
        Map<String, Object> map = createThreadMap();

        List<SystemStateEnum> systemStateEnums = null;
        if (map.containsKey(SystemConstant.SYSTEM_STATE_NAME)) {
            systemStateEnums = (List<SystemStateEnum>) map.get(SystemConstant.SYSTEM_STATE_NAME);
        } else {
            systemStateEnums = new ArrayList<>();
            map.put(SystemConstant.SYSTEM_STATE_NAME, systemStateEnums);
        }
        systemStateEnums.add(systemStateEnum);
    }

    public void clearSystemState(SystemStateEnum systemStateEnum) {
        Map<String, Object> map = createThreadMap();

        List<SystemStateEnum> systemStateEnums = null;
        if (map.containsKey(SystemConstant.SYSTEM_STATE_NAME)) {
            systemStateEnums = (List<SystemStateEnum>) map.get(SystemConstant.SYSTEM_STATE_NAME);
            systemStateEnums.remove(systemStateEnum);
        }
    }

    public SystemStateEnum getSystemStateEnum() {
        Map<String, Object> map = createThreadMap();

        List<SystemStateEnum> systemStateEnums = null;
        if (map.containsKey(SystemConstant.SYSTEM_STATE_NAME)) {
            systemStateEnums = (List<SystemStateEnum>) map.get(SystemConstant.SYSTEM_STATE_NAME);
            if (systemStateEnums.size() > 0) {
                return systemStateEnums.get(systemStateEnums.size() - 1);
            }
        }
        return SystemStateEnum.none;
    }

    /**
     * 具有临时权限
     *
     * @param elevatePermission 权限类型
     */
    public boolean hasTemporaryElevate(ElevatePermissionEnum elevatePermission) {
        Map<String, Object> map = createThreadMap();
        Map<ElevatePermissionEnum, Integer> elevatePermissionEnums = null;
        if (!map.containsKey(ElevatePermissionName)) {
            return false;
        }

        elevatePermissionEnums = (Map<ElevatePermissionEnum, Integer>) map.get(ElevatePermissionName);

        if (!elevatePermissionEnums.containsKey(elevatePermission)) {
            return false;
        }

        return elevatePermissionEnums.get(elevatePermission) > 0;
    }

    /**
     * 删除临时权限
     *
     * @param elevatePermission 权限类型
     */
    public void clearTemporaryElevate(ElevatePermissionEnum elevatePermission) {
        Map<String, Object> map = createThreadMap();

        if (!map.containsKey(ElevatePermissionName)) {
            return;
        }

        Map<ElevatePermissionEnum, Integer> elevatePermissionEnums =
                (Map<ElevatePermissionEnum, Integer>) map.get(ElevatePermissionName);

        if (elevatePermissionEnums.containsKey(elevatePermission)) {
            elevatePermissionEnums.put(elevatePermission, elevatePermissionEnums.get(elevatePermission) - 1);
            if (elevatePermissionEnums.get(elevatePermission) <= 0) {
                elevatePermissionEnums.remove(elevatePermission);
            }
        }
    }

    public Integer getUserId() {
        Map<String, Object> map = createThreadMap();
        if (!map.containsKey(SystemConstant.USER_ID)) return null;
        return Integer.valueOf(map.get(SystemConstant.USER_ID).toString());
    }

    public String getUserName() {

        Map<String, Object> map = createThreadMap();
        if (map.containsKey(SystemConstant.USER_NAME)) return map.get(SystemConstant.USER_NAME).toString();

        Integer userId = getUserId();
        String userName = "";

        if (ObjectUtils.isNull(userId)) {
            return "null";
        }

        if (userId == 1) {
            userName = SystemConstant.USER_ADMIN;
        } else {
            AbstractService serviceBean = getServiceBean("base.user");
            FieldValue name = serviceBean.getFieldValue("name",
                    Condition.equalCondition(serviceBean.getPrimaryKeyName(), userId));
            userName = name.getString();
        }

        map.put(SystemConstant.USER_NAME, userName);

        return userName;
    }


    public String getBaseName() {
        return getDatabaseName();
    }

    public static String getDateTimeFormat() {
        return getApplicationConfigBean().getDatetimeFormat();
    }

    public static Logger getLogger(Class<?> cls) {
        return LoggerFactory.getLogger(cls);
    }

    public static Logger getLogger(Object obj) {
        return LoggerFactory.getLogger(obj.getClass());
    }

    public static String getDateFormat() {
        return getApplicationConfigBean().getDateFormat();
    }

    public static String getTimeFormat() {
        return getApplicationConfigBean().getTimeFormat();
    }

    public static String getDatabaseName() {
        Map<String, Object> map = THREAD_LOCAL_CONTEXT.get();

        if (ObjectUtils.isNull(map) || ObjectUtils.isNull(map.get("databaseName"))) {
            if (StringUtils.isNotEmpty(getApplicationConfigBean().getDataSource().getDatabase())) {
                return getApplicationConfigBean().getDataSource().getDatabase();
            }
            return getAvalonApplicationContextInstance().getBean(Context.class).getDefaultDatabase();
        }
        return map.get("databaseName").toString();
    }

    public <T> T getModule(Class<T> t) {
        return getClassBean(t);
    }

    public AbstractModule getModule(String moduleName) {
        return (AbstractModule) getAvalonApplicationContext().getBean(moduleName);
    }

    public Boolean containsBean(String beanName) {
        return getAvalonApplicationContext().containsBean(beanName);
    }

    public <T> T getClassBean(Class<T> t) {
        return getAvalonApplicationContext().getBean(t);
    }

    public ExternalService getNewExternalService() {
        return (ExternalService) getAvalonApplicationContext().getBean("external_service");
    }

    public String getORMServiceName(String serviceName) {
        if (!(getSystemStateEnum() == SystemStateEnum.uninstallModule ||
                getSystemStateEnum() == SystemStateEnum.installModule ||
                getSystemStateEnum() == SystemStateEnum.upgradeModule ||
                getSystemStateEnum() == SystemStateEnum.createDB ||
                getSystemStateEnum() == SystemStateEnum.dropDB)) { // 在安装、卸载、升级模块时，不进行ORM
            serviceName = getDbORM(getBaseName(), serviceName);
        }
        return serviceName;
    }

    public AbstractService getServiceBean(String serviceName) {
        try {
            String serviceNameWithDB = getORMServiceName(serviceName);

            return (AbstractService) getAvalonApplicationContext().getBean(serviceNameWithDB);
        } catch (Exception ex) {
            try {
                return getMany2manyService(serviceName);
            } catch (Exception ex2) {
                log.error(serviceName + "获取service bean失败", ex);
                return null;
            }
        }
    }

    public static ApplicationConfig getApplicationConfigBean() {
        return getAvalonApplicationContextInstance().getBean(ApplicationConfig.class);
    }

    /**
     * 返回类名
     *
     * @param springBeanClass 类
     * @return
     * @throws AvalonException
     */
    public String getFirstBeanName(Class<?> springBeanClass) throws AvalonException {
        GenericWebApplicationContext context = getAvalonApplicationContext();
        if (ObjectUtils.isNull(context)) {
            return springBeanClass.getTypeName();
        }
        String[] beanNamesForType = context.getBeanNamesForType(springBeanClass);

        for (String s : beanNamesForType) {
            BeanDefinition bean = context.getBeanDefinition(s);
            if (springBeanClass.getName().equals(bean.getBeanClassName())) {
                return s;
            }
        }
        return null;
    }

    public AvalonApplicationContext getAvalonApplicationContext() {
        return AvalonApplicationContext.getInstance();
    }

    public static AvalonApplicationContext getAvalonApplicationContextInstance() {
        return AvalonApplicationContext.getInstance();
    }

    public void registerAlias(String beanName, String alias) {
        getAvalonApplicationContext().registerAlias(beanName, alias);
    }

    /**
     * 删除一个service
     *
     * @param beanName service 或者 service.name
     */
    public void removeSingleton(String beanName) {
        try {
            DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) getAvalonApplicationContext().getBeanFactory();
            beanFactory.destroySingleton(beanName);
        } catch (Exception e) {
            log.error("removeSingleton error:" + e.getMessage(), e);
        }
    }


    public void registerSingleton(String beanName, AbstractService service) {
        String newBeanName = getDbORM(getBaseName(), beanName);
        if (!newBeanName.equals(beanName)) {
            getORMMapper().addSingletonServiceBean(beanName, service);
        }
        if (getAvalonApplicationContext().getBeanFactory().containsSingleton(newBeanName)) {
            removeSingleton(newBeanName);
        }
        getAvalonApplicationContext().getBeanFactory().registerSingleton(newBeanName, service);
    }


    /**
     * 获取分布式redis锁
     *
     * @param key
     * @return
     */
    public IRedisLock getRedisLock(String key) {
        Map<String, Object> map = createThreadMap();
        if (!map.containsKey(key)) return null;
        return (IRedisLock) map.get(key);
    }

    /**
     * 获取分布式redis锁
     *
     * @param key
     * @return
     */
    public void setRedisLock(String key, IRedisLock redisLock) {
        Map<String, Object> map = createThreadMap();
        map.put(key, redisLock);
    }

    private SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
    private StandardEvaluationContext standardEvaluationContext;
    private AvalonEvaluationContext avalonEvaluationContext;

    public Object executeScript(String script) {
        if (ObjectUtils.isEmpty(script)) return null;
        Expression expression = spelExpressionParser.parseExpression(script);
        if (ObjectUtils.isNull(standardEvaluationContext)) {
            return expression.getValue();
        }
        return expression.getValue(standardEvaluationContext);
    }

    public Object invokeServiceMethod(String serviceName, String methodName, Object... args) {
        AbstractService service = getServiceBean(serviceName);
        return service.invokeMethod(serviceName, methodName, args);
    }

    public void registerBean(String beanName, Class<?> beanClass) {
        // 获取 BeanFactory
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) getAvalonApplicationContext().getBeanFactory();

        // 创建 BeanDefinition
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON); // 可设置为 prototype 等

        // 注册 BeanDefinitionRegistry
        beanFactory.registerBeanDefinition(beanName, beanDefinition);
        log.info("注册 bean definition {}, class name {}", beanName, beanClass.getName());
    }

    // 刷新容器，bean生效
    public void refreshSpring() {
        getAvalonApplicationContext().refresh();
    }

    public void removeBeanDefinition(String beanName) {
        // 获取 BeanFactory
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) getAvalonApplicationContext().getBeanFactory();

        log.info("去除 bean definition {}", beanName);
        beanFactory.removeBeanDefinition(beanName);
    }

    public boolean containBeanDefinition(String beanName) {
        // 获取 BeanFactory
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) getAvalonApplicationContext().getBeanFactory();
        // 注册 Bean
        return beanFactory.containsBean(beanName);
    }

    public String getDbORM(String db, String serviceName) {
        if (getApplicationConfig().getMultiDb()) {
            if (!db.equals(getDefaultDatabase())) {
                if (ormMapperMap.containsKey(db)) {
                    return ormMapperMap.get(db).getServiceNameWithDb(serviceName);
                }
            }
            return serviceName;
        }
        return serviceName;
    }

    public void installOrUpgrade(List<AbstractModule> modules) {
        ORMMapper ormMapper = ormMapperMap.get(getBaseName());
        ModuleList abstractModules = ormMapper.copyModule();
        abstractModules.addAll(modules);
        installOrUpgrade(ormMapper, abstractModules);
    }

    public void uninstall(List<AbstractModule> modules) {
        ORMMapper ormMapper = ormMapperMap.get(getBaseName());

        ormMapper.getBeanServices().forEach((serviceName, serviceClass) -> {
            if (containsBean(serviceName)) {
                removeBeanDefinition(serviceName);
            }
        });
        ormMapper.getSingletonBeanServices().forEach((serviceName, serviceBean) -> {
            if (containsBean(serviceName)) {
                removeSingleton(serviceName);
            }
        });

        ormMapper.removeModule(modules);
        ModuleList abstractModules = ormMapper.copyModule();
        ormMapper.getModules().clear();
        ormMapper.getBeanServices().clear();
        ormMapper.clearSingletonServiceBean();
        installOrUpgrade(abstractModules);
    }

    /**
     * 清空数据
     *
     * @param db 数据库
     */
    public void clearDB(String db) {
        ORMMapper ormMapper = ormMapperMap.get(db);
        if (ObjectUtils.isNull(ormMapper)) return;
        ormMapperMap.remove(db);
    }

    public void dropDB(String db) {
        ORMMapper ormMapper = ormMapperMap.get(db);
        if (ObjectUtils.isNull(ormMapper)) return;
        ormMapperMap.remove(db);
        ormMapper.getBeanServices().forEach((serviceName, serviceClass) -> {
            if (containsBean(serviceName)) {
                removeBeanDefinition(serviceName);
            }
        });
        ormMapper.getSingletonBeanServices().forEach((serviceName, serviceBean) -> {
            if (containsBean(serviceName)) {
                removeSingleton(serviceName);
            }
        });

    }

    /**
     * 升级或安装模块
     *
     * @param ormMapper 数据库
     * @param modules   模块
     */
    public void installOrUpgrade(ORMMapper ormMapper, List<AbstractModule> modules) {
        for (AbstractModule module : modules) {
            ormMapper.addModule(module);
        }

        Map<String, Class<?>> rootServices = getAvalonApplicationContext().getDefaultORM().getRootServiceFromModule(modules);
        Map<String, Class<?>> allServices = getAvalonApplicationContext().getDefaultORM().getAllServiceFromModule();
        Map<String, List<Class<?>>> inheritServices = getAvalonApplicationContext().getDefaultORM()
                .getInheritServiceFromModule(modules);

        Map<String, Class<?>> inheritNewServices = getAvalonApplicationContext().getDefaultORM()
                .getInheritNewServiceFromModule(modules);


        // 注册模型bean
        Map<String, Class<?>> beanServices = new Hashtable<>(rootServices);

        ClassPool classPool = ClassPoolManager.createClassPool();
        inheritServices.forEach((serviceName, services) -> {
            Class<?> rootService = allServices.get(serviceName); // 根模型
            try {
                Class<?> enhancedClass = ClassPoolManager.createEnhancedClass(classPool, rootService, services);
                beanServices.put(serviceName, enhancedClass);
            } catch (NotFoundException | CannotCompileException e) {
                throw new RuntimeException(e);
            }
        });

        inheritNewServices.forEach((serviceName, serviceClass) -> {
            String[] split = serviceName.split("/");
            String newServiceName = split[0];
            String parentServiceName = split[1];
            Class<?> rootService = beanServices.get(parentServiceName); // 根模型
            try {
                Class<?> enhancedClass = ClassPoolManager.createEnhancedClass(classPool, rootService, List.of(serviceClass));
                beanServices.put(newServiceName, enhancedClass);
            } catch (NotFoundException | CannotCompileException e) {
                throw new RuntimeException(e);
            }
        });

        beanServices.forEach((serviceName, serviceClass) -> {
            if (containsBean(ormMapper.getServiceNameWithDb(serviceName))) {
                AbstractService serviceBean = getServiceBean(ormMapper.getServiceNameWithDb(serviceName));
                FieldHashMap relationFieldMap = serviceBean.getRelationFieldMap(); // 获取关系字段，删除全部已注册的关系模型
                for (Map.Entry<String, Field> stringFieldEntry : relationFieldMap.entrySet()) {
                    if (stringFieldEntry.getValue() instanceof Many2manyField) { //
                        String serviceRelativeName = ((Many2manyField) stringFieldEntry.getValue()).getTableSqlName();
                        if (containsBean(ormMapper.getServiceNameWithDb(serviceRelativeName))) {
                            removeSingleton(ormMapper.getServiceNameWithDb(serviceRelativeName));
                        }
                    }
                }
                removeBeanDefinition(ormMapper.getServiceNameWithDb(serviceName));
            }
            registerBean(ormMapper.getServiceNameWithDb(serviceName), serviceClass);
            ormMapper.addBeanService(serviceName, serviceClass);
        });
    }

    public ORMMapper initORM(String db) {
        log.info("init orm {}", db);
        ORMMapper ormMapper = new ORMMapper();
        ormMapper.setDb(db);
        if (!(getSystemStateEnum() == SystemStateEnum.createDB ||
                getSystemStateEnum() == SystemStateEnum.dropDB)) {// 在非安装或删除数据库情况下，执行
            List<String> moduleStr = getAvalonApplicationContext().getDefaultORM()
                    .getIModuleSupport().getInstalledModule(); // 获取已安装的模块
            List<AbstractModule> modules = new ArrayList<>();
            for (String module : moduleStr) {
                AbstractModule moduleBean = getModule(module);
                modules.add(moduleBean);
            }

            installOrUpgrade(ormMapper, modules);
        }


        return ormMapper;
    }

    public Record getDB() {
        return jdbcTemplate.getDB();
    }

    public Condition interpreter(String script) {
        return conditionManager.interpreter(this, script);
    }
}
