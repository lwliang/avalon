<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {computed, useAttrs, inject} from 'vue'
import type {ButtonProps, ButtonType, ButtonSize, ButtonActionType} from './types'
import {ElButton} from 'element-plus'
// props、emits、attrs
const props = defineProps<ButtonProps>()

const emit = defineEmits<{
  (e: 'click', payload: { action?: string; actionType?: ButtonActionType; event: MouseEvent }): void
}>()
const attrs = useAttrs()

// 获取按钮组属性
const buttonGroupProps = inject('buttonGroupProps', { size: undefined, type: undefined })

function handleClick(event: MouseEvent) {
  if (props.disabled || props.loading) {
    event.preventDefault()
    return
  }
  emit('click', {
    action: props.action,
    actionType: props.actionType,
    event
  })
}

// 将自定义属性映射到 el-button 属性，优先使用按钮自身的属性，如果没有则使用按钮组的属性
const buttonType = computed((): 'primary' | 'success' | 'warning' | 'danger' | 'info' | 'default' | 'text' => {
  const typeValue = props.type || buttonGroupProps.type || 'default'
  const typeMap: Record<ButtonType, 'primary' | 'success' | 'warning' | 'danger' | 'info' | 'default' | 'text'> = {
    primary: 'primary',
    success: 'success',
    warning: 'warning', 
    danger: 'danger',
    info: 'info',
    default: 'default'
  }
  return typeMap[typeValue] || 'default'
})

const buttonSize = computed((): 'large' | 'default' | 'small' => {
  const sizeValue = props.size || buttonGroupProps.size || 'default'
  const sizeMap: Record<ButtonSize, 'large' | 'default' | 'small'> = {
    large: 'large',
    default: 'default',
    small: 'small'
  }
  return sizeMap[sizeValue] || 'default'
})

const buttonPlain = computed(() => props.plain)
const buttonRound = computed(() => props.round)
const buttonCircle = computed(() => props.circle)
const buttonDisabled = computed(() => props.disabled || props.loading)
const buttonLoading = computed(() => props.loading)
const buttonNativeType = computed(() => props.nativeType || 'button')

// 处理图标
const buttonIcon = computed(() => {
  if (props.loading) return 'Loading'
  if (props.icon) {
    if (Array.isArray(props.icon)) {
      return props.icon[1] // 取图标名称
    }
    return props.icon
  }
  return undefined
})

const buttonIconRight = computed(() => {
  if (props.iconRight) {
    if (Array.isArray(props.iconRight)) {
      return props.iconRight[1] // 取图标名称
    }
    return props.iconRight
  }
  return undefined
})
</script>

<template>
  <el-button
    :type="buttonType"
    :size="buttonSize"
    :plain="buttonPlain"
    :round="buttonRound"
    :circle="buttonCircle"
    :disabled="buttonDisabled"
    :loading="buttonLoading"
    :native-type="buttonNativeType"
    :icon="buttonIcon"
    v-bind="attrs"
    @click="handleClick">
    <slot/>
  </el-button>
</template>

<style scoped>


</style>