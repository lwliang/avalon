/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {ref} from 'vue'
import {createGlobalState} from "@vueuse/core";
import Field from "../../model/Field.ts";
import {useGlobalServiceDataStore} from "./serviceStore.ts";
import Service from "../../model/Service.ts";
import {getServiceFieldApi} from "../../api/modelApi.ts";
import {getFieldsByServiceName} from "../../api/commonApi.ts";

const useServiceDataStore = useGlobalServiceDataStore();

export const useGlobalFieldDataStore = createGlobalState(() => {
    const field = ref<Field[]>([])
    const fields = ref<any>({})

    function setFieldStore(value: Field[]) {
        field.value.splice(0, field.value.length);
        field.value.push(...value);
    }

    function addField(value: Field) {
        field.value.push(value);
    }

    function getFieldStore() {
        return field;
    }

    function getFieldByServiceId(serviceId: number) {
        return field.value.filter((item) => item.serviceId === serviceId);
    }

    function getFieldByServiceName(service: string) {
        const serviceId = useServiceDataStore.getServiceIdByName(service) as number
        return field.value.filter((item) => item.serviceId === serviceId);
    }

    function getPrimaryKeyFieldByServiceId(serviceId: number): Field {
        return field.value.find((item) => item.serviceId === serviceId && item.isPrimaryKey) as Field;
    }

    function getPrimaryKeyFieldByServiceName(service: string): Field {
        const serviceId = useServiceDataStore.getServiceIdByName(service) as number
        return field.value.find((item) => item.serviceId === serviceId && item.isPrimaryKey) as Field;
    }

    async function getFieldByServiceNameAsync(service: string): Promise<Field[]> {
        if (fields.value[service]) return fields.value[service]
        return await getFieldsByServiceName(service).then(data => {
            fields.value[service] = data;
            return data
        })
    }

    return {
        getFieldStore,
        setFieldStore,
        addField,
        getFieldByServiceId,
        getPrimaryKeyFieldByServiceId,
        getFieldByServiceName,
        getFieldByServiceNameAsync,
        getPrimaryKeyFieldByServiceName
    }
})