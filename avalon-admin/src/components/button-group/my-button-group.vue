<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {computed, provide, useSlots, defineProps} from 'vue'
import type {ButtonGroupProps} from './types'

const props = defineProps<ButtonGroupProps>()
const slots = useSlots()

provide('ButtonGroupProps', props)

const groupClass = computed(() => [
  'inline-flex align-middle overflow-hidden',
  'my-button-group',
])
</script>

<template>
  <div :class="groupClass">
    <slot/>
  </div>
</template>

<style scoped>
.my-button-group > :not(:first-child) {
  position: relative;
}

.my-button-group > :not(:first-child)::before {
  content: "";
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 1px;
  background: var(--border-extra-light, #dcdfe6); /* 默认分割线色 */
  z-index: 1;
}
</style>