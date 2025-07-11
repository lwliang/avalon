/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import { defineStore } from 'pinia'
import Service from "../../model/Service.ts";
import { getServiceByServiceName } from "../../api/commonApi.ts";

export const useServiceStore = defineStore('service', {
    state: () => ({
        services: [] as Service[],
        serviceCache: {} as Record<string, Service>
    }),

    actions: {
        /**
         * 设置服务列表
         * @param services 服务数组
         */
        setServices(services: Service[]) {
            this.services = services;
        },

        /**
         * 添加单个服务
         * @param service 服务对象
         */
        addService(service: Service) {
            this.services.push(service);
        },

        /**
         * 通过服务名异步获取服务（带缓存）
         * @param name 服务名
         */
        async getServiceByNameAsync(name: string): Promise<Service> {
            if (this.serviceCache[name]) {
                return this.serviceCache[name];
            }
            
            try {
                const data = await getServiceByServiceName(name);
                if (data.length > 0) {
                    this.serviceCache[name] = data[0];
                    return data[0];
                }
                throw new Error(`Service '${name}' not found`);
            } catch (error) {
                console.error(`Failed to fetch service '${name}':`, error);
                throw error;
            }
        },

        /**
         * 清空服务缓存
         */
        clearCache() {
            this.serviceCache = {};
        }
    },

    getters: {
        /**
         * 获取所有服务
         */
        getAllServices: (state) => state.services,

        /**
         * 根据模块ID获取服务列表
         */
        getServicesByModuleId: (state) => (moduleId: number) => {
            return state.services.filter(service => service.moduleId === moduleId);
        },

        /**
         * 根据ID获取服务名称
         */
        getServiceNameById: (state) => (id: number) => {
            return state.services.find(service => service.id === id)?.name;
        },

        /**
         * 根据服务名获取服务ID
         */
        getServiceIdByName: (state) => (name: string) => {
            return state.services.find(service => service.name === name)?.id;
        },

        /**
         * 根据服务名获取服务对象
         */
        getServiceByName: (state) => (name: string) => {
            return state.services.find(service => service.name === name);
        },

        /**
         * 根据ID获取服务对象
         */
        getServiceById: (state) => (id: number) => {
            return state.services.find(service => service.id === id);
        }
    }
})