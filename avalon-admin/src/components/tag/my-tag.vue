<script setup lang="ts">
import {computed} from 'vue'
import type {TagProps} from './types'

const props = withDefaults(defineProps<TagProps>(), {
  type: 'primary',
  effect: 'light',
  size: 'small',
  round: false,
  closable: false,
  color: ''
})

const emit = defineEmits<{
  (e: 'close', value: any): void
  (e: 'click', value: any): void
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

// Element Plus effect 映射
const elEffect = computed(() => {
  const effectMap = {
    dark: 'dark',
    light: 'light',
    plain: 'plain'
  }
  return effectMap[props.effect || 'light']
})

const handleClose = (event: Event) => {
  event.stopPropagation()
  emit('close', props.value)
}

const handleClick = () => {
  emit('click', props.value)
}
</script>

<template>
  <el-tag
    :type="type"
    :size="elSize"
    :effect="elEffect"
    :closable="closable"
    :color="color"
    :round="round"
    @close="handleClose"
    @click="handleClick"
  >
    <template v-if="label">
      {{ label }}
    </template>
    <template v-else>
      <slot/>
    </template>
  </el-tag>
</template>

<style scoped>
/* 可以根据需要添加自定义样式 */
</style>
