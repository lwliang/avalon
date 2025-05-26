<script setup lang="ts">
import {ref, computed, watch} from 'vue'
import type {IconType} from '../icon/types'
import type {RateProps} from './types'
import FormField from "../../model/FormField"
import myIcon from '../icon/my-icon.vue'

// props 定义
const props = withDefaults(defineProps<RateProps>(), {
  max: 5,
  size: 'default',
  lowThreshold: 2,
  highThreshold: 4,
  colors: () => ['text-warning', 'text-warning', 'text-warning'],
  voidColor: 'text-border',
  disabledVoidColor: 'text-background-disabled',
  icon: 'star',
  iconType: 'fas',
  voidIcon: 'star',
  voidIconType: 'far',
  disabled: false,
  required: false
})

// emits
const emit = defineEmits<{
  (e: 'change', value: number): void
}>()
// v-model
const formField = defineModel<FormField>({required: true})
const currentValue = ref(formField.value?.value ?? 0)

const statueClass = (idx: number) => {
  if (props.disabled && props.disabledVoidColor)
    return idx < currentValue.value ? props.colors[0] : props.disabledVoidColor
  if (idx < currentValue.value) {
    if (currentValue.value <= props.lowThreshold) return props.colors[0]
    if (currentValue.value <= props.highThreshold) return props.colors[1]
    return props.colors[2]
  }
  return props.voidColor
}
const getIconProps = (idx: number) => {
  if (idx < currentValue.value) {
    return {icon: props.icon, type: props.iconType, size: iconSize.value}
  }
  return {icon: props.voidIcon, type: props.voidIconType, size: iconSize.value}
}
const iconSize = computed(() =>
    props.size === 'large' ? '2rem' : props.size === 'small' ? '1rem' : '1.5rem'
)
const isReadonly = computed(() => props.disabled)

// 鼠标事件
function handleEnter(idx: number) {
  if (isReadonly.value) return
  currentValue.value = idx + 1
}

function handleLeave() {
  // 不区分在哪一颗星，统一重置
  if (isReadonly.value) return
  currentValue.value = formField.value?.value ?? 0
}

function handleClick(idx: number) {
  if (isReadonly.value) return
  if (formField.value) formField.value.value = idx + 1
  currentValue.value = idx + 1
  emit('change', idx + 1)
}

// 校验逻辑
const setValidate = (valid: boolean) => {
  if (formField.value) {
    formField.value.isValidate = valid
  }
}
const validate = () => {
  if (props.required) {
    if (formField.value && !formField.value.value) {
      formField.value.isValidate = false
      return false
    }
  }
  setValidate(!!formField.value?.value)
  return formField.value?.isValidate
}
defineExpose({validate})

watch(
    () => formField.value && formField.value.value,
    (val) => {
      currentValue.value = val ?? 0
      validate()
    }
)
</script>

<template>
  <span class="flex items-center gap-1 select-none cursor-pointer"
        @mouseleave="handleLeave">
    <template v-for="idx in props.max" :key="idx">
      <span
          :class="[
          'cursor-pointer',
          isReadonly ? 'pointer-events-none opacity-60' : '',
          statueClass(idx - 1)
        ]"
          @mouseenter="handleEnter(idx - 1)"
          @mousemove="handleEnter(idx - 1)"
          @click="handleClick(idx - 1)"
          style="transition: color 0.2s"
      >
        <my-icon v-bind="getIconProps(idx-1) as any"/>
      </span>
    </template>
  </span>
</template>
