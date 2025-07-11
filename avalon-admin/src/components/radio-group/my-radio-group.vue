<script setup lang="ts">
import {computed} from 'vue'
import type {RadioGroupProps} from './types'

// props与v-model
const props = withDefaults(defineProps<RadioGroupProps>(), {
  disabled: false,
  size: 'default'
})

const emit = defineEmits<{
  (e: 'change', value: any): void
  (e: 'blur'): void
}>()

const formField = defineModel<string | number | boolean>()

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
  <el-radio-group
    v-model="formField"
    :disabled="disabled"
    :size="elSize"
    @change="handleChange"
    @blur="handleBlur"
  >
    <slot />
  </el-radio-group>
</template>

<style scoped>
/* 可以根据需要添加自定义样式 */
</style>
