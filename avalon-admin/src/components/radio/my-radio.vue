<script setup lang="ts">
import {computed} from 'vue'
import type {RadioProps} from './types'

const props = withDefaults(defineProps<RadioProps>(), {
  disabled: false,
  border: false
})

// v-model
const formField = defineModel<string | number | boolean>()

const emit = defineEmits<{
  (e: 'change', value: any): void
  (e: 'blur'): void
}>()

// Element Plus size 映射
const elSize = computed(() => {
  const sizeMap = {
    large: 'large',
    default: 'default',
    small: 'small'
  }
  return sizeMap[props.size || 'default']
})

// 处理变化事件
const handleChange = (value: string | number | boolean) => {
  formField.value = value
  emit('change', value)
}

// 处理失焦事件
const handleBlur = () => {
  emit('blur')
}

// 校验逻辑
const validate = (): boolean => {
  if (props.required) {
    return !!(formField.value !== undefined && formField.value !== null && formField.value !== '')
  }
  return true
}

defineExpose({validate})
</script>

<template>
  <el-radio
    v-model="formField"
    :label="value"
    :disabled="disabled"
    :border="border"
    :size="elSize"
    :name="name"
    @change="handleChange"
    @blur="handleBlur"
  >
    <slot>{{ label }}</slot>
  </el-radio>
</template>

<style scoped>
/* 可以根据需要添加自定义样式 */
</style>
