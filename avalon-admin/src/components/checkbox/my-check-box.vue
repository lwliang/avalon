<script setup lang="ts">
import { computed } from 'vue'
import type { CheckboxProps } from './types'

const props = withDefaults(defineProps<CheckboxProps>(), {
  trueValue: true,
  falseValue: false,
  border: false,
  disabled: false,
  indeterminate: false
})

const emit = defineEmits<{
  (e: 'change', value: any): void
  (e: 'blur'): void
}>()

const checkboxValue = defineModel<string | number | boolean>()

// 计算 checkbox 的值
const isChecked = computed({
  get: () => {
    return checkboxValue.value === props.trueValue
  },
  set: (val: boolean) => {
    checkboxValue.value = val ? props.trueValue : props.falseValue
  }
})

// 处理 change 事件
const handleChange = (value: any) => {
  emit('change', checkboxValue.value)
}

// 处理 blur 事件
const handleBlur = () => {
  emit('blur')
}

const validate = () => {
  if (props.required) { // 必填
    if (!checkboxValue.value) {
      return false;
    }
  }
  return true
}

defineExpose({ validate })
</script>

<template>
  <el-checkbox
    v-model="isChecked"
    :label="props.label"
    :disabled="props.disabled"
    :indeterminate="props.indeterminate"
    :border="props.border"
    :size="props.size"
    :class="props.class"
    @change="handleChange"
    @blur="handleBlur"
  >
    <slot>{{ props.label }}</slot>
  </el-checkbox>
</template>

<style scoped>

</style>