/// <reference types="vite/client" />

import {ComponentCustomProperties} from "@vue/runtime-core";
import {CreateNotificationOptions} from "./components/notification";

declare module '@vue/runtime-core' {
    interface ComponentCustomProperties {
        $notify: {
            success: (title: string, message: string, duration?: number, options: Partial<CreateNotificationOptions> = {}) => void,
            warning: (title: string, message: string, duration?: number, options: Partial<CreateNotificationOptions> = {}) => void,
            error: (title: string, message: string, duration?: number, options: Partial<CreateNotificationOptions> = {}) => void,
            info: (title: string, message: string, duration?: number, options: Partial<CreateNotificationOptions> = {}) => void
        }; // 这里填类型
    }
}

export {};

declare global {
    interface Window {
        ReconnectingWebSocket: any,
        getToken: any,
        ws: any
    }
}

// 必须导出，才能在其他文件中使用
export default ComponentCustomProperties;