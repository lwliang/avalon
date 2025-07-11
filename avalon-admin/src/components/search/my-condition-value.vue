<script setup lang="ts">
import Field from "../../model/Field.ts";
import { FilterOperator } from "../../model/FilterCondition.ts";

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/13 11:56
 */
const props = defineProps<{
  serviceName: string,
  field?: Field | undefined,
  operate: FilterOperator | undefined,
}>()

const formField = defineModel<string>({
  default: ''
})
</script>

<template>
  <el-input
    v-model="formField"
    placeholder="请输入查询值"
    clearable
    style="width: 100%"
  />
</template>

<style scoped>

</style>