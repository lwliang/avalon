package com.avalon.core.orm;

import com.avalon.core.module.AbstractModule;
import com.avalon.core.module.ModuleList;
import com.avalon.core.service.IModuleSupport;
import com.avalon.core.util.ClassUtils;
import com.avalon.core.util.ObjectUtils;
import com.avalon.core.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * 所有模块代码
 *
 * @author lwlianghehe@gmail.com
 * @date 2024/12/06 10:15
 */
public class DefaultORMMapper extends ORMMapper{
    @Getter
    private final ModuleList modules = new ModuleList(); // 所有模块
    @Getter
    @Setter
    private IModuleSupport iModuleSupport; // 适配所有数据库的模块表查询

    // base.user,com.avalon.core.service.UserService
    private Map<String, Class<?>> rootService = new Hashtable<>(); // 根模型 直接继承AbstractService类，且getInherit返回空
    private Map<String, List<Class<?>>> inheritService = new Hashtable<>(); // 扩展字段模型
    private Map<String, Class<?>> inheritNewService = new Hashtable<>(); // 扩展全新模型  格式 全新模型/被继承模型

    /**
     * 添加服务
     * 根模型 serviceName,null
     * 扩展字段模型 serviceName=inheritName
     * 扩展全新模型 serviceName!=inheritName 还是根模型
     *
     * @param serviceName 模型
     * @param inheritName 被继承的模型
     */
    public void addService(String serviceName, String inheritName, Class<?> className) {
        if (StringUtils.isEmpty(inheritName)) { // 根模型
            rootService.put(serviceName, className);
        } else {
            if (serviceName.equals(inheritName)) { // 扩展字段模型
                if (inheritService.containsKey(serviceName)) {
                    inheritService.get(serviceName).add(className);
                } else {
                    List<Class<?>> list = new ArrayList<>();
                    list.add(className);
                    inheritService.put(serviceName, list);
                }
            } else { // 扩展全新模型
                inheritNewService.put(serviceName + "/" + inheritName, className);
            }
        }
    }

    public void addModule(AbstractModule module) {
        modules.add(module);
    }

    public void removeModule(AbstractModule module) {
        modules.remove(module);
    }

    /**
     * 获取所有模块的继承全新模型
     *
     * @param modules 模块
     * @return
     */
    public Map<String, Class<?>> getInheritNewServiceFromModule(List<AbstractModule> modules) {
        Map<String, Class<?>> resultMap = new Hashtable<>();
        this.inheritNewService.forEach((key, value) -> {
            for (AbstractModule module : modules) {
                if (ClassUtils.isServiceOfModule(module, value)) { // 是同一个模块
                    resultMap.put(key, value);
                }
            }
        });

        return resultMap;
    }

    /**
     * 获取所有模块的根模型
     *
     * @param modules 模块
     * @return
     */
    public Map<String, Class<?>> getRootServiceFromModule(List<AbstractModule> modules) {
        Map<String, Class<?>> resultMap = new Hashtable<>();
        this.rootService.forEach((key, value) -> {
            for (AbstractModule module : modules) {
                if (ClassUtils.isServiceOfModule(module, value)) { // 是同一个模块
                    resultMap.put(key, value);
                }
            }
        });

        return resultMap;
    }

    public Map<String, Class<?>> getAllServiceFromModule() {
        return new Hashtable<>(this.rootService);
    }

    /**
     * 获取所有模块的扩展模型
     *
     * @param modules 模块
     * @return
     */
    public Map<String, List<Class<?>>> getInheritServiceFromModule(List<AbstractModule> modules) {
        Map<String, List<Class<?>>> resultMap = new Hashtable<>();
        this.inheritService.forEach((key, value) -> {
            List<Class<?>> values = new ArrayList<>();
            for (AbstractModule module : modules) {
                for (Class<?> aClass : value) {
                    if (ClassUtils.isServiceOfModule(module, aClass)) { // 是同一个模块
                        values.add(aClass);
                    }
                }
            }
            if (!values.isEmpty()) {
                resultMap.put(key, values);
            }
        });

        return resultMap;
    }
}
