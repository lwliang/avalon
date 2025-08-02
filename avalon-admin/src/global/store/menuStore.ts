/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {ref} from 'vue'
import {createGlobalState} from "@vueuse/core";
import MenuModel from "../../model/MenuModel.ts";
import {getModuleMenu} from "../../api/commonApi.ts";
import { addModelRouter } from '../../router/index.ts';


export const useMenuStore = createGlobalState(() => {
    const menus = ref<MenuModel[]>([])
    const currentModuleMenus = ref<MenuModel[]>([])
    const moduleMenus = ref<any>({})

    function setMenuStore(value: MenuModel[]) {
        menus.value.splice(0, menus.value.length);
        menus.value.push(...value);
    }

    function clearMenu() {
        menus.value.splice(0, menus.value.length);
        currentModuleMenus.value.splice(0, currentModuleMenus.value.length);
        moduleMenus.value = {}
    }

    function addMenu(value: MenuModel) {
        menus.value.push(value);
    }

    function getMenuStore() {
        return menus;
    }

    async function setModuleMenu(module: string) {
        if (moduleMenus.value[module]) {
            currentModuleMenus.value.splice(0, currentModuleMenus.value.length);
            currentModuleMenus.value.push(...moduleMenus.value[module]);
            return currentModuleMenus.value
        }
        const menuTemps = await getModuleMenu(module)

        // 添加模型路由 根据菜单添加路由 三级菜单
        menuTemps.forEach((menu: MenuModel) => {
            if(menu.action && menu.action.serviceId){
                addModelRouter(module, menu.action.serviceId.name)
            }
            if(menu.children){ // 二级菜单
                menu.children.forEach((child: MenuModel) => {
                    if(child.action && child.action.serviceId){
                        addModelRouter(module, child.action.serviceId.name)
                    }
                    if(child.children){ // 三级菜单
                        child.children.forEach((child: MenuModel) => {
                            if(child.action && child.action.serviceId){
                                addModelRouter(module, child.action.serviceId.name)
                            }
                        })
                    }
                })
            }
        })

        moduleMenus.value[module] = menuTemps;
        currentModuleMenus.value.splice(0, currentModuleMenus.value.length);
        currentModuleMenus.value.push(...menuTemps);
        return currentModuleMenus.value;
    }

    function getCurrentModuleMenu() {
        return currentModuleMenus;
    }

    return {getMenuStore, clearMenu, setMenuStore, addMenu, setModuleMenu, getModuleMenu: getCurrentModuleMenu}
})