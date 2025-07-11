<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import { ref, computed, watch, useAttrs } from 'vue'
import type { InputProps } from './types'

const props = defineProps<InputProps>()
const inputValue = defineModel<any>({
  default: ''
})
const attrs = useAttrs()

const emit = defineEmits<{
  (e: 'clear'): void
  (e: 'change', value: any): void
  (e: 'blur', event: FocusEvent): void
  (e: 'focus', event: FocusEvent): void
  (e: 'suffixIconClick'): void
  (e: 'keyup', event: KeyboardEvent): void
}>()

// 验证状态
const isValidate = ref(true)

// Element Plus size 映射
const elSize = computed(() => {
  const sizeMap = {
    large: 'large',
    default: 'default',
    small: 'small'
  }
  return sizeMap[props.size || 'default']
})

// 前缀图标处理
const prefixIcon = computed(() => {
  if (!props.prefixIcon) return undefined
  // 这里需要根据项目的图标系统来处理
  // 如果使用 Element Plus 图标，需要导入对应的图标组件
  return undefined // 暂时返回 undefined，需要根据具体图标系统调整
})

// 后缀图标处理
const suffixIcon = computed(() => {
  if (!props.suffixIcon) return undefined
  // 这里需要根据项目的图标系统来处理
  return undefined // 暂时返回 undefined，需要根据具体图标系统调整
})

/** 清空按钮点击 */
function onClear() {
  inputValue.value = ''
  isValidate.value = true
  emit('clear')
}

/** 输入变化事件 */
function onInput(value: any) {
  inputValue.value = value
  emit('change', value)
}

/** 失焦事件 */
function onBlur(e: FocusEvent) {
  validate()
  emit('blur', e)
}

/** 聚焦事件 */
function onFocus(e: FocusEvent) {
  emit('focus', e)
}

/** 键盘事件 */
function onKeyup(e: KeyboardEvent) {
  emit('keyup', e)
}

/** 校验逻辑 */
function validate(): boolean {
  if (props.required && !inputValue.value?.trim()) {
    isValidate.value = false
    return false
  }
  if (props.pattern && inputValue.value) {
    const regex = new RegExp(props.pattern)
    if (!regex.test(inputValue.value)) {
      isValidate.value = false
      return false
    }
  }
  isValidate.value = true
  return true
}

/** 自动恢复校验 */
watch(inputValue, () => {
  if (!isValidate.value) {
    isValidate.value = true
  }
})

/** 后缀图标点击 */
function onSuffixIconClick() {
  emit('suffixIconClick')
}

// 验证状态样式
const validateStatus = computed(() => {
  if (props.required && !isValidate.value) {
    return 'error'
  }
  return ''
})

defineExpose({ validate, isValidate })
</script>

<template>
  <el-input
      v-model="inputValue"
      :type="type || 'text'"
      :size="elSize"
      :placeholder="placeholder"
      :disabled="disabled"
      :readonly="readonly"
      :clearable="clearable"
      :prefix-icon="prefixIcon"
      :suffix-icon="suffixIcon"
      :validate-event="false"
      @input="onInput"
      @clear="onClear"
      @blur="onBlur"
      @focus="onFocus"
      @keyup="onKeyup"
      v-bind="attrs"
  >
    <!-- 前缀插槽 -->
    <template v-if="$slots.prefix" #prefix>
      <slot name="prefix" />
    </template>
    
    <!-- 后缀插槽 -->
    <template v-if="$slots.suffix" #suffix>
      <span @click="onSuffixIconClick" :class="{ 'cursor-pointer': type === 'password' }">
        <slot name="suffix" />
      </span>
    </template>
    
    <!-- append 插槽 -->
    <template v-if="$slots.append" #append>
      <slot name="append" />
    </template>
    
    <!-- prepend 插槽 -->
    <template v-if="$slots.prepend" #prepend>
      <slot name="prepend" />
    </template>
  </el-input>
</template>

<style scoped>
/* 可以根据需要添加样式 */
</style>