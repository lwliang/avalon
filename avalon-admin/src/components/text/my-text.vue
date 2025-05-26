<script setup lang="ts">
import {computed, defineProps} from 'vue'
import type {TextProps, TextType, TextSize, TextWeight} from './types'

const props = defineProps<TextProps>()

// 颜色映射表
const typeClassMap: Record<TextType, string> = {
  default: 'text-text-regular',
  primary: 'text-primary',
  success: 'text-success',
  warning: 'text-warning',
  danger: 'text-danger',
  info: 'text-info',
}

// 字号
const sizeClassMap: Record<TextSize, string> = {
  large: 'text-lg',
  medium: 'text-base',
  small: 'text-sm',
}

// 字重
const weightClassMap: Record<TextWeight, string> = {
  regular: 'font-normal',
  medium: 'font-medium',
  bold: 'font-bold',
}

// 单行省略
const truncatedClass = computed(() =>
    props.truncated ? 'truncate' : ''
)

// 多行省略
const clampClass = computed(() =>
    props.lineClamp ? `line-clamp-${props.lineClamp}` : ''
)

const widthStyleClass = computed(() =>
    props.width !== undefined ?
        typeof props.width === 'number' ? `width:${props.width}px` : `width:${props.width}`
        : ''
)

const textClass = computed(() => [
  typeClassMap[props.type ?? 'default'],
  sizeClassMap[props.size ?? 'medium'],
  weightClassMap[props.weight ?? 'regular'],
  truncatedClass.value,
  clampClass.value,
  props.textClass
])

</script>

<template>
  <span :class="textClass" :style="widthStyleClass" :title="title">
    <slot/>
  </span>
</template>