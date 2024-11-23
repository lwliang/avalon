/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {ref} from 'vue'
import {defineStore} from 'pinia'
import Module from "../../model/Module.ts";

export const useGlobalModuleDataStore = defineStore("module",
    () => {
        const module = ref<Module[]>([])
        const installModule = ref<Module[]>([])
        const currentModule = ref<Module>()

        function setModuleStore(value: Module[]) {
            module.value.splice(0, module.value.length);
            module.value.push(...value);
            installModule.value.splice(0, installModule.value.length);
            installModule.value.push(...module.value.filter((item) => item.isInstall))
        }

        function addModule(value: Module) {
            module.value.push(value);
        }

        function setCurrentModule(module: Module) {
            currentModule.value = module;
        }

        function getCurrentModule() {
            return currentModule;
        }

        function getModuleStore() {
            return module;
        }

        function getInstallModule() {
            return installModule;
        }

        function getModuleNameById(id: number) {
            return module.value.find((item) => item.id === id)?.name;
        }

        return {
            getModuleStore,
            setModuleStore,
            addModule,
            getInstallModule,
            getModuleNameById,
            setCurrentModule,
            getCurrentModule
        }
    })