/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {createRouter, createWebHistory, Router} from 'vue-router'
import {useRouteStore} from '../global/store/routeStore.ts'


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
                        }
                    ]
                }
            ]
        },
        {
            path:'/:pathMatch(.*)*',
            component: () => import('../pages/NotFound.vue')
        }
    ]
})


router.beforeEach((to, from, next) => {
    const routeStore = useRouteStore();
    let module = ''
    if (to.params.module) {
        module = to.params.module as string
    }
    let service = ''
    if (to.params.service) {
        service = to.params.service as string
    }
    routeStore.setCurrentRoute(to.path, module, service)
    routeStore.setBeforeRoute(from.path)
    next()
})

router.afterEach((to, from) => {
})
export default router
