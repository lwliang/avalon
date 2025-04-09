import {compileScript, parse} from '@vue/compiler-sfc';
import * as Vue from 'vue';
import {compile} from 'vue';
// @ts-ignore
import {loadModule} from 'vue3-sfc-loader';
import {defineAsyncComponent} from "@vue/runtime-dom";
import {getModuleStartVue_URL} from "../api/moduleApi.ts";

export function loadScript(url: any, callback: any) {
    // 创建一个 <script> 标签
    const script = document.createElement('script');

    // 设置 script 的 src 属性为传入的 URL
    script.src = url;

    // 设置 script 的加载完成事件
    script.onload = () => {
        console.log(`Script loaded successfully from ${url}`);
        if (callback) {
            callback(); // 加载完成后执行回调函数
        }
    };

    // 设置加载错误的处理逻辑
    script.onerror = () => {
        console.error(`Failed to load script from ${url}`);
    };

    // 将 script 标签插入到 <head> 或 <body> 中
    document.head.appendChild(script);
}

export async function loadVueFile(vueText: string) {
    // 解析 .vue 文件 得到 script style template
    const {descriptor} = parse(vueText);

    if (!descriptor.template) return null;
    // 编译模板
    const render = compile(descriptor.template.content)

    // 提取脚本 setup 脚本，option脚本
    const script = descriptor.scriptSetup ? descriptor.scriptSetup.content :
        descriptor.script?.content;
    const scriptExports = compileScript(descriptor, {id: 'dynamic_component'});

    // 创建动态组件
    return {
        ...scriptExports,
        render,
    };
}

export async function loadVueComponent(vueUrl: string) {
    const options = {
        moduleCache: {
            vue: Vue,
        },
        async getFile(url: string) {
            return await getModuleStartVue_URL(url);
        },
        addStyle(textContent: string) {
            const style = Object.assign(document.createElement("style"), {
                textContent,
            });
            const ref = document.head.getElementsByTagName("style")[0] || null;
            document.head.insertBefore(style, ref);
        },
    };

    return defineAsyncComponent(async () => {
        const res = await loadModule(
            vueUrl,
            options
        );
        return res;
    });
}