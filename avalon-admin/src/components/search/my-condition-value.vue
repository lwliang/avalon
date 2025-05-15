<script setup lang="ts">
import Field from "../../model/Field.ts";
import FormField from "../../model/FormField.ts";
import MyInput from "../input/my-input.vue";
import {ConditionOperateType} from "./search.ts";

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/13 11:56
 */
const props = defineProps<{
  serviceName: string,
  field: Field,
  operate: ConditionOperateType
}>()

const formField = defineModel({type: FormField, required: true})


</script>

<template>
  <my-input placeholder="查询值" v-model="formField"/>
</template>

<style scoped>

</style>