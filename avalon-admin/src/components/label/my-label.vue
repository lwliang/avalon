<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import { computed } from 'vue'
import type { LabelProps } from './types'

const props = withDefaults(defineProps<LabelProps>(), {
  type: 'default',
  size: 'default',
  closable: false,
  disableTransitions: false,
  hit: false,
  effect: 'light'
})

// 映射类型到 Element Plus 的 tag 类型
const mapTagType = (type: string) => {
  const typeMap: Record<string, string> = {
    default: '',
    primary: 'primary',
    success: 'success',
    warning: 'warning',
    danger: 'danger',
    info: 'info'
  }
  return typeMap[type] || ''
}

// 映射尺寸到 Element Plus 的尺寸
const mapTagSize = (size: string) => {
  const sizeMap: Record<string, string> = {
    large: 'large',
    default: 'default',
    small: 'small'
  }
  return sizeMap[size] || 'default'
}

// 计算 tag 属性
const tagProps = computed(() => ({
  type: mapTagType(props.type),
  size: mapTagSize(props.size),
  effect: props.effect,
  closable: props.closable,
  disableTransitions: props.disableTransitions,
  hit: props.hit,
  color: props.color
}))
</script>

<template>
  <el-tag v-bind="tagProps">
    <template v-if="$slots.icon || props.icon" #icon>
      <slot name="icon">
        <el-icon v-if="props.icon" class="mr-1">
          <component :is="props.icon" />
        </el-icon>
      </slot>
    </template>
    <slot />
  </el-tag>
</template>