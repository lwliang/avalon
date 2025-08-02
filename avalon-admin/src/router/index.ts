/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {createRouter, createWebHistory, Router} from 'vue-router'
import {useRouteStore} from '../global/store/routeStore.ts'
import {getServiceAllMenuView} from '../api/commonApi.ts'

const modelRouter = {
    path: '/model',
    component: () => import('../pages/ModelParent.vue'),
    children: [] as any[]
}

const existModuleService: string[] = []; // 已经添加过的模块服务

export const addModelRouter = async (module:string, service:string) => {
    // if(!module || !service){
    //     return
    // }

    // if(existModuleService.includes(service)){
    //     return
    // }
    
    // existModuleService.push(service) // 标记为已添加
    
    // const views = await getServiceAllMenuView(service)
    // const modelWindowRouter = {
    //     path: `${module}/${service}/window`,
    //     component: () => import('../pages/window/ModelWindow.vue'),
    //     children: [] as any[]
    // }
    // console.log("views", views)
    // modelRouter.children.push(modelWindowRouter)
    // views.forEach((view: any) => {
    //     let children = modelWindowRouter.children
    //     if(view.viewMode === 'form'){
    //         children.push({
    //             path: 'form',
    //             component: () => import('../pages/window/action/form/form.vue')
    //         })
    //     }
    //     if(view.viewMode === 'tree'){
    //         children.push({
    //             path: 'tree',
    //             component: () => import('../pages/window/action/tree/tree.vue')
    //         })
    //     }
    //     if(view.viewMode === 'xtree'){
    //         children.push({
    //             path: 'xtree',
    //             component: () => import('../pages/window/action/xtree/xtree.vue')
    //         })
    //     }
    //     if(view.viewMode === 'kanban'){ 
    //         children.push({
    //             path: 'kanban',
    //             component: () => import('../pages/window/action/kanban/kanban.vue')
    //         })
    //     }
    // })
    // console.log(modelRouter)
    // 移除重复添加，因为modelRouter已经在初始化时添加过了
    // router.addRoute(modelRouter)
    
}

const router: Router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            component: () => import('../pages/Home.vue')
        },
        {
            path: '/login',
            component: () => import('../pages/Login.vue')
        },
        {
            path: '/register',
            component: () => import('../pages/Register.vue')
        },
        {
            path: '/reset_password',
            component: () => import('../pages/ResetPassword.vue')
        },
        {
            path: '/database/manager',
            component: () => import('../pages/DatabaseManager.vue')
        },
        {
            path: '/excalidraw',
            component: () => import('../pages/ExcalidrawPage.vue')
        },
        {
            path: '/model',
            component: () => import('../pages/ModelParent.vue'),
            children: [
                {
                    path: ':module/:service/window',
                    component: () => import('../pages/window/ModelWindow.vue'),
                    children: [
                        {
                            path: 'form',
                            component: () => import('../pages/window/action/form/form.vue')
                        },
                        {
                            path: 'tree',
                            component: () => import('../pages/window/action/tree/tree.vue')
                        },
                        {
                            path: 'kanban',
                            component: () => import('../pages/window/action/kanban/kanban.vue')
                        },
                        {
                            path: 'import',
                            component: () => import('../pages/window/action/excel/import-excel.vue')
                        }, {
                            path: 'xtree',
                            component: () => import('../pages/window/action/xtree/xtree.vue')
                        },
                        {
                            path: 'report',
                            component: () => import('../pages/window/action/report/report.vue')
                        }
                    ]
                }
            ]
        },
        {
            path: '/:pathMatch(.*)*',
            component: () => import('../pages/NotFound.vue')
        }
    ]
})
// router.addRoute(modelRouter)

router.beforeEach((to, from, next) => {
    const routeStore = useRouteStore();
    
    let module = ''
    let service = ''
    
    // 检查路径是否匹配 /model/{module}/{service}/window 模式
    const modelPathRegex = /^\/model\/([^\/]+)\/([^\/]+)\/window(\/.*)?$/
    const match = to.path.match(modelPathRegex)
    
    if (match) {
        module = match[1]  // 第一个捕获组：base
        service = match[2] // 第二个捕获组：base.module
        console.log('解析路径:', to.path)
        console.log('模块:', module)
        console.log('服务:', service)
    } else {
        // 兼容原有的 params 方式
        if (to.params.module) {
            module = to.params.module as string
        }
        if (to.params.service) {
            service = to.params.service as string
        }
    }
    
    routeStore.setCurrentRoute(to.path, module, service)
    routeStore.setBeforeRoute(from.path)
    
    if(module && service){  
        addModelRouter(module, service).then(() => {
            next()
        })
    }else{
        next()
    }
})

router.afterEach((to, from) => {
})

// 测试路径解析函数
export const testPathParsing = (path: string) => {
    const modelPathRegex = /^\/model\/([^\/]+)\/([^\/]+)\/window(\/.*)?$/
    const match = path.match(modelPathRegex)
    
    if (match) {
        const module = match[1]
        const service = match[2]
        const remainingPath = match[3] || ''
        
        console.log('路径:', path)
        console.log('模块:', module)
        console.log('服务:', service)
        console.log('剩余路径:', remainingPath)
        
        return { module, service, remainingPath }
    } else {
        console.log('路径不匹配模式:', path)
        return null
    }
}

export default router
