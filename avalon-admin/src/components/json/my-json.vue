<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2025/07/02 10:26
 */
import {Vue3JsonEditor} from 'vue3-json-editor'
import FormField from "../../model/FormField.ts";
import {ref, watch} from "vue";

const formField = defineModel<FormField>({required: true})
const emit = defineEmits<{
  (e: 'clear'): void
  (e: 'change', value: any): void
  (e: 'blur', event: FocusEvent): void
  (e: 'focus', event: FocusEvent): void
  (e: 'suffixIconClick'): void
  (e: 'keyup', event: KeyboardEvent): void
}>()

const onJsonChange = (value: any) => {
  emit('change', value)
}

const value = ref()

watch(() => formField.value.value, (newValue) => {
  if (newValue) {
    value.value = JSON.parse(newValue)
  }
}, {immediate: true})
watch(() => value.value, (newValue) => {
  if (newValue) {
    formField.value.value = JSON.stringify(newValue)
  } else {
    formField.value.value = ""
  }
})

/** 校验逻辑 */
function validate(): boolean {
  if (!formField.value) return true

  formField.value.isValidate = true
  return true
}

defineExpose({validate})
</script>

<template>
  <Vue3JsonEditor
      v-model="value"
      :expandedOnStart="true"
      @json-change="onJsonChange"
  />
</template>

<style scoped>

</style>