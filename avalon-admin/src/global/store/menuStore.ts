/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {ref} from 'vue'
import {createGlobalState} from "@vueuse/core";
import MenuModel from "../../model/MenuModel.ts";
import {getModuleMenu} from "../../api/commonApi.ts";



export const useGlobalMenuDataStore = createGlobalState(() => {
    const menus = ref<MenuModel[]>([])
    const currentModuleMenus = ref<MenuModel[]>([])
    const moduleMenus = ref<any>({})

    function setMenuStore(value: MenuModel[]) {
        menus.value.splice(0, menus.value.length);
        menus.value.push(...value);
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
        moduleMenus.value[module] = menuTemps;
        currentModuleMenus.value.splice(0, currentModuleMenus.value.length);
        currentModuleMenus.value.push(...menuTemps);
        return currentModuleMenus.value;
    }

    function getCurrentModuleMenu() {
        return currentModuleMenus;
    }

    return {getMenuStore, setMenuStore, addMenu, setModuleMenu, getModuleMenu: getCurrentModuleMenu}
})