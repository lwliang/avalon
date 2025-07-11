<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import { computed } from 'vue'
import type { ProgressProps, ProgressType, ProgressStatus } from './types'

const props = defineProps<ProgressProps>()

// Element Plus status 映射
const elStatus = computed(() => {
  const statusMap = {
    primary: 'primary',
    success: 'success',
    warning: 'warning',
    danger: 'exception',
    info: 'info'
  }
  return statusMap[props.status || 'primary']
})

// Element Plus type 映射
const elType = computed(() => {
  const typeMap = {
    line: 'line',
    circle: 'circle'
  }
  return typeMap[props.type || 'line']
})
</script>

<template>
  <el-progress
    :percentage="percentage"
    :type="elType"
    :status="elStatus"
    :color="color"
    :text-inside="textInside"
    :stroke-width="strokeWidth"
    :width="width"
  />
</template>

<style scoped>
/* 可以根据需要添加自定义样式 */
</style>
