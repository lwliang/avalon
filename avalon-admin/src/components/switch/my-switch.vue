<script setup lang="ts">
import { computed, ref } from 'vue'
import type { SwitchProps } from './types'

interface ExtendedSwitchProps extends SwitchProps {
  validateEvent?: boolean
  inlinePrompt?: boolean
  beforeChange?: (value: boolean) => boolean | Promise<boolean>
}

const props = withDefaults(defineProps<ExtendedSwitchProps>(), {
  disabled: false,
  loading: false,
  activeValue: true,
  inactiveValue: false,
  require: false,
  validateEvent: true,
  inlinePrompt: false
})

const emit = defineEmits<{
  (e: 'change', value: any): void
  (e: 'update:modelValue', value: any): void
}>()

const modelValue = defineModel<any>({
  default: false
})

// 验证状态
const isValid = ref(true)

// 计算当前值是否为激活状态
const isChecked = computed(() => modelValue.value === props.activeValue)

// 处理切换事件
const handleChange = (value: any) => {
  modelValue.value = value
  emit('change', value)
  validate()
}

// 验证函数
const validate = () => {
  if (props.require && !isChecked.value) {
    isValid.value = false
    return false
  }
  isValid.value = true
  return true
}

// 计算 el-switch 的属性
const switchProps = computed(() => ({
  modelValue: isChecked.value,
  disabled: props.disabled,
  loading: props.loading,
  width: props.width,
  activeIcon: props.activeIcon,
  inactiveIcon: props.inactiveIcon,
  size: props.size,
  activeText: props.activeText,
  inactiveText: props.inactiveText,
  activeValue: props.activeValue,
  inactiveValue: props.inactiveValue,
  name: props.name,
  validateEvent: props.validateEvent,
  inlinePrompt: props.inlinePrompt,
  beforeChange: props.beforeChange
}))

defineExpose({ validate })
</script>

<template>
  <el-switch
    v-bind="switchProps"
    @change="handleChange"
    @update:modelValue="(value: any) => emit('update:modelValue', value)"
  />
</template>
