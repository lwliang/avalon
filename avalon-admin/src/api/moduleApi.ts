/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {getErpPrefix} from './env.ts'


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
