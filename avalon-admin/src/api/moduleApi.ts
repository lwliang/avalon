/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {getErpPrefix} from './env.ts'
import {getErpHttp, postErpHttp} from "./http.ts";


export function getModuleIcon(module: string,
                              iconPath: string): string {
    if (!iconPath) {
        return getErpPrefix() + "/module/icon/default"
    }
    if (!iconPath.startsWith("/")) {
        iconPath = "/" + iconPath
    }
    return getErpPrefix() + `/module/icon/down/${module}` + iconPath;
}


export function getPermissionModule() {
    return postErpHttp('/module/get/permission/module', {})
}

export function getInstallModule() {
    return postErpHttp('/module/get/install/module', {})
}

export function getModuleStartJS(module: string) {
    return getErpHttp(`/module/get/start/js/${module}`, {})
}

export function downloadModuleStartJS(module: string, filePath: string) {
    return getErpHttp(`/module/download/start/js/${module}/${filePath}`, {});
}

export function getModuleStartJS_URL(module: string, filePath: string) {
    return getErpPrefix() + `/module/download/start/js/${module}/${filePath}`
}