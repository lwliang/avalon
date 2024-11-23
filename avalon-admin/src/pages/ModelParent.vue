<script setup lang="tsx">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {useRoute} from "vue-router";
import ModuleMenu from "../components/module/menu/module-menu.vue";
import MyMenu from "../components/menu/my-menu.vue";
import {useGlobalMenuDataStore} from "../global/store/menuStore.ts";
import {useGlobalModuleDataStore} from "../global/store/moduleStore.ts";
import {goModelWindow} from "../util/routerUtils.ts";
import Module from "../model/Module.ts";
import UserInfo from "../components/user-info/user-info.vue";
import {useRouteStore} from "../global/store/routeStore.ts";
import mittBus from "../global/bus/mittBus.ts";
import MenuModel from "../model/MenuModel.ts";
import {invokeMethod} from "../api/modelApi.ts";
import {ComponentInternalInstance, getCurrentInstance, ref} from "vue";

const {proxy} = getCurrentInstance() as ComponentInternalInstance;

const menuStore = useGlobalMenuDataStore();
const routeStore = useRouteStore()
const moduleStore = useGlobalModuleDataStore();
const route = useRoute();
const serviceName = ref<string>(route.params.service as string)

if (!routeStore.module) {
    const module = 'base'
    menuStore.setModuleMenu(module).then((menus: any) => {
        const menu = getActionMenu(menus);
        if (menu) {
            goModelWindow(menu.action.serviceId.moduleId.name,
                menu.action.serviceId.name,
                {})
        }
    })
}

const getActionMenu = (menus: MenuModel[]) => {
    for (let menu of menus) {
        if (menu.children && menu.children.length) {
            return getActionMenu(menu.children);
        }

        if (menu.type == 'action') {
            return menu
        }
    }
}

const moduleClick = (module: Module) => {
    moduleStore.setCurrentModule(module);
    mittBus.emit('changeModule', {module: module.name, click: true})
}

const menuClick = (menu: MenuModel) => {
    if (menu.type == 'action') {
        goModelWindow(menu.action.serviceId.moduleId.name,
            menu.action.serviceId.name,
            {})
    } else {
        const param = {
            method: menu.objectAction,
            ids: [],
            param: {}
        }
        invokeMethod(menu.serviceId.name, param).then(data => {
            proxy?.$notify.success("提示", "操作成功");
        })
    }
}
</script>

<template>
    <div class="flex h-full bg-white">
        <div class="w-[100px] flex flex-col items-center border-r overflow-y-auto">
            <ModuleMenu @moduleClick="moduleClick"></ModuleMenu>
        </div>
        <div class="flex-1 flex flex-col ">
            <div class="h-[46px] flex items-center">
                <div class="pl-4 flex-1">
                    <MyMenu @menuClick="menuClick"></MyMenu>
                </div>
                <div class="pr-4">
                    <UserInfo></UserInfo>
                </div>
            </div>
            <div class="flex-1 overflow-y-auto">
                <div class="h-full ">
                    <router-view :key="route.fullPath"></router-view>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>

</style>