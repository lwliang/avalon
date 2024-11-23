/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {postErpHttp} from "./http.ts";
import {getUserId} from "../cache/userStorage.ts";


export function login(db: String, username: string, password: string) {
    return postErpHttp('/login', {db, username, password})
}

export function register(name: string, username: string, password: string) {
    return postErpHttp('/register', {name, username, password})
}

export function getUserDetail() {
    return postErpHttp('/model/get/detail',
        {
            rnpCondition: `(id,=,${getUserId()})`,
            fields: 'id,account,name',
            serviceName: 'base.user'
        })
}