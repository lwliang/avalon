<script setup lang="ts">
import { computed } from 'vue'
import type { TextProps, TextType, TextSize, TextWeight } from './types'

const props = defineProps<TextProps>()

// Element Plus text 类型映射
const elTextType = computed(() => {
  const typeMap: Record<TextType, string> = {
    default: '',
    primary: 'primary',
    success: 'success',
    warning: 'warning',
    danger: 'danger',
    info: 'info',
  }
  return typeMap[props.type ?? 'default']
})

// Element Plus text 尺寸映射
const elTextSize = computed(() => {
  const sizeMap: Record<TextSize, string> = {
    large: 'large',
    medium: 'default',
    small: 'small',
  }
  return sizeMap[props.size ?? 'medium']
})

// 计算样式
const textStyle = computed(() => {
  const styles: Record<string, string> = {}
  
  // 宽度
  if (props.width !== undefined) {
    styles.width = typeof props.width === 'number' ? `${props.width}px` : props.width
  }
  
  // 字重
  if (props.weight) {
    const weightMap: Record<TextWeight, string> = {
      regular: 'normal',
      medium: '500',
      bold: 'bold',
    }
    styles.fontWeight = weightMap[props.weight]
  }
  
  return styles
})

// 计算class
const textClass = computed(() => {
  const classes: string[] = []
  
  // 单行省略
  if (props.truncated) {
    classes.push('truncate')
  }
  
  // 多行省略
  if (props.lineClamp) {
    classes.push(`line-clamp-${props.lineClamp}`)
  }
  
  // 自定义class
  if (props.textClass) {
    classes.push(props.textClass)
  }
  
  return classes.join(' ')
})
</script>

<template>
  <el-text
    :type="elTextType"
    :size="elTextSize"
    :class="textClass"
    :style="textStyle"
    :title="title"
  >
    <slot />
  </el-text>
</template>

<style scoped>
/* Tailwind CSS 样式支持 */
.truncate {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.line-clamp-1 {
  overflow: hidden;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
}

.line-clamp-2 {
  overflow: hidden;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
}

.line-clamp-3 {
  overflow: hidden;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 3;
}

.line-clamp-4 {
  overflow: hidden;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 4;
}

.line-clamp-5 {
  overflow: hidden;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 5;
}

.line-clamp-6 {
  overflow: hidden;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 6;
}
</style>