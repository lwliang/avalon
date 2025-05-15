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
    getInstallModule,
    getModuleStartJS,
    getModuleStartJS_URL, getModuleStartVue, getModuleStartVue_URL, getModuleStartVue_URL_path,
    getPermissionModule
} from "../../api/moduleApi.ts";
import Module from "../../model/Module.ts";
import {loadScript} from "../../util/scriptUtils.ts";
import app from '../../main.ts'
import {render, h, createVNode} from 'vue';

type MittEvent = {
    changeModule: { module: string, click?: boolean }
    login: void,
    logout: void,
    loadUserInfo: void,
    loadMenu: void,
    loadModule: void,
    loadService: void,
    loadField: void,
    uploadFile: void
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

const handleLogoutEvent = () => {
    useGlobalMenuDataStore().clearMenu();
}
mittBus.on('login', handleLoginEvent);
mittBus.on('logout', handleLogoutEvent);
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
            goModelWindow(menu.moduleId ? menu.moduleId.name : menu.action.serviceId.moduleId.name,
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
        }
    }

    for (const module of installModules) {
        const moduleNames: string[] = await getModuleStartVue(module.name);
        for (const str of moduleNames) { // js 文件内容
            // loadVueComponent(getModuleStartVue_URL_path(module.name, str)).then(component => {
            //     if (component) {
            //         console.log("component", component)
            //         app.component('remote-component', component)
            //     }
            // })
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

export const emitLogout = () => {
    mittBus.emit('logout')
}

// 动态显示组件的函数
function showDynamicComponentVNode(dynamicComponent: any, containerId = 'app') {
    // 创建一个 DOM 容器
    let container = document.getElementById(containerId);
    if (!container) {
        container = document.createElement('div');
        container.id = containerId;
        document.body.appendChild(container);
    }

    // 创建虚拟节点
    const vnode = createVNode(dynamicComponent);

    // 渲染到容器中
    render(vnode, container);

    // 提供销毁方法
    return () => {
        render(null, container); // 卸载组件
        document.body.removeChild(container); // 移除容器
    };
}


export default mittBus;