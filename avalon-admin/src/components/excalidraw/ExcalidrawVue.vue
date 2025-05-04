<script setup lang="ts">
// @ts-ignore
import {createRoot} from "react-dom/client";
// @ts-ignore
import React from "react";
import {Excalidraw} from "@excalidraw/excalidraw";
import '@excalidraw/excalidraw/index.css'
import {onMounted, onUnmounted, inject, watch} from 'vue'
import {useTemplateRef} from "@vue/runtime-dom";
import FormField from "../../model/FormField.ts";
import {goRouterBack} from "../../util/routerUtils.ts";

const props = defineProps({
    htmlId: String,
    htmlName: String,
    required: Boolean,
    readonly: Boolean,
    showBack: {
        type: Boolean,
        default: false
    }
})

const formField = defineModel({
    type: FormField,
    required: true
})

const scrollValue = inject('formScrollEvent', {scrollTop: 0, scrollLeft: 0})

watch(() => scrollValue, (newVal) => {
    if (app) {
        app.refresh()
    }
}, {
    deep: true,
})


let root: any = null;
let app: any = null;

const excalidrawRef = useTemplateRef('excalidraw')

onMounted(() => {
    init()
})

function FullScreenButton() {
    // 兼容所有浏览器的 requestFullscreen
    const handleFullScreen = () => {
        if(props.showBack) {
            goRouterBack()
            return;
        }
        const el: any = excalidrawRef.value
        if (!el) return
        if (el.requestFullscreen) {
            el.requestFullscreen()
        } else if (el.webkitRequestFullscreen) {
            el.webkitRequestFullscreen()
        } else if (el.mozRequestFullScreen) {
            el.mozRequestFullScreen()
        } else if (el.msRequestFullscreen) {
            el.msRequestFullscreen()
        }
    }
    return React.createElement(
        'button',
        {
            style: {
                padding: '6px 16px',
                border: '1px solid #ccc',
                borderRadius: '4px',
                background: '#fff',
                cursor: 'pointer',
                fontSize: '14px',
                boxShadow: '0 1px 4px #0002',
            },
            onClick: handleFullScreen,
        },
        props.showBack ? '后退' : '全屏'
    )
}

const init = () => {
    root = createRoot(excalidrawRef.value);
    const savedElements = formField.value.value
    const initialData = savedElements
        ? JSON.parse(savedElements)
        : {elements: []}
    root.render(
        React.createElement(Excalidraw, {
            name: "我的画板",
            langCode: "zh-CN",
            detectScroll: false,
            renderScrollbars: true,
            initialData: initialData,
            onChange: onChange,
            onLibraryChange: onLibraryChange,
            excalidrawAPI: excalidrawAPI,
            renderTopRightUI: FullScreenButton
            // props
        })
    );
}

onUnmounted(() => {
    root.unmount();
})

function onChange(elements: any, newAppState: any) {
    let {collaborators, ...appState} = newAppState
    formField.value.value = JSON.stringify({elements: elements, appState});
}

function onLibraryChange() {

}

function excalidrawAPI(e: any) {
    app = e;
}

</script>

<template>
    <div class="w-full h-full relative" :id="htmlId">
        <div class="excalidraw" ref="excalidraw"></div>
    </div>
</template>

<style scoped>
.excalidraw {
    min-height: 800px;
}
</style>
