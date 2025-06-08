<script setup lang="ts">
import {computed, provide} from 'vue'
import type {RadioGroupProps} from './types'
import FormField from "../../model/FormField.ts";

// props与v-model
const props = withDefaults(defineProps<RadioGroupProps>(), {
  disabled: false,
  size: 'default'
})

const emit = defineEmits<{
  (e: 'change', value: any): void
  (e: 'blur'): void
}>()

const formField = defineModel<FormField>({required: true}) // v-model: 实际绑定数据（radio选中值）

// 提供group上下文（供Radio子项注入）
const group = {
  size: computed(() => props.size),
  disabled: computed(() => props.disabled),
  checkedValue: computed({
    get: () => formField.value.value,
    set: (val) => {
      formField.value.value = val
    }
  }),
  change: (val: string | number | boolean) => {
    formField.value.value = val
  }
}
provide('radioGroupContext', group)

const setValidate = (valid: boolean) => {
  if (formField.value) {
    formField.value.isValidate = valid
  }
}

const validate = () => {
  if (props.required) { // 必填
    if (formField.value && !formField.value.value) {
      formField.value.isValidate = false
      return false;
    }
  }

  setValidate(!!formField.value?.value)
  return formField.value?.isValidate
}

const inputBlurClick = () => {
  emit('blur')
}

defineExpose({validate})
</script>

<template>
  <div class="flex flex-wrap gap-4">
    <slot @blur="inputBlurClick"/>
  </div>
</template>
