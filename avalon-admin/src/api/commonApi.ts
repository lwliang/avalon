/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {getModelAllApi} from "./modelApi.ts";
import _ from 'lodash'
import {arrayToTree} from "../util/treeUtils.ts";
import {getErpHttp, postDownloadFileHttp, postErpHttp, postUploadFileHttp} from "./http.ts";


export async function getModuleMenu(module: string) {
    return postErpHttp("/module/get/permission/menu", {
        module: module,
        field: "id,label,param,name,parentId,objectAction,serviceId.id,serviceId.name," +
            "sequence,type,icon,action.id,action.viewMode,action.label,action.serviceId.id," +
            "action.serviceId.name,action.serviceId.moduleId.id,action.serviceId.moduleId.name"
    }).then(data => {
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

export async function exportExcel(serviceName: string, fields: string, rpnCondition: string, order: string) {
    return postDownloadFileHttp(`/service/export/${serviceName}/excel`, {
        order,
        field: fields,
        condition: rpnCondition
    }).then((response) => {
        // 创建一个 URL 对象
        const url = window.URL.createObjectURL(new Blob([response.data]));

        // 创建一个临时的下载链接
        const link = document.createElement('a');
        link.href = url;
        // 获取 Content-Disposition 响应头
        const contentDisposition = response.headers.get('Content-Disposition');

        // 提取文件名
        let fileName = 'default-filename.xlsx';
        if (contentDisposition && contentDisposition.includes('filename=')) {
            const fileNameMatch = contentDisposition.match(/filename="?(.+)"?/);
            if (fileNameMatch.length > 1) {
                fileName = decodeURIComponent(fileNameMatch[1]);
            }
        }
        // 文件名设置为导出的文件名
        link.setAttribute('download', fileName);

        // 触发下载
        document.body.appendChild(link);
        link.click();

        // 清理 URL 对象
        document.body.removeChild(link);
        window.URL.revokeObjectURL(url);
    })
}

export function readExcelContent(serviceName: string, file: File) {
    return postUploadFileHttp(`/service/read/${serviceName}/excel`, file)
}

export function importExcel(serviceName: string, param: any) {
    return postErpHttp(`/service/import/${serviceName}/excel`, param)
}