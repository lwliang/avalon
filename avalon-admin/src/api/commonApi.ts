/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {getModelAllApi} from "./modelApi.ts";
import _ from 'lodash'
import {arrayToTree} from "../util/treeUtils.ts";
import {useGlobalServiceDataStore} from "../global/store/serviceStore.ts";
import {postErpHttp} from "./http.ts";


export async function getModuleMenu(module: string) {
    return getModelAllApi("id,label,param,name,parentId,objectAction,serviceId.id,serviceId.name," +
        "sequence,type,icon,action.id,action.viewMode,action.label,action.serviceId.id," +
        "action.serviceId.name,action.serviceId.moduleId.id,action.serviceId.moduleId.name",
        `(=,moduleId.name,${module})`,
        "base.menu").then(data => {
        data = _.sortBy(data, ['sequence', 'id'])
        data = arrayToTree(data)
        console.log(data)
        return data
    })
}

export function getFieldsByServiceName(serviceName: string): Promise<any> {
    return getModelAllApi("id,label,name,isPrimaryKey,isAutoIncrement,isRequired,isReadonly,defaultValue," +
        "type,serviceId,isUnique,allowNull,minValue,maxValue,masterForeignKeyName,relativeForeignKeyName," +
        "relativeServiceName,manyServiceTable,relativeFieldName",
        `(=,serviceId.name,${serviceName})`,
        "base.field")
}

export function getServiceByServiceName(serviceName: string): Promise<any> {
    const param: any = {
        fields: "id,label,name,tableName,moduleId,nameField,keyField",
        rpnCondition: `(=,name,${serviceName})`,
        serviceName: serviceName
    };
    param.order = `id desc`;
    return postErpHttp(`/service/get/base.service/all`, param);
}

export function getActionView(serviceName: string, viewMode: string) {
    return getModelAllApi("id,name,viewMode,label,priority,arch",
        `(&,(=,serviceId.name,${serviceName}),(=,viewMode,${viewMode}))`,
        'base.action.view')
}

export function getActionTreeView(serviceName: string) {
    return getModelAllApi("id,name,viewMode,label,priority,arch",
        `(&,(=,serviceId.name,${serviceName}),(=,viewMode,tree))`,
        'base.action.view')
}

export function getActionFormView(serviceName: string) {
    return getModelAllApi("id,name,viewMode,label,priority,arch",
        `(&,(=,serviceId.name,${serviceName}),(=,viewMode,form))`,
        'base.action.view')
}

export function getActionKanbanView(serviceName: string) {
    return getModelAllApi("id,name,viewMode,label,priority,arch",
        `(&,(=,serviceId.name,${serviceName}),(=,viewMode,kanban))`,
        'base.action.view')
}

export function getActionSearchView(serviceName: string) {
    return getModelAllApi("id,name,viewMode,label,priority,arch",
        `(&,(=,serviceId.name,${serviceName}),(=,viewMode,search))`,
        'base.action.view')
}