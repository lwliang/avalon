/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {isEqual, reduce, union, keys} from "lodash";

export function getDifference(obj1: any, obj2: any) {
    const allKeys = union(keys(obj1), keys(obj2));
    return reduce(allKeys, (result: any, key) => {
        if (!isEqual(obj1[key], obj2[key])) {
            result[key] = obj2[key];
        }
        return result;
    }, {});
}