/// <reference types="vite/client" />

import {ComponentCustomProperties} from "@vue/runtime-core";

import { ComponentCustomProperties } from 'vue'
import { ElNotification } from 'element-plus'

declare module '@vue/runtime-core' {
  interface ComponentCustomProperties {
    $notify: typeof ElNotification
  }
}export {};

declare global {
    interface Window {
        ReconnectingWebSocket: any,
        getToken: any,
        ws: any
    }
}

// 必须导出，才能在其他文件中使用
export default ComponentCustomProperties;