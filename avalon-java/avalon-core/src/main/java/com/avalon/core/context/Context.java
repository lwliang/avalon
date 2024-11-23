/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.context;

import com.avalon.core.condition.Condition;
import com.avalon.core.config.ApplicationConfig;
import com.avalon.core.config.PulsarConfig;
import com.avalon.core.db.DataSourceUtil;
import com.avalon.core.db.DynamicDataSource;
import com.avalon.core.db.DynamicJdbcTemplate;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.model.RecordRow;
import com.avalon.core.module.AbstractModule;
import com.avalon.core.module.ModuleHashMap;
import com.avalon.core.redis.IRedisLock;
import com.avalon.core.service.AbstractService;
import com.avalon.core.service.AbstractServiceList;
import com.avalon.core.service.ExternalService;
import com.avalon.core.util.FieldValue;
import com.avalon.core.util.ObjectUtils;
import com.avalon.core.util.SnowflakeIdWorker3rd;
import com.avalon.core.util.StringUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.GenericWebApplicationContext;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@Component()
@Data
public class Context {
    private static final Logger log = LoggerFactory.getLogger(Context.class);
    @Autowired
    private ApplicationConfig applicationConfig;
    @Autowired
    private PulsarConfig pulsarConfig;

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

    private final static AbstractServiceList serviceList = new AbstractServiceList();

    public AbstractServiceList getServiceList() {
        return serviceList;
    }

    public void putService(AbstractService service) {
        serviceList.add(service);
    }

    private static ModuleHashMap moduleHashMap = new ModuleHashMap();

    public static ModuleHashMap getModuleHashMap() {
        return moduleHashMap;
    }

    public ModuleHashMap getModuleMap() {
        return moduleHashMap;
    }

    public AbstractModule getModuleByName(String moduleName) {
        return moduleHashMap.getModule(moduleName);
    }

    public void addModule(AbstractModule module) {
        moduleHashMap.addModule(module);
    }

    public void putModule(AbstractModule module, AbstractService service) {
        moduleHashMap.put(module.getModuleName(), service);
    }

    public void putModule(String moduleName, AbstractService service) {
        putService(service);
        moduleHashMap.put(moduleName, service);
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

    public void init(String database) {
        log.info("init database {}", database);
        Map<String, Object> map = createThreadMap();
        map.put("databaseName", database);
        applicationConfig.getDataSource().setDatabase(database);
        map.put("databaseConfig", applicationConfig.getDataSource());
        THREAD_LOCAL_CONTEXT.set(map);
        map.put("uuid", SnowflakeIdWorker3rd.getInstance().nextId());
        DynamicDataSource bean = SpringContextHolder.getContext().getBean(DynamicDataSource.class);
        if (!bean.contains(database)) {
            DataSourceUtil.addDataSourceToDynamic(database, applicationConfig.getDataSource());
        }
        avalonEvaluationContext = new AvalonEvaluationContext();
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
        applicationConfig.getDataSource().setDatabase(map.get("databaseName").toString());
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
        return map.get(SystemConstant.TOKEN).toString();
    }

    public void setUserId(Integer userId) {
        Map<String, Object> map = createThreadMap();
        map.put("userId", userId);
        if (ObjectUtils.isNotNull(avalonEvaluationContext)) {
            avalonEvaluationContext.setUserId(userId);
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
        return getServiceBean(t);
    }

    public AbstractModule getModule(String moduleName) {
        return getModuleHashMap().getModule(moduleName);
    }

    public AbstractModule getModuleByPackageName(String packageName) {
        return getModuleHashMap().getModuleByPackageName(packageName);
    }

    public boolean containsModule(String packageName) {
        return getModuleHashMap().containsModule(packageName);
    }

    public Boolean containsBean(String beanName) {
        return getAvalonApplicationContext().containsBean(beanName);
    }

    public Boolean containsBean(Class<?> beanClass) {
        try {
            getAvalonApplicationContext().getBean(beanClass);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public <T> T getServiceBean(Class<T> t) {
        return getAvalonApplicationContext().getBean(t);
    }

    public ExternalService getNewExternalService() {
        return (ExternalService) getAvalonApplicationContext().getBean("external_service");
    }

    public <T> T getServiceBean(Class<T> t, String serviceName) {
        try {
            return (T) getAvalonApplicationContext().getBean(serviceName);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    public AbstractService getServiceBean(String serviceName) {
        try {
            return (AbstractService) getAvalonApplicationContext().getBean(serviceName);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    public static AbstractService getServiceBeanInstance(String serviceName) {
        try {
            return (AbstractService) getAvalonApplicationContextInstance().getBean(serviceName);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return null;
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

    public void registerSingleton(String beanName, AbstractService service) {
        getAvalonApplicationContext().getBeanFactory().registerSingleton(beanName, service);
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

    /**
     * 调用服务方法
     *
     * @param serviceName
     * @param methodName
     * @param ids
     * @param row
     * @return
     */
    public Object invokeServiceMethod(String serviceName, String methodName, List<Object> ids, RecordRow row) {
        AbstractService service = getServiceBean(serviceName);
        return service.invokeMethod(methodName, ids, row);
    }
}
