<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {computed, useAttrs, useSlots, defineProps, defineEmits, inject} from 'vue'
import MyIcon from '../icon/my-icon.vue'
import type {ButtonProps, ButtonType, ButtonSize, ButtonActionType} from './types'
import type {ButtonGroupProps} from '../button-group/types'

const groupProps = inject<ButtonGroupProps | null>('ButtonGroupProps', null)

// props、emits、attrs、slots
const props = defineProps<ButtonProps>()


const type = computed(() => props.type !== undefined ? props.type : groupProps?.type)
const size = computed(() => props.size !== undefined ? props.size : groupProps?.size)

const plain = computed(() => props.plain)
const round = computed(() => props.round)
const disabled = computed(() => props.disabled)

const emit = defineEmits<{
  (e: 'click', payload: { action?: string; actionType?: ButtonActionType; event: MouseEvent }): void
}>()
const attrs = useAttrs()
const slots = useSlots()

function handleClick(event: MouseEvent) {
  if (disabled.value || props.loading) {
    event.preventDefault()
    return
  }
  emit('click', {
    action: props.action,
    actionType: props.actionType,
    event
  })
}

// 颜色映射
const solidTypeClassMap: Record<ButtonType, string> = {
  primary: 'bg-primary text-white  hover:bg-primary-hover active:bg-primary-active',
  success: 'bg-success text-white  hover:bg-success-hover active:bg-success-active',
  warning: 'bg-warning text-white  hover:bg-warning-hover active:bg-warning-active',
  danger: 'bg-danger text-white  hover:bg-danger-hover active:bg-danger-active',
  info: 'bg-info text-white  hover:bg-info-hover active:bg-info-active',
  default: 'bg-background text-text-regular border border-solid border-border-base hover:text-primary hover:bg-primary-light-9 hover:border-primary-light-8 hover:bg-background-overlay active:bg-background-disabled',
}

const plainTypeClassMap: Record<ButtonType, string> = {
  primary: 'bg-background text-primary border border-solid border-primary bg-primary-light-9  hover:bg-primary hover:text-white active:bg-primary-active active:text-white',
  success: 'bg-background text-success border border-solid border-success bg-success-light-9 hover:bg-success hover:text-white active:bg-success-active active:text-white',
  warning: 'bg-background text-warning border border-solid border-warning bg-warning-light-9 hover:bg-warning hover:text-white active:bg-warning-active active:text-white',
  danger: 'bg-background text-danger border border-solid border-danger bg-danger-light-9 hover:bg-danger hover:text-white active:bg-danger-active active:text-white',
  info: 'bg-background text-info border border-solid border-info bg-info-light-9 hover:bg-info hover:text-white active:bg-info-active active:text-white',
  default: 'bg-background text-text-regular border bg-default-light-9 border-solid border-border-base hover:border-primary hover:text-primary hover:bg-background-overlay active:bg-background-disabled',
}

const sizeClassMap: Record<ButtonSize, string> = {
  large: 'h-[40px] text-lg',
  default: 'h-[32px] text-base',
  small: 'h-[24px] text-sm',
}

const paddingClassMap: Record<ButtonSize, string> = {
  large: 'px-7',
  default: 'px-5',
  small: 'px-3',
}

const circleClassMap: Record<ButtonSize, string> = {
  large: 'w-11 rounded-circle justify-center items-center',
  default: 'w-9 rounded-circle justify-center items-center',
  small: 'w-7 rounded-circle justify-center items-center',
}

// 判断是否 icon-only
const isIconOnly = computed(() =>
    !!props.icon && !props.iconRight && !props.loading && !slots.default
)

// 图标 size
const iconSize = computed(() => {
  if (size.value === 'large') return isIconOnly.value ? 'lg' : '1x'
  if (size.value === 'small') return isIconOnly.value ? '1x' : 'sm'
  return isIconOnly.value ? '1x' : '1x'
})

// 组合最终 class
const buttonClass = computed(() => {
  const typeValue = type.value ?? 'default'
  const sizeValue = size.value ?? 'small'
  let paddingClass = props.circle ? '' : paddingClassMap[sizeValue];
  let colorClass = plain.value
      ? plainTypeClassMap[typeValue]
      : solidTypeClassMap[typeValue]


  // group下中间按钮不加圆角
  let shapeClass = props.circle
      ? circleClassMap[sizeValue]
      : round.value
          ? 'rounded-full'
          : groupProps
              ? 'rounded-none'
              : 'rounded'

  let disabledClass = (props.disabled || props.loading)
      ? 'opacity-disabled cursor-not-allowed pointer-events-none'
      : 'hover:shadow'

  return [
    'border-box leading-none inline-flex items-center justify-center font-medium transition duration-150 outline-none select-none relative align-middle',
    colorClass,
    paddingClass,
    shapeClass,
    disabledClass,
    sizeClassMap[sizeValue],
  ].filter(Boolean).join(' ')
})

const nativeType = computed(() => props.nativeType ?? 'button')
const isDisabled = computed(() => (props.disabled || props.loading))
</script>

<template>
  <button
      :type="nativeType"
      :disabled="isDisabled"
      :class="buttonClass"
      v-bind="attrs"
      @click="handleClick"
  >
    <!-- loading icon -->
    <MyIcon
        v-if="loading"
        :icon="['fas', 'spinner']"
        spin
        :size="iconSize"
        :class="isIconOnly ? '' : 'mr-2'"
    />
    <!-- 左icon -->
    <MyIcon
        v-else-if="icon"
        :icon="icon"
        :type="iconType"
        :size="iconSize"
        :class="isIconOnly ? '' : 'mr-2'"
    />
    <!-- 内容 -->
    <span class="inline-flex items-center " v-if="slots.default">
      <slot/>
    </span>
    <!-- 右icon -->
    <MyIcon
        v-if="iconRight"
        :icon="iconRight"
        :type="iconRightType"
        :size="iconSize"
        class="ml-2"
    />
  </button>
</template>

<style scoped>


</style>