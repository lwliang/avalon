<script setup lang="ts">
import {computed} from 'vue'
import type {AvatarProps} from './types'

const props = withDefaults(defineProps<AvatarProps>(), {
  size: 'default',
  fit: 'cover',
  shape: 'square'
})

const emit = defineEmits<{
  (e: 'error', ev: Event): void
}>()

// 将自定义属性映射到 el-avatar 属性
const avatarSize = computed(() => {
  const sizeMap = {large: 'large', default: 'default', small: 'small'}
  return typeof props.size === 'number' ? props.size : sizeMap[props.size] ?? 'default'
})

const avatarShape = computed(() => {
  const shapeMap = {circle: 'circle', square: 'square'}
  return shapeMap[props.shape] ?? 'square'
})

const avatarFit = computed(() => {
  const fitMap: Record<string, string> = {fill: 'fill', contain: 'contain', cover: 'cover', none: 'none', scaleDown: 'scale-down'}
  return fitMap[props.fit] || 'cover'
})

function handleError(e: Event) {
  emit('error', e)
}
</script>

<template>
  <el-avatar
    :size="avatarSize"
    :shape="avatarShape"
    :fit="avatarFit"
    :src="src"
    :icon="icon"
    @error="handleError">
    <slot/>
  </el-avatar>
</template>
