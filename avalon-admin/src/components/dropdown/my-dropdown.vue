<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {computed} from 'vue'

const props = withDefaults(defineProps<{
  trigger?: 'click' | 'hover' | 'contextmenu'
  placement?: 'top' | 'top-start' | 'top-end' | 'bottom' | 'bottom-start' | 'bottom-end'
  teleported?: boolean
  disabled?: boolean
  size?: 'large' | 'default' | 'small'
  type?: 'primary' | 'success' | 'warning' | 'danger' | 'info'
  splitButton?: boolean
  hideOnClick?: boolean
  showTimeout?: number
  hideTimeout?: number
  tabindex?: number
  maxHeight?: string | number
  popperClass?: string
  popperStyle?: object
  role?: string
  ariaLabel?: string
  ariaDescribedby?: string
  ariaLabelledby?: string
  ariaModal?: boolean
  zIndex?: number
}>(), {
  trigger: 'hover',
  placement: 'bottom',
  teleported: true,
  disabled: false,
  size: 'default',
  type: undefined,
  splitButton: false,
  hideOnClick: true,
  showTimeout: 0,
  hideTimeout: 0,
  tabindex: 0,
  maxHeight: '',
  ariaModal: false
})

const emit = defineEmits<{
  (e: 'command', command: string | number | object): void
  (e: 'click', event: MouseEvent): void
  (e: 'visible-change', visible: boolean): void
}>()

// 将自定义属性映射到 el-dropdown 属性
const dropdownTrigger = computed(() => {
  const triggerMap: Record<string, string> = {
    'hover': 'hover',
    'click': 'click',
    'contextmenu': 'contextmenu'
  }
  return triggerMap[props.trigger] || 'hover'
})

const dropdownPlacement = computed(() => {
  const placementMap: Record<string, string> = {
    'top': 'top',
    'top-start': 'top-start',
    'top-end': 'top-end',
    'bottom': 'bottom',
    'bottom-start': 'bottom-start',
    'bottom-end': 'bottom-end'
  }
  return placementMap[props.placement] || 'bottom'
})
</script>

<template>
  <el-dropdown
    :trigger="dropdownTrigger"
    :placement="dropdownPlacement"
    :teleported="teleported"
    :disabled="disabled"
    :size="size"
    :type="type"
    :split-button="splitButton"
    :hide-on-click="hideOnClick"
    :show-timeout="showTimeout"
    :hide-timeout="hideTimeout"
    :tabindex="tabindex"
    :max-height="maxHeight"
    :popper-class="popperClass"
    :popper-style="popperStyle"
    :aria-label="ariaLabel"
    :aria-describedby="ariaDescribedby"
    :aria-labelledby="ariaLabelledby"
    :aria-modal="ariaModal"
    :z-index="zIndex"
    @command="(command: string | number | object) => emit('command', command)"
    @click="(event: MouseEvent) => emit('click', event)"
    @visible-change="(visible: boolean) => emit('visible-change', visible)">
    <template #default>
      <slot name="default"></slot>
    </template>
    <template #dropdown>
      <slot name="dropdown"></slot>
    </template>
  </el-dropdown>
</template>

<style scoped>

</style>