<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {computed, defineProps, defineEmits} from 'vue'
import type {LabelProps} from './types'

const props = defineProps<LabelProps>()

const typeClassMap = {
  default: 'text-text-regular',
  primary: 'text-primary',
  success: 'text-success',
  warning: 'text-warning',
  danger: 'text-danger',
  info: 'text-info',
}

const sizeClassMap = {
  large: 'px-3 py-0.5 text-lg',
  default: 'px-2.5 py-0.5 text-base',
  small: 'px-2 py-0.5 text-sm'
}

const labelClass = computed(() => [
  'inline-flex',
  'items-center',
  'gap-1',
  typeClassMap[props.type ?? 'default'],
  sizeClassMap[props.size ?? 'default'],
])
</script>

<template>
  <label :class="labelClass">
    <slot name="icon">
      <my-icon
          v-if="props.icon"
          :type="props.iconType"
          class="mr-1"
          aria-hidden="true"
          :icon="props.icon"
      />
    </slot>
    <slot/>
  </label>
</template>