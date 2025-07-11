<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import { computed } from 'vue'

interface SheetProps {
  padding?: number | string // 内边距，支持数字(px)或字符串
  height?: string // 高度
  width?: string // 宽度
  background?: string // 背景色
  border?: boolean // 是否显示边框
  shadow?: boolean // 是否显示阴影
  radius?: number | string // 圆角，支持数字(px)或字符串
  overflow?: 'visible' | 'hidden' | 'auto' | 'scroll' // 溢出处理
}

const props = withDefaults(defineProps<SheetProps>(), {
  padding: 16,
  height: '100%',
  width: '100%',
  background: 'transparent',
  border: false,
  shadow: false,
  radius: 0,
  overflow: 'visible'
})

// 计算样式
const sheetStyle = computed(() => {
  const paddingValue = typeof props.padding === 'number' ? `${props.padding}px` : props.padding
  const radiusValue = typeof props.radius === 'number' ? `${props.radius}px` : props.radius
  
  return {
    padding: paddingValue,
    height: props.height,
    width: props.width,
    backgroundColor: props.background,
    borderRadius: radiusValue,
    overflow: props.overflow,
    position: 'relative' as const
  }
})

// 计算类名
const sheetClass = computed(() => [
  'sheet-container',
  {
    'sheet-border': props.border,
    'sheet-shadow': props.shadow
  }
])
</script>

<template>
  <div 
    :class="sheetClass"
    :style="sheetStyle"
  >
    <slot />
  </div>
</template>

<style scoped>
.sheet-container {
  box-sizing: border-box;
  transition: all 0.3s ease;
}

.sheet-border {
  border: 1px solid var(--el-border-color, #dcdfe6);
}

.sheet-shadow {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .sheet-container {
    padding: 12px !important;
  }
}

@media (max-width: 480px) {
  .sheet-container {
    padding: 8px !important;
  }
}
</style>