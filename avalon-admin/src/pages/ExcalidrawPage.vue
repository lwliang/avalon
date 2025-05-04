<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2025/04/22 9:05
 */
import {ref} from 'vue'
import Excalidraw from "../components/excalidraw/ExcalidrawVue.vue";
import FormField from "../model/FormField.ts";

const draw = ref(new FormField(null))
</script>

<template>
    <Excalidraw v-model="draw" :show-back="true"/>
</template>

<style scoped>

</style>