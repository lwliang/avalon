<script setup lang="ts">
import { ref } from "vue";
import MyConditionValue from "./my-condition-value.vue";
import MyConditionOperate from "./my-condition-operate.vue";
import MyConditionField from "./my-condition-field.vue";
import Field from "../../model/Field.ts";
import { useFieldStore } from "../../global/store/fieldStore.ts";
import { FilterOperator } from "../../model/FilterCondition.ts";

const props = defineProps<{
  serviceName: string
}>()
/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/13 12:01
 */
const field = ref<string>('')
const operate = ref<FilterOperator | undefined>(undefined)
const value = ref<string>('')
const selectField = ref<Field | undefined>(undefined)

const selectFieldEvent = async (fieldData: any) => {
  const fieldStore = useFieldStore()
  const fields = await fieldStore.getFieldByServiceNameAsync(props.serviceName)
  const fieldService = fields.find((item) => item.name === fieldData.name)
  if (fieldService) {
    selectField.value = fieldService
  }
}

const getConditionString = (): string | null => {
  if (!field.value) {
    return null
  }
  if (!operate.value) {
    return null
  }
  if (!value.value) {
    return null
  }

  return `('${field.value}',${operate.value},'${value.value}')`
}

defineExpose({
  getConditionString
})
</script>

<template>
  <div class="flex gap-2">
    <myConditionField 
      v-model="field" 
      :service-name="serviceName" 
      @select-field="selectFieldEvent"
    />
    <my-condition-operate v-model="operate" customStyle="width: 200px"/>
    <my-condition-value 
      class="flex-1" 
      :service-name="serviceName" 
      :field="selectField" 
      v-model="value"
      :operate="operate"
    />
  </div>
</template>

<style scoped>

</style>