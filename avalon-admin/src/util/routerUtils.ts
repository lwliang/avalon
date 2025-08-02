/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import router, { addModelRouter } from '../router'

export function goRouterBack() {
    router.back()
}

export function goLogin(param?: Object) {
    param = param || {}
    router.push({
        path: '/login'
    })
}

export function goExcalidraw() {
    router.push({
        path: '/excalidraw'
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

export async function goModelWindow(module: string, service: string, actionWindow: any) {
    await addModelRouter(module, service)
    router.push({
        path: `/model/${module}/${service}/window`,
        query: actionWindow
    })
}

export function goModelImport(module: string, service: string, actionWindow: any) {
    router.push({
        path: `/model/${module}/${service}/window/import`,
        query: actionWindow
    })
}

export async function goModelForm(module: string, service: string, id: any) {
    await addModelRouter(module, service)
    router.push({
        path: `/model/${module}/${service}/window/form`,
        query: {id}
    })
}
export async function replaceModelForm(module: string, service: string, id: any) {
    await addModelRouter(module, service)
    router.replace({
        path: `/model/${module}/${service}/window/form`,
        query: {id}
    })
}

export async function goModelTree(module: string, service: string, query?: any) {
    await addModelRouter(module, service)
    router.push({
        path: `/model/${module}/${service}/window/tree`,
        query: query
    })
}

export async function goModelXTree(module: string, service: string, query?: any) {
    await addModelRouter(module, service)
    router.push({
        path: `/model/${module}/${service}/window/xtree`,
        query: query
    })
}

export async function goModelKanban(module: string, service: string, query?: any) {
    await addModelRouter(module, service)
    router.push({
        path: `/model/${module}/${service}/window/kanban`,
        query: query
    })
}

export async function goModelReport(module: string, service: string, query?: any) {
    await addModelRouter(module, service)
    router.push({
        path: `/model/${module}/${service}/window/report`,
        query: query
    })
}