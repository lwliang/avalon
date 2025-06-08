<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {inject, computed} from 'vue'

type ColSize = number | undefined

const props = defineProps<{
  span?: number
  offset?: number
  xs?: ColSize
  sm?: ColSize
  md?: ColSize
  lg?: ColSize
  xl?: ColSize
}>()

const gutter = inject('gutter', 0) as number

// 计算基础 span 类
function spanClass(prefix: string, span?: number) {
  return span ? `${prefix}${span}/12` : ''
}

function offsetClass(prefix: string, offset?: number) {
  return offset ? `ml-${offset}/12` : ''
}

// 响应式 class
const responsiveClass = computed(() => {
  const classes: string[] = []
  if (props.xs) classes.push(`w-full sm:w-${props.xs}/12`)
  if (props.sm) classes.push(`sm:w-${props.sm}/12`)
  if (props.md) classes.push(`md:w-${props.md}/12`)
  if (props.lg) classes.push(`lg:w-${props.lg}/12`)
  if (props.xl) classes.push(`xl:w-${props.xl}/12`)
  return classes
})

const baseClass = computed(() => [
  props.span ? `w-${props.span}/12` : 'flex-1',
  props.offset ? `ml-${props.offset}/12` : '',
])

const style = computed(() => ({
  paddingLeft: gutter ? `${gutter / 2}px` : undefined,
  paddingRight: gutter ? `${gutter / 2}px` : undefined,
}))
</script>

<template>
  <div
      :class="['min-h-[1px]', baseClass, responsiveClass]"
      :style="style"
  >
    <slot/>
  </div>
</template>