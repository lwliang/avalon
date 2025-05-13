<script setup lang="ts">
import Field from "../../model/Field.ts";
import FormField from "../../model/FormField.ts";
import MyInput from "../input/my-input.vue";
import {conditionOperateType} from "./search.ts";
import {PropType} from "vue";

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/13 11:56
 */
const props = defineProps({
  serviceName: String,
  field: Object as PropType<Field>,
  operate: conditionOperateType
})

const formField = defineModel({type: FormField, required: true})


</script>

<template>
  <my-input v-model="formField"/>
</template>

<style scoped>

</style>