<script setup lang="tsx">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {useRoute} from "vue-router";
import {ref, watch, provide} from "vue";
import {goModelForm, goModelKanban, goModelTree} from "../../util/routerUtils.ts";

import {onMounted} from "@vue/runtime-dom";
import {getModelAllApi} from "../../api/modelApi.ts";

const route = useRoute();

const viewMode = ref<string>()

const moduleName = ref<string | undefined>(route.params.module as string)
const serviceName = ref<string | undefined>(route.params.service as string)
const activeViewMode = ref<string>('');

if (route.fullPath.endsWith('/window')) {
    getModelAllApi("id,name,viewMode,label,serviceId",
        `(=,serviceId.name,${serviceName.value})`,
        "base.action.window")
        .then(data => {
            if (data.length) {
                activeViewMode.value = data[0].viewMode.split(",")[0];
            }
        })
}

const navigateToView = (viewMode: string) => {
    if (viewMode == 'kanban') {
        goServiceKanban();
    } else if (viewMode == 'tree') {
        goServiceTree()
    }
}

const goServiceForm = (id: number | undefined) => {
    if (moduleName.value && serviceName.value) {
        goModelForm(moduleName.value, serviceName.value, id)
    }
}

const goServiceTree = () => {
    if (moduleName.value && serviceName.value) {
        goModelTree(moduleName.value, serviceName.value)
    }
}

const goServiceKanban = () => {
    if (moduleName.value && serviceName.value) {
        goModelKanban(moduleName.value, serviceName.value)
    }
}


watch(() => activeViewMode.value, (newValue) => {
    if (newValue) {
        navigateToView(newValue)
    }
})

onMounted(() => {
    navigateToView(activeViewMode.value)
})

const rowClickHandler = (id: number | undefined) => {
    goServiceForm(id)
}
provide('rowClick', rowClickHandler)

</script>

<template>
    <router-view></router-view>
</template>

<style scoped>

</style>