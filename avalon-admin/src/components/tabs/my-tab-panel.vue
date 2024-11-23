<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {ref, inject, getCurrentInstance} from "vue";
import {MyTabsContext, tabsRootContextKey} from "./my-tabs.ts";

const current = getCurrentInstance();
const tabContext = inject<MyTabsContext>(tabsRootContextKey)
const props = defineProps({
    label: String
})
const show = ref<boolean>(false)
tabContext?.registerTab({
    label: props.label,
    uid: current?.uid,
    active: show
})
</script>

<template>
    <div v-show="show" class="bg-white">
        <slot></slot>
    </div>
</template>

<style scoped>

</style>