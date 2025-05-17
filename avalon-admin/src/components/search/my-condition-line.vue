<script setup lang="ts">
import {ref} from "vue";
import FormField from "../../model/FormField.ts";
import MyConditionValue from "./my-condition-value.vue";
import MyConditionOperate from "./my-condition-operate.vue";
import MyConditionField from "./my-condition-field.vue";
import Field from "../../model/Field.ts";
import {useGlobalFieldDataStore} from "../../global/store/fieldStore.ts";

const props = defineProps<{
  serviceName: string
}>()
/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/13 12:01
 */
const field = ref(new FormField(null))
const operate = ref(new FormField(null))
const value = ref(new FormField(null))
const selectField = ref<Field>();

const selectFieldEvent = async (field: any) => {
  useGlobalFieldDataStore().getFieldByServiceNameAsync(props.serviceName as string).then((data) => {
    const fieldService = data.find((item) => {
      return item.name == field.name
    })
    if (fieldService) {
      selectField.value = fieldService
    }
  })
}

const getConditionString = () => {
  if (!field.value.value) {
    return null
  }
  if (!operate.value.value) {
    return null
  }
  if (!value.value.value) {
    return null
  }

  return `('${field.value.value.name}',${operate.value.value.value},'${value.value.value}')`
}

defineExpose({
  getConditionString
})
</script>

<template>
  <div class="flex gap-2">
    <myConditionField v-model="field" :service-name="serviceName" @select-field="selectFieldEvent"></myConditionField>
    <my-condition-operate v-model="operate"></my-condition-operate>
    <my-condition-value :service-name="serviceName" :field="selectField" v-model="value" :operate="operate.value"></my-condition-value>
  </div>
</template>

<style scoped>

</style>