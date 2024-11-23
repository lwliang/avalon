<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import './my-tabs.css'
import {provide, ref, Ref} from "vue";
import {MyTabPanelContext, tabsRootContextKey} from "./my-tabs.ts";
import {onMounted} from "@vue/runtime-dom";


const activeTab = ref<MyTabPanelContext>()
const panels: Ref = ref<MyTabPanelContext[]>([])
const root = {
    registerTab(tabPanel: MyTabPanelContext): void {
        panels.value.push(tabPanel)
    },
    tabClick(tabPanel: MyTabPanelContext): void {
        if (activeTab.value) {
            activeTab.value.active = false;
        }
        tabPanel.active = true;
        activeTab.value = tabPanel
    },
    activeTab,
    tabs: panels
}
provide(tabsRootContextKey, root)

const tabClickHandler = (tabPanel: MyTabPanelContext) => {
    root.tabClick(tabPanel)
}

onMounted(() => {
    if (panels.value.length) {
        panels.value[0].active = true;
        activeTab.value = panels.value[0];
    }
})
</script>

<template>
    <div>
        <div class="flex overflow-x-auto bg-gray-200 pt-1 pl-1 pr-1 mb-2">
            <div v-for="(panel,index) in panels" :key="index"
                 :class="['tab-panel-default' , 'rounded-t-lg' ,{'tab-panel-active':panel.active}]"
                 @click="tabClickHandler(panel)">
                {{ panel.label }}
            </div>
        </div>
        <div>
            <slot name="default"></slot>
        </div>
    </div>
</template>

<style scoped>

</style>