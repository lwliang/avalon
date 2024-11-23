/// <reference types="vitest" />
import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from "@vitejs/plugin-vue-jsx";

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [vue(), vueJsx()],
    test: {
        environment: 'jsdom'
    },
    base: './',
    resolve: {
        alias: {
            vue: 'vue/dist/vue.esm-bundler.js'
        },
    },
    server: {
        proxy: {
            '/erp': {
                target: 'http://localhost:8090',
                changeOrigin: true
            },
            '/file': {
                target: 'http://localhost:8091',
                changeOrigin: true
            }
        }
    }
})
