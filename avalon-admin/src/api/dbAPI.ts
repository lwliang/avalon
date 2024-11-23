/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {getErpHttp} from "./http.ts";

export function getDB() {
    return getErpHttp('/db/get/database', {})
}

export function createDB(database: String) {
    return getErpHttp(`/db/create/database/${database}`, {})
}

export function dropDB(database: String) {
    return getErpHttp(`/db/drop/database/${database}`, {})
}