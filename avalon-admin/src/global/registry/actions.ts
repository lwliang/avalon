/**
 * @author lwlianghehe@gmail.com
 * @date 2025/04/06 10:20
 */
// actions.ts
import {Registry} from './registry';
import {createUploadDocumentDialog} from "../../components/dialog/my-dialog.ts";

// 示例动作 1
const logAction = {
    execute: (param: any) => {
        console.log(`[LogAction] ${param.message}`);
    },
};

// 示例动作 2
const alertAction = {
    execute: (param: any) => {
        alert(`[AlertAction]: ${param.message}`);
    },
};

const uploadDocument = {
    execute: (param: any) => {
        createUploadDocumentDialog('上传文件', true)
    },
}


// 动态注册动作到 "actions" 分类
export function registerActions(registry: Registry) {
    registry.register('actions', 'log', logAction);
    registry.register('actions', 'alter', alertAction);
    registry.register('actions', 'uploadDocument', uploadDocument);
}