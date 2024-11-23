/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {get, remove, save} from "../util/LocalStroageUtils.ts";

export function setToken(token: string) {
    save('token', token)
}

export function getToken(): string {
    return get('token')
}

export function clearToken() {
    remove('token')
}