/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {ref} from 'vue'
import {createGlobalState} from "@vueuse/core";
import Service from "../../model/Service.ts";
import {getServiceByServiceName} from "../../api/commonApi.ts";


export const useGlobalServiceDataStore = createGlobalState(() => {
    const service = ref<Service[]>([])
    const services = ref<any>({})

    function setServiceStore(value: Service[]) {
        service.value.splice(0, service.value.length);
        service.value.push(...value);
    }

    function addService(value: Service) {
        service.value.push(value);
    }

    function getServiceStore() {
        return service;
    }

    function getServiceByModuleId(moduleId: number) {
        return service.value.filter((item) => item.moduleId === moduleId);
    }

    function getServiceNameById(id: number) {
        return service.value.find((item) => item.id === id)?.name;
    }

    function getServiceIdByName(name: string) {
        return service.value.find((item) => item.name === name)?.id;
    }

    function getServiceByName(name: string): Service {
        return service.value.find((item) => item.name === name) as Service;
    }

    function getServiceById(id: number): Service {
        return service.value.find((item) => item.id === id) as Service;
    }

    async function getServiceByNameAsync(name: string): Promise<Service> {
        if (services.value[name]) return services.value[name]
        return await getServiceByServiceName(name).then(data => {
            if (data.length > 0) {
                services.value[name] = data[0]
            }
            return data[0]
        })
    }

    return {
        getServiceStore,
        setServiceStore,
        addService,
        getServiceNameById,
        getServiceByName,
        getServiceIdByName,
        getServiceById,
        getServiceByNameAsync
    }
})