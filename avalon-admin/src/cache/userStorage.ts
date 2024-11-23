/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {get, remove, save} from "../util/LocalStroageUtils.ts";

export function setUserId(userId: number) {
    save('userId', userId)
}

export function getUserId(): number {
    return get('userId')
}

export function clearUserId() {
    remove('userId')
}