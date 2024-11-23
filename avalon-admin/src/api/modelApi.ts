/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {postErpHttp} from "./http.ts";
import PageParam from "../model/PageParam.ts";
import {getPageSize} from "./env.ts";
import ServiceConditionPage from "../model/ServiceConditionPage.ts";
import ServiceInvokeParam from "../model/ServiceInvokeParam.ts";
import {useGlobalServiceDataStore} from "../global/store/serviceStore.ts";

export function getModelSelectionApi(serviceName: string, fields: string): Promise<any> {
    return postErpHttp(`/service/get/${serviceName}/selection/map`, {serviceName, fields})
}


export function getServiceFieldApi(serviceName: string): Promise<any> {
    return postErpHttp(`/service/get/${serviceName}/fields`, {serviceName})
}

export function addModelApi(value: Object, serviceName: string): Promise<any> {
    return postErpHttp(`/service/${serviceName}/add`, {serviceName, value})
}

export function getModelDetailApi(id: number, fields: string, serviceName: string): Promise<any> {
    return postErpHttp(`/service/get/${serviceName}/detail`, {
        serviceName,
        fields,
        rpnCondition: `(=,id,${id})`
    })
}

export function editModelApi(value: Object, serviceName: string): Promise<any> {
    return postErpHttp(`/service/${serviceName}/update`, {serviceName, value})
}

export function getModelPageApi(fields: string,
                                rpnCondition: string,
                                serviceName: string,
                                pageNum: number,
                                pageSize?: number): Promise<any> {
    const af = async () => {
        const serviceStore = useGlobalServiceDataStore();
        const service = await serviceStore.getServiceByNameAsync(serviceName)
        const pageParam: PageParam = {pageNum, pageSize: pageSize ? pageSize : getPageSize()}
        const param: ServiceConditionPage = {
            serviceName,
            page: pageParam,
            fields,
            order: `${service.keyField} desc`,
            isDistinct: true,
            rpnCondition
        }

        return postErpHttp(`/service/get/${serviceName}/page`, param)
    }
    return af()
}

export function deleteModelApi(id: number, serviceName: string) {
    return postErpHttp(`/service/${serviceName}/delete`, {serviceName, id})
}

export function getModelAllApi(fields: string,
                               rpnCondition: string,
                               serviceName: string) {
    const af = async () => {
        const param: any = {fields, rpnCondition, serviceName};
        const serviceStore = useGlobalServiceDataStore();
        const service = await serviceStore.getServiceByNameAsync(serviceName)
        param.order = `${service.keyField} desc`;
        return postErpHttp(`/service/get/${serviceName}/all`, param);
    }
    return af()
}


export function invokeMethod(serviceName: string, param: ServiceInvokeParam) {
    return postErpHttp(`/service/invoke/${serviceName}/method`, param)
}