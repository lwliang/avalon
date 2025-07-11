<script setup lang="ts">
import {computed, watch} from 'vue'
import type {RateProps} from './types'

// props 定义
const props = withDefaults(defineProps<RateProps>(), {
  max: 5,
  size: 'default',
  lowThreshold: 2,
  highThreshold: 4,
  colors: () => ['#F7BA2A', '#F7BA2A', '#F7BA2A'],
  voidColor: '#C6D1DE',
  disabledVoidColor: '#C6D1DE',
  icon: 'Star',
  voidIcon: 'StarFilled',
  disabled: false,
  required: false
})

// emits
const emit = defineEmits<{
  (e: 'change', value: number): void
}>()

// v-model
const formField = defineModel<number>({
  default: 0
})

// Element Plus size 映射
const elSize = computed(() => {
  const sizeMap = {
    large: 'large',
    default: 'default',
    small: 'small'
  }
  return sizeMap[props.size || 'default']
})

// 校验逻辑
const validate = (): boolean => {
  if (props.required) {
    return !!(formField.value && formField.value > 0)
  }
  return true
}

// 处理评分变化
const handleChange = (value: number) => {
  formField.value = value
  emit('change', value)
}

defineExpose({validate})

// 监听值变化进行校验
watch(formField, () => {
  validate()
})
</script>

<template>
  <el-rate
    v-model="formField"
    :max="max"
    :size="elSize"
    :disabled="disabled"
    :low-threshold="lowThreshold"
    :high-threshold="highThreshold"
    :colors="colors"
    :void-color="voidColor"
    :disabled-void-color="disabledVoidColor"
    :icon="icon"
    :void-icon="voidIcon"
    @change="handleChange"
  />
</template>

<style scoped>
/* 可以根据需要添加自定义样式 */
</style>
