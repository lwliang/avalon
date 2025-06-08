<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import { computed, defineProps } from 'vue'
import type { ProgressProps, ProgressType, ProgressStatus } from './types'

const props = defineProps<ProgressProps>()

const type = computed(() => props.type ?? 'line')
const status = computed(() => props.status ?? 'primary')
const strokeWidth = computed(() => props.strokeWidth ?? 6)
const circleWidth = computed(() => props.width ?? 126)

// 支持 FormField 或 number
const rawPercentage = computed(() => {
  const p = props.percentage
  if (typeof p === 'number') return p
  if (typeof p === 'object' && p !== null && 'value' in p) return p.value
  return 0
})
const percentage = computed(() => {
  const val = Number(rawPercentage.value)
  return Math.max(0, Math.min(100, isNaN(val) ? 0 : val))
})

const getLineBarClass = (status: ProgressStatus) => {
  switch (status) {
    case 'success': return 'bg-success'
    case 'warning': return 'bg-warning'
    case 'danger': return 'bg-danger'
    case 'info': return 'bg-info'
    case 'primary':
    default: return 'bg-primary'
  }
}
const getCircleBarClass = (status: ProgressStatus) => {
  switch (status) {
    case 'success': return 'stroke-success'
    case 'warning': return 'stroke-warning'
    case 'danger': return 'stroke-danger'
    case 'info': return 'stroke-info'
    case 'primary':
    default: return 'stroke-primary'
  }
}

function isPureColor(val?: string) {
  if (!val) return false
  return /^#|^rgb|^hsl|^[a-zA-Z]+$/.test(val) && !val.startsWith('bg-') && !val.startsWith('stroke-')
}

const lineBarClass = computed(() =>
    isPureColor(props.color) ? '' : (props.color || getLineBarClass(status.value))
)
const lineBarStyle = computed(() =>
    isPureColor(props.color)
        ? { background: props.color, width: percentage.value + '%' }
        : { width: percentage.value + '%' }
)

const circleBarClass = computed(() =>
    isPureColor(props.color) ? '' : (props.color ? props.color.replace('bg-', 'stroke-') : getCircleBarClass(status.value))
)
const circleBarColor = computed(() =>
    isPureColor(props.color) ? props.color : undefined
)
const circleDasharray = 2 * Math.PI * 45
const circleDashoffset = computed(() =>
    circleDasharray * (1 - percentage.value / 100)
)
</script>

<template>
  <div>
    <!-- 线型进度条 -->
    <div   v-if="type === 'line'" class="flex items-center gap-2">
      <div

          class="flex-1 relative rounded-full bg-background-disabled h-2 overflow-hidden"
      >
        <div
            class="absolute left-0 top-0 h-full rounded-full transition-all duration-300"
            :class="lineBarClass"
            :style="lineBarStyle"
        ></div>
        <span
            v-if="textInside"
            class="absolute left-1/2 top-1/2 -translate-x-1/2 -translate-y-1/2 text-default text-text-primary select-none"
        >
        {{ percentage }}%
      </span>
      </div>
      <div
          v-if="type === 'line' && !textInside"
          class="text-right text-default text-text-secondary select-none"
      >
        {{ percentage }}%
      </div>
    </div>


    <!-- 圆形进度条 -->
    <svg
        v-if="type === 'circle'"
        :width="circleWidth"
        :height="circleWidth"
        viewBox="0 0 100 100"
        class="block mx-auto"
    >
      <!-- 背景圆环 -->
      <circle
          cx="50"
          cy="50"
          r="45"
          class="fill-none"
          stroke="#f5f7fa"
          :stroke-width="strokeWidth"
      />
      <!-- 进度圆环 -->
      <circle
          cx="50"
          cy="50"
          r="45"
          class="fill-none transition-all duration-300"
          :class="circleBarClass"
          :stroke="circleBarColor"
          :stroke-width="strokeWidth"
          stroke-linecap="round"
          :stroke-dasharray="circleDasharray"
          :stroke-dashoffset="circleDashoffset"
      />
      <!-- 百分比文字 -->
      <text
          x="50"
          y="55"
          text-anchor="middle"
          font-size="20"
          class="fill-text-primary select-none"
      >
        {{ percentage }}%
      </text>
    </svg>
  </div>
</template>

<style scoped>
</style>
