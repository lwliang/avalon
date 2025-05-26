<script setup lang="ts">
import {computed} from 'vue'
import type {BadgeProps} from './types'

const props = withDefaults(defineProps<BadgeProps>(), {
  type: 'danger',
  isDot: false,
  hidden: false
})

// 主题色映射
const typeColorMap: Record<string, string> = {
  primary: 'bg-primary text-white',
  success: 'bg-success text-white',
  warning: 'bg-warning text-white',
  danger: 'bg-danger text-white',
  info: 'bg-info text-white'
}

// 处理是否为点/数字
const badgeContent = computed(() => {
  if (props.isDot) return ''
  if (typeof props.value === 'number' && (props.max && props.value > props.max))
    return `${props.max}+`
  return props.value
})

// 最终类
const badgeClass = computed(() => [
  'inline-flex items-center justify-center select-none',
  'absolute ',
  props.isDot ? 'w-2 h-2 rounded-full top-[-2.5px] right-[-2.5px]' : 'h-5 min-w-5 px-2 rounded-full text-xs leading-5 -top-2 -right-2',
  props.color
      ? `text-white`
      : typeColorMap[props.type ?? 'danger'],
  props.color ? '' : '', // 若自定义色，用 bg色填充
  !props.hidden ? '' : 'hidden'
])

const badgeStyle = computed(() =>
    props.color ? {backgroundColor: props.color} : undefined
)
</script>

<template>
  <span class="relative inline-block align-middle">
    <!-- 默认插槽区域 -->
    <slot/>
    <!-- badge -->
    <sup
        v-if="!props.hidden && (props.isDot || props.value !== undefined && props.value !== null && props.value !== '')"
        :class="badgeClass"
        :style="badgeStyle">
      <template v-if="!props.isDot">{{ badgeContent }}</template>
    </sup>
  </span>
</template>
