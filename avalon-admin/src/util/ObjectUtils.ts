/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {isEqual, reduce, union, keys, isEmpty, cloneDeep} from "lodash";

export function getDifference(obj1: any, obj2: any) {
    const allKeys = union(keys(obj1), keys(obj2));
    return reduce(allKeys, (result: any, key) => {
        if (!isEqual(obj1[key], obj2[key])) {
            result[key] = obj2[key];
        }
        return result;
    }, {});
}

export function isObjectEmpty(obj: any) {
    return isEmpty(obj);
}

/**
 * 判断两个对象是否一致
 * @param obj 对象
 * @param other 对象1
 */
export function isObjectEqual(obj: any, other: any) {
    return isEqual(obj, other);
}

export function objectCloneDeep(obj: any) {
    return cloneDeep(obj);
}