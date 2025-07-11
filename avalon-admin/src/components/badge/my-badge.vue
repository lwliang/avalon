<script setup lang="ts">
import {computed} from 'vue'
import type {BadgeProps} from './types'

const props = withDefaults(defineProps<BadgeProps>(), {
  type: 'danger',
  isDot: false,
  hidden: false
})

// 将自定义属性映射到 el-badge 属性
const badgeValue = computed(() => {
  if (props.isDot) return ''
  if (typeof props.value === 'number' && (props.max && props.value > props.max))
    return `${props.max}+`
  return props.value
})

const badgeType = computed(() => {
  // 将自定义类型映射到 Element Plus 类型
  const typeMap: Record<string, string> = {
    primary: 'primary',
    success: 'success', 
    warning: 'warning',
    danger: 'danger',
    info: 'info'
  }
  return typeMap[props.type] || 'danger'
})
</script>

<template>
  <el-badge
    :value="badgeValue"
    :type="badgeType"
    :is-dot="isDot"
    :hidden="hidden"
    :color="color"
    :max="max">
    <slot/>
  </el-badge>
</template>
