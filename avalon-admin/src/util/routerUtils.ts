/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import router from '../router'

export function goLogin(param?: Object) {
    param = param || {}
    router.push({
        path: '/login'
    })
}

export function goRegister() {
    router.push({
        path: '/register'
    })
}

export function goResetPassword() {
    router.push({
        path: '/reset_password'
    })
}

export function goDatabaseManager() {
    router.push({
        path: '/database/manager'
    })
}

export function goModelParent() {
    router.push({
        path: `/model`
    })
}

export function goModelWindow(module: string, service: string, actionWindow: any) {
    router.push({
        path: `/model/${module}/${service}/window`,
        query: actionWindow
    })
}

export function goModelForm(module: string, service: string, id: any) {
    router.push({
        path: `/model/${module}/${service}/window/form`,
        query: {id}
    })
}

export function goModelTree(module: string, service: string, query?: any) {
    router.push({
        path: `/model/${module}/${service}/window/tree`,
        query: query
    })
}

export function goModelKanban(module: string, service: string, query?: any) {
    router.push({
        path: `/model/${module}/${service}/window/kanban`,
        query: query
    })
}