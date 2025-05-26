<script setup lang="ts">
import {computed, provide} from 'vue'
import type {CheckboxGroupProps} from './types'
import FormField from "../../model/FormField";


const props = defineProps<CheckboxGroupProps>()
const formField = defineModel<FormField>({required: true}) // 数组型 value

const emit = defineEmits<{
  (e: 'valueChange', value: any): void
}>()
// 统一向下提供
const group = {
  size: computed(() => props.size || 'default'),
  disabled: computed(() => props.disabled),
  checkedValues: computed({
    get: () => Array.isArray(formField.value.value) ? formField.value.value : [],
    set: (val: any[]) => {
      formField.value.value = val
    }
  }),
  toggleCheck: (val: string | number, checked: boolean) => {
    const arr = Array.isArray(formField.value.value) ? [...formField.value.value] : []
    const idx = arr.indexOf(val)
    if (checked && idx === -1) arr.push(val)
    if (!checked && idx !== -1) arr.splice(idx, 1)
    formField.value.value = arr
    emit('valueChange', formField.value.value)
  }
}
provide('checkboxGroupContext', group)
</script>

<template>
  <div>
    <label v-if="props.label" class="block mb-2 text-text-regular">{{ props.label }}</label>
    <div class="flex flex-wrap gap-4">
      <slot/>
    </div>
  </div>
</template>
