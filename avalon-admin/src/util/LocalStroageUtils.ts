/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {isArray, isObject} from "./typeUtils.ts";

export function save(key: string, value: any) {
    if (isObject(value) || isArray(value)) {
        localStorage.setItem(key, JSON.stringify(value))
    } else {
        localStorage.setItem(key, value)
    }
}

export function get(key: string): any {
    return localStorage.getItem(key)
}

export function getObject(key: string): any {
    const value = get(key)
    if (value) {
        return JSON.parse(value)
    }
    return null
}

export function getNumber(key: string): any {
    const value = get(key)
    if (value) {
        return parseInt(value)
    }
    return 0
}

export function remove(key: string) {
    localStorage.removeItem(key)
}
