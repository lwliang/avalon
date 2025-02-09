/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {getModelAllApi} from "../../api/modelApi.ts";
import {useGlobalMenuDataStore} from "../store/menuStore.ts";
import {useGlobalModuleDataStore} from "../store/moduleStore.ts";
import {useGlobalServiceDataStore} from "../store/serviceStore.ts";
import {useUserInfoStore} from "../store/userInfoStore.ts"
import mitt from "mitt";
import {getUserDetail} from "../../api/loginApi.ts";
import MenuModel from "../../model/MenuModel.ts";
import {goModelWindow} from "../../util/routerUtils.ts";
import {
    downloadModuleStartJS, getInstallModule,
    getModuleStartJS,
    getModuleStartJS_URL,
    getPermissionModule
} from "../../api/moduleApi.ts";
import Module from "../../model/Module.ts";
import {loadScript} from "../../util/scriptUtils.ts";

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
    handleLoadModuleEvent();
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


const handleLoadModuleEvent = async () => {
    const data: Module[] = await getPermissionModule();
    useGlobalModuleDataStore().setModuleStore(data);

    const installModules = await getInstallModule()
    for (const module of installModules) {
        const moduleNames: string[] = await getModuleStartJS(module.name);
        for (const str of moduleNames) { // js 文件内容
            loadScript(getModuleStartJS_URL(module.name, str), null)
            // const jsContent = await downloadModuleStartJS(module.name, str)
            // eval(jsContent)
        }
    }
}

mittBus.on('loadModule', handleLoadModuleEvent)

const handleLoadServiceEvent = () => {
    getModelAllApi("id,label,name,tableName,moduleId,nameField,keyField",
        "",
        "base.service").then(data => {
        useGlobalServiceDataStore().setServiceStore(data);
    })
}

mittBus.on('loadService', handleLoadServiceEvent)

export const emitLogin = () => {
    mittBus.emit('login')
}


export default mittBus;