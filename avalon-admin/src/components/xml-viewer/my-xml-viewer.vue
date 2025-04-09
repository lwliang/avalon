<script setup lang="ts">
import FormField from "../../model/FormField.ts";

import {ref, watch} from "vue";
import CodeMirror from 'vue-codemirror6';
import {xml} from '@codemirror/lang-xml'
import {formatXML} from "../../util/xmlUtils.ts";
import {onMounted} from "@vue/runtime-dom";

/**
 * @author lwlianghehe@gmail.com
 * @date 2024/12/15 15:27
 */
const props = defineProps({
    htmlId: String,
    htmlName: String,
    required: Boolean,
    readonly: Boolean,
})

const formField = defineModel({
    type: FormField,
    required: true
})

const extensions = ref([xml()])  // 加载xml支持

onMounted(() => {
    if (formField.value.value) {
        formField.value.value = formatXML(formField.value.value)
    }
})

</script>

<template>
    <div class="max-h-[500px] overflow-y-auto border">
        <code-mirror v-model="formField.value" :extensions="extensions" :basic="true"/>
    </div>
</template>

<style scoped>

</style>