/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {defineStore} from "pinia";
import mittBus from "../bus/mittBus.ts";

export const useRouteStore = defineStore('routeStore', {
    state: () => ({
        currentRoute: '',
        beforeRoute: '',
        module: '',
        service: ''
    }),
    actions: {
        setCurrentRoute(route: string, module: string, service: string) {
            this.currentRoute = route
            if (this.module != module) {
                this.module = module
                if (module) {
                    mittBus.emit('changeModule', {module, click: false})
                }
            }
            this.service = service
        },
        setBeforeRoute(route: string) {
            this.beforeRoute = route;
        },
    },
    getters: {
        getCurrentRoute: (state) => {
            return state.currentRoute;
        },
        getBeforeRoute: (state) => {
            return state.beforeRoute;
        }
    }
})