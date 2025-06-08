<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {computed, provide} from 'vue'

const props = defineProps<{
  gutter?: number, // 间距，单位 px
  justify?: 'start' | 'center' | 'end' | 'space-between' | 'space-around' | 'space-evenly',
  align?: 'top' | 'middle' | 'bottom' | 'stretch'
}>()

const style = computed(() => ({
  marginLeft: props.gutter ? `-${props.gutter / 2}px` : undefined,
  marginRight: props.gutter ? `-${props.gutter / 2}px` : undefined,
}))

const justifyClassMap = {
  start: 'justify-start',
  center: 'justify-center',
  end: 'justify-end',
  'space-between': 'justify-between',
  'space-around': 'justify-around',
  'space-evenly': 'justify-evenly',
}
const alignClassMap = {
  top: 'items-start',
  middle: 'items-center',
  bottom: 'items-end',
  stretch: 'items-stretch',
}

provide('gutter', props.gutter || 0)
</script>

<template>
  <div
      class="flex flex-wrap w-full gap-4"
      :class="[justifyClassMap[props.justify || 'start'], alignClassMap[props.align || 'top']]"
      :style="style"
  >
    <slot/>
  </div>
</template>