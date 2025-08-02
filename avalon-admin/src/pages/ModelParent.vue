<script setup lang="tsx">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {useRoute} from "vue-router";
import ModuleMenu from "../components/module/menu/module-menu.vue";
import ServiceMenu from "../components/menu/service-menu.vue";
import {useMenuStore} from "../global/store/menuStore.ts";
import {useModuleStore} from "../global/store/moduleStore.ts";
import {goModelWindow} from "../util/routerUtils.ts";
import Module from "../model/Module.ts";
import UserInfo from "../components/user-info/user-info.vue";
import {useRouteStore} from "../global/store/routeStore.ts";
import mittBus from "../global/bus/mittBus.ts";
import MenuModel from "../model/MenuModel.ts";
import {invokeMethod} from "../api/modelApi.ts";
import {ComponentInternalInstance, getCurrentInstance, ref} from "vue";
import MyThemeSwitch from "../components/theme-switch/my-theme-switch.vue";

const {proxy} = getCurrentInstance() as ComponentInternalInstance;

const menuStore = useMenuStore();
const routeStore = useRouteStore()
const moduleStore = useModuleStore();
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
    goModelWindow(menu.moduleId.name,
        menu.action.serviceId.name,
        {})
  } else {
    const param = {
      serviceName: menu.serviceId.name,
      method: menu.objectAction,
      param: {}
    }
    invokeMethod(menu.serviceId.name, param).then(data => {
      const result = data as any;
      if (!result) { // 没有返回值
        proxy?.$notify.success({
          title: "提示",
          message: "操作成功",
        });
      } else {
        if (result.type && result.type == 'ir.actions.client') { // 判断前端动作
          const service = proxy?.$registry.getAll('actions').get(result.tag) as any
          if (service) {
            service.execute(result.param);
          }
        }
      }
    })
  }
}
</script>

<template>
  <div class="flex h-full">
    <div
        class="w-[70px] flex flex-col items-center overflow-x-hidden overflow-y-auto flex-shrink-0 border-r border-border border-solid border-t-0 border-l-0 border-b-0">
      <ModuleMenu @moduleClick="moduleClick"></ModuleMenu>
    </div>
    <div class="flex-1 flex flex-col overflow-hidden">
      <div class="h-[46px] flex items-center w-full overflow-y-auto">
        <div class="pl-4 flex-1 overflow-x-auto">
          <div class="">
            <ServiceMenu @menuClick="menuClick"></ServiceMenu>
          </div>
        </div>
        <div class="pr-4 flex-shrink-0 flex gap-2 items-center">
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