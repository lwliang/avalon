<script setup lang="ts">
import {computed} from 'vue'
import type {SwitchProps} from './types'
import FormField from "../../model/FormField.ts";
import myIcon from '../icon/my-icon.vue';


// props & v-model
const props = withDefaults(defineProps<SwitchProps>(), {
  disabled: false,
  loading: false,
  activeValue: true,
  inactiveValue: false,
  require: false,
})
const emit = defineEmits<{
  (e: 'change', value: any): void
}>()
const formField = defineModel<FormField>({required: true})

// 状态判定
const isChecked = computed(() =>
    formField.value && formField.value.value === props.activeValue
)

const switchWidth = computed(() => {
  if (props.width) return typeof props.width === 'number' ? `${props.width}px` : props.width
  return ({
    large: '50px',
    default: '40px',
    small: '30px'
  })[props.size ?? 'default']
})
const switchHeight = computed(() => (
    props.size === 'large' ? '24px' : props.size === 'small' ? '16px' : '20px'
))

const stateClass = computed(() => {
  if (props.disabled) return 'bg-background-disabled border-border-disabled cursor-not-allowed'
  if (getIconProps.value) return 'bg-background-base border-border'
  if (isChecked.value) return 'bg-primary border-primary'
  return 'bg-background-base border-border'
})

const thumbClass = computed(() =>
    [
      'absolute rounded-full bg-white shadow transition-all duration-200',
      isChecked.value ? 'right-1' : 'left-1',
      props.size === 'large' ? 'w-6 h-6' : props.size === 'small' ? 'w-3 h-3' : 'w-4 h-4'
    ].join(' ')
)

// 处理 icon 统一格式（兼容 string/[type,icon] 以及 type 优先级）
const getActiveIconProps = computed(() => {
  if (!props.activeIcon) return null
  if (Array.isArray(props.activeIcon)) {
    return {icon: props.activeIcon, size: getIconSize(), color: "#fff"} satisfies Record<string, any>;
  }
  return {
    icon: props.activeIcon,
    type: props.activeIconType ?? 'fas',
    size: getIconSize(),
    color: "#fff"
  } satisfies Record<string, any>;
})

const getInactiveIconProps = computed(() => {
  if (!props.inactiveIcon) return null
  if (Array.isArray(props.inactiveIcon)) {
    return {
      icon: props.inactiveIcon,
      size: getIconSize(),
      color: "#fff",
      type: undefined
    } satisfies Record<string, any>;
  }
  return {
    icon: props.inactiveIcon,
    type: props.inactiveIconType ?? 'fas',
    size: getIconSize(),
    color: "#fff"
  } satisfies Record<string, any>;
})

const getIconProps = computed(() => {
  if (props.loading) {
    return {
      icon: 'spinner',
      type: 'fas',
      spin: true,
      size: getIconSize(),
      color: '#FFF'
    } as const // 直接断言
  }
  if (isChecked.value) return getActiveIconProps.value as any
  return getInactiveIconProps.value as any
})


function getIconSize() {
  if (props.size === 'large') return 'lg'
  if (props.size === 'small') return 'sm'
  return '1x'
}

// 切换
const handleToggle = () => {
  if (props.disabled || props.loading) return
  const newVal = isChecked.value ? props.inactiveValue : props.activeValue
  if (formField.value) formField.value.value = newVal
  emit('change', newVal)
}

// 校验
const setValidate = (valid: boolean) => {
  if (formField.value) formField.value.isValidate = valid
}
const validate = () => {
  if (props.require && (!formField.value || !isChecked.value)) {
    setValidate(false)
    return false
  }
  setValidate(true)
  return true
}
defineExpose({validate})
</script>

<template>
  <div
      class="inline-flex items-center select-none relative transition-all"
      :class="[
      'switch-root',
      stateClass,
      props.disabled ? 'opacity-60' : 'cursor-pointer'
    ]"
      :style="{
      width: switchWidth,
      height: switchHeight,
    }"
      @click="handleToggle"
  >
    <!-- 动态 icon -->
    <span v-if="getIconProps" class="absolute z-10 flex items-center justify-center switch-thumb "
          :class="[isChecked ? 'left-auto right-1' : 'left-1 right-auto']">
          <my-icon v-if="getIconProps" v-bind="getIconProps as any"/>
        </span>
    <!-- 滑块按钮 -->
    <span v-else class="switch-thumb" :class="thumbClass"></span>
    <!-- 激活/未激活文本 -->
    <span v-if="props.activeText || props.inactiveText" :class="['absolute mx-2 text-xs transition text-white',
    !isChecked ? 'right-1' : 'left-1']">
      <template v-if="isChecked">
        {{ props.activeText }}
      </template>
      <template v-else>
        {{ props.inactiveText }}
      </template>
    </span>
  </div>
</template>

<style scoped>
.switch-root {
  border-radius: 9999px;
  box-sizing: border-box;
  user-select: none;
  position: relative;
  transition: background-color 0.2s, border-color 0.2s;
}

.switch-thumb {
  top: 50%;
  transform: translateY(-50%);
}
</style>
