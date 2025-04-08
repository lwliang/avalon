/**
 * @author lwlianghehe@gmail.com
 * @date 2025/04/06 10:19
 */
// registryPlugin.ts
import {App} from 'vue';
import {Registry} from './registry.ts';

declare module '@vue/runtime-core' {
    interface ComponentCustomProperties {
        $registry: Registry;
    }
}

export default {
    install(app: App) {
        const registry = new Registry();

        // 将注册表注入到全局
        app.config.globalProperties.$registry = registry;

        // 提供给 Vue 应用的依赖注入
        app.provide('registry', registry);
    },
};