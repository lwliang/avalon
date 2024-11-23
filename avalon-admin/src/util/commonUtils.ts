/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {getToken} from "../cache/tokenStorage.ts";

export const isLogin = () => {
    return !!getToken()
}

export const refreshPage = () => {
    window.location.reload()
}

export const isAbsoluteUrl = (url: string) => {
    return url.startsWith('http') || url.startsWith('https');
}