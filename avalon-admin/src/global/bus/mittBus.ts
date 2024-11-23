/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {getModelAllApi} from "../../api/modelApi.ts";
import {useGlobalMenuDataStore} from "../store/menuStore.ts";
import {useGlobalModuleDataStore} from "../store/moduleStore.ts";
import {useGlobalServiceDataStore} from "../store/serviceStore.ts";
import {useGlobalFieldDataStore} from "../store/fieldStore.ts";
import {useUserInfoStore} from "../store/userInfoStore.ts"
import mitt from "mitt";
import {getUserDetail} from "../../api/loginApi.ts";
import MenuModel from "../../model/MenuModel.ts";
import {goModelWindow} from "../../util/routerUtils.ts";

type MittEvent = {
    changeModule: { module: string, click?: boolean }
    login: void,
    loadUserInfo: void,
    loadMenu: void,
    loadModule: void,
    loadService: void,
    loadField: void,
}

const mittBus = mitt<MittEvent>();

/*
*
* 以下是全局加载默认数据事件
* */
const handleLoginEvent = () => {
    handleLoadMenuEvent();
    handleLoadModuleEvent();
    handleLoadServiceEvent();
    handleLoadFieldEvent();
    loadUserInfo();
}
mittBus.on('login', handleLoginEvent);
mittBus.on('loadUserInfo', () => {
    loadUserInfo();
});

const loadUserInfo = () => {
    getUserDetail().then(data => {
        useUserInfoStore().setUserInfo(data)
    })
}

async function goFirstMenu(menus: MenuModel[]) {
    if (menus.length) {
        let menu = menus[0];
        if (menu.children && menu.children.length) {
            menu = menu.children[0]
            if (menu.children && menu.children.length) { // 菜单最多三层
                menu = menu.children[0]
            }
        }
        if (menu) {
            console.log("changeModule", menu)
            goModelWindow(menu.action.serviceId.moduleId.name,
                menu.action.serviceId.name,
                {})
        }
    }
}

mittBus.on('changeModule', async (args) => {
    const menus = await useGlobalMenuDataStore().setModuleMenu(args.module as string);
    if (args.click) {
        goFirstMenu(menus)
    }
})

const handleLoadMenuEvent = () => {
    getModelAllApi("id,label,param,name,sequence,type,icon,objectAction,serviceId.id,serviceId.name" +
        ",action.id,action.viewMode,action.label,action.serviceId.id," +
        "action.serviceId.name,action.serviceId.moduleId.id,action.serviceId.moduleId.name",
        "",
        "base.menu").then(data => {
        useGlobalMenuDataStore().setMenuStore(data)
    })
}

mittBus.on('loadMenu', handleLoadMenuEvent)

const handleLoadModuleEvent = () => {
    getModelAllApi("id,label,name,icon,description,display,isInstall",
        "",
        "base.module").then(data => {
        useGlobalModuleDataStore().setModuleStore(data);
    })
}

mittBus.on('loadModule', handleLoadModuleEvent)

const handleLoadServiceEvent = () => {
    getModelAllApi("id,label,name,tableName,moduleId,nameField,keyField",
        "",
        "base.service").then(data => {
        useGlobalServiceDataStore().setServiceStore(data);
    })
}
const handleLoadFieldEvent = () => {
    getModelAllApi("id,label,name,isPrimaryKey,isAutoIncrement,isRequired,isReadonly,defaultValue," +
        "type,serviceId,isUnique,allowNull,minValue,maxValue,masterForeignKeyName,relativeForeignKeyName," +
        "relativeServiceName,manyServiceTable,relativeFieldName",
        "",
        "base.field").then(data => {
        useGlobalFieldDataStore().setFieldStore(data);
    })
}
mittBus.on('loadService', handleLoadServiceEvent)
mittBus.on('loadField', handleLoadFieldEvent)


export const emitLogin = () => {
    mittBus.emit('login')
}


export default mittBus;