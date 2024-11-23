/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.module;

import com.avalon.core.service.AbstractService;
import com.avalon.core.service.AbstractServiceList;
import com.avalon.core.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ModuleHashMap {

    private ModuleList moduleList = new ModuleList();

    private Hashtable<String, AbstractServiceList> moduleServiceMap = new Hashtable<>(); // 模块名，模块下的服务列表
    private Hashtable<String, AbstractModule> packageServiceMap = new Hashtable<>(); // 模块包名，模块  com.xxx module
    private Hashtable<String, AbstractModule> moduleMap = new Hashtable<>(); // 模块名，模块 比如 base module


    public AbstractServiceList getAllService() {
        AbstractServiceList serviceList = new AbstractServiceList();
        List<AbstractService> collect = moduleServiceMap.values().stream()
                .flatMap(list -> list.stream()).collect(Collectors.toList());
        serviceList.addAll(collect);
        return serviceList;
    }

    public ModuleList getModuleList() {
        return moduleList;
    }

    public void setModuleList(ModuleList moduleList) {
        this.moduleList = moduleList;
    }


    public void addModule(AbstractModule module) {

        if (!moduleList.contains(module)) {
            moduleList.add(module);
        }
        moduleMap.put(module.getModuleName(), module);
        packageServiceMap.put(module.getClass().getPackageName(), module);
    }

    public AbstractModule getModule(String moduleName) {
        if (ObjectUtils.isNull(moduleList)) return null;

        return moduleMap.get(moduleName);
    }

    public boolean containsModule(String packageName) {
        return packageServiceMap.containsKey(packageName);
    }

    public AbstractModule getModuleByPackageName(String packageName) {
        if (ObjectUtils.isNull(moduleList)) return null;

        return packageServiceMap.get(packageName);
    }

    public AbstractServiceList getModuleServiceList(String moduleName) {
        return moduleServiceMap.get(moduleName);
    }

    public void put(String moduleName, AbstractService service) {

        synchronized (this) {
            if (!moduleServiceMap.containsKey(moduleName)) {
                moduleServiceMap.put(moduleName, new AbstractServiceList());
            }
            moduleServiceMap.get(moduleName).add(service);
        }

    }
}
