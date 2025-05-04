package com.avalon.core.orm;

import com.avalon.core.module.AbstractModule;
import com.avalon.core.module.ModuleList;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lwlianghehe@gmail.com
 * @date 2024/12/06 9:37
 */
public class ORMMapper {
    @Getter
    private final ModuleList modules = new ModuleList(); //
    @Getter
    @Setter
    private String db;

    @Getter
    private final Map<String, Class<?>> beanServices = new Hashtable<>();
    @Getter
    private final Map<String, Object> singletonBeanServices = new Hashtable<>();


    public void addModule(AbstractModule module) {
        List<AbstractModule> collect = modules.stream().filter(
                x -> x.getModuleName().equals(module.getModuleName())).collect(Collectors.toList());
        if (collect.isEmpty()) {
            modules.add(module);
        }
    }

    public void removeModule(AbstractModule module) {
        modules.remove(module);
    }

    public String getServiceNameWithDb(String serviceName) {
        if (StringUtils.isEmpty(db)) {
            return serviceName;
        }
        return db + "." + serviceName;
    }

    public void addBeanService(String serviceName, Class<?> serviceClass) {
        beanServices.put(getServiceNameWithDb(serviceName), serviceClass);
    }

    public void removeModule(List<AbstractModule> moduleList) {
        for (AbstractModule abstractModule : moduleList) {
            modules.remove(abstractModule);
        }
    }

    public void addSingletonServiceBean(String serviceName, Object serviceBean) {
        singletonBeanServices.put(getServiceNameWithDb(serviceName), serviceBean);
    }

    public void removeSingletonServiceBean(String serviceName) {
        singletonBeanServices.remove(getServiceNameWithDb(serviceName));
    }

    public void clearSingletonServiceBean() {
        singletonBeanServices.clear();
    }

    public ModuleList copyModule() {
        return (ModuleList) modules.clone();
    }
}
