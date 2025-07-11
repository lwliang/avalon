<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/15
 */

import { ref, computed, watch, useAttrs } from 'vue'
import type { NumberInputProps } from './types'

const props = withDefaults(defineProps<NumberInputProps>(), {
  size: 'default',
  disabled: false,
  readonly: false,
  clearable: true,
  required: false,
  placeholder: '请输入数字',
  step: 1,
  stepStrictly: false,
  controls: false,
  controlsPosition: 'right'
})

const numberValue = defineModel<number | undefined>({
  default: undefined
})
const attrs = useAttrs()

const emit = defineEmits<{
  (e: 'clear'): void
  (e: 'change', value: number | undefined): void
  (e: 'blur', event: FocusEvent): void
  (e: 'focus', event: FocusEvent): void
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
  return sizeMap[props.size]
})

// 内部字符串值，用于 el-input 显示
const inputValue = ref<string>('')

// 初始化输入值
function initInputValue() {
  if (numberValue.value === undefined || numberValue.value === null) {
    inputValue.value = ''
  } else {
    inputValue.value = formatDisplayValue(numberValue.value)
  }
}

// 监听数字值变化，更新输入显示
watch(() => numberValue.value, () => {
  initInputValue()
}, { immediate: true })

// 监听输入值变化，更新数字值
watch(inputValue, (newVal) => {
  if (newVal === '' || newVal === null || newVal === undefined) {
    numberValue.value = undefined
  } else {
    const num = parseFloat(newVal)
    if (!isNaN(num)) {
      // 应用步长限制
      if (props.stepStrictly && props.step) {
        const steps = Math.round(num / props.step)
        numberValue.value = steps * props.step
      } else {
        numberValue.value = num
      }
    }
  }
})

/** 清空按钮点击 */
function onClear() {
  numberValue.value = undefined
  inputValue.value = ''
  isValidate.value = true
  emit('clear')
}

/** 输入变化事件 */
function onInput(value: string) {
  // 更严格的输入过滤
  let filteredValue = value
  
  // 只保留数字、小数点、负号
  filteredValue = filteredValue.replace(/[^\d.-]/g, '')
  
  // 确保只有一个负号且在开头
  const minusCount = (filteredValue.match(/-/g) || []).length
  if (minusCount > 1) {
    filteredValue = filteredValue.replace(/-/g, '')
    if (filteredValue !== '') {
      filteredValue = '-' + filteredValue
    }
  }
  
  // 确保只有一个小数点
  const dotCount = (filteredValue.match(/\./g) || []).length
  if (dotCount > 1) {
    const parts = filteredValue.split('.')
    filteredValue = parts[0] + '.' + parts.slice(1).join('')
  }
  
  // 如果负号不在开头，移除它
  if (filteredValue.includes('-') && !filteredValue.startsWith('-')) {
    filteredValue = filteredValue.replace(/-/g, '')
  }
  
  // 更新输入值
  inputValue.value = filteredValue
  
  // 实时验证数字格式
  if (filteredValue === '' || filteredValue === '-') {
    numberValue.value = undefined
  } else {
    const num = parseFloat(filteredValue)
    if (!isNaN(num)) {
      // 范围检查
      let finalNum = num
      if (props.min !== undefined && finalNum < props.min) {
        finalNum = props.min
      }
      if (props.max !== undefined && finalNum > props.max) {
        finalNum = props.max
      }
      
      // 应用步长限制
      if (props.stepStrictly && props.step) {
        const steps = Math.round(finalNum / props.step)
        finalNum = steps * props.step
      }
      
      numberValue.value = finalNum
    }
  }
  
  emit('change', numberValue.value)
}

/** 格式化数值显示 */
function formatDisplayValue(value: number): string {
  if (props.precision !== undefined) {
    return value.toFixed(props.precision)
  }
  return value.toString()
}

/** 失焦事件 */
function onBlur(e: FocusEvent) {
  // 失焦时格式化显示值
  if (numberValue.value !== undefined) {
    inputValue.value = formatDisplayValue(numberValue.value)
  } else if (inputValue.value === '-' || inputValue.value === '.') {
    // 清理无效输入
    inputValue.value = ''
  }
  
  validate()
  emit('blur', e)
}

/** 聚焦事件 */
function onFocus(e: FocusEvent) {
  emit('focus', e)
}

/** 键盘事件 */
function onKeyup(e: KeyboardEvent) {
  // 支持上下箭头调整数值
  if (props.controls && numberValue.value !== undefined) {
    if (e.key === 'ArrowUp') {
      e.preventDefault()
      const newValue = numberValue.value + (props.step || 1)
      if (props.max === undefined || newValue <= props.max) {
        numberValue.value = newValue
      }
    } else if (e.key === 'ArrowDown') {
      e.preventDefault()
      const newValue = numberValue.value - (props.step || 1)
      if (props.min === undefined || newValue >= props.min) {
        numberValue.value = newValue
      }
    }
  }
  
  emit('keyup', e)
}

/** 校验逻辑 */
function validate(): boolean {
  // 必填校验
  if (props.required && (numberValue.value === undefined || numberValue.value === null)) {
    isValidate.value = false
    return false
  }

  // 数值校验
  if (inputValue.value !== '' && numberValue.value === undefined) {
    // 输入了内容但不是有效数字
    isValidate.value = false
    return false
  }

  if (numberValue.value !== undefined && numberValue.value !== null) {
    // 最小值校验
    if (props.min !== undefined && numberValue.value < props.min) {
      isValidate.value = false
      return false
    }

    // 最大值校验
    if (props.max !== undefined && numberValue.value > props.max) {
      isValidate.value = false
      return false
    }
  }

  isValidate.value = true
  return true
}

/** 自动恢复校验 */
watch(numberValue, () => {
  if (!isValidate.value) {
    isValidate.value = true
  }
})

// 验证状态样式
const validateStatus = computed(() => {
  if (props.required && !isValidate.value) {
    return 'error'
  }
  return ''
})

// 输入限制：只允许数字、小数点、负号
function onKeydown(e: KeyboardEvent) {
  const allowedKeys = [
    'Backspace', 'Delete', 'Tab', 'Escape', 'Enter',
    'ArrowLeft', 'ArrowRight', 'ArrowUp', 'ArrowDown',
    'Home', 'End'
  ]
  
  if (allowedKeys.includes(e.key)) {
    return
  }
  
  // 允许 Ctrl/Cmd + A/C/V/X
  if ((e.ctrlKey || e.metaKey) && ['a', 'c', 'v', 'x'].includes(e.key.toLowerCase())) {
    return
  }
  
  // 允许数字
  if (/^\d$/.test(e.key)) {
    return
  }
  
  // 允许小数点（但不能重复，且整数模式下不允许）
  if (e.key === '.' && !inputValue.value.includes('.') && props.precision !== 0) {
    return
  }
  
  // 允许负号（但只能在开头，且不能重复）
  if (e.key === '-' && inputValue.value === '' && !inputValue.value.includes('-')) {
    return
  }
  
  // 阻止科学计数法输入（e, E）
  if (e.key.toLowerCase() === 'e') {
    e.preventDefault()
    return
  }
  
  e.preventDefault()
}

/** 粘贴事件处理 */
function onPaste(e: ClipboardEvent) {
  e.preventDefault()
  
  const pastedText = e.clipboardData?.getData('text') || ''
  
  // 验证粘贴的内容是否为有效数字
  const cleanText = pastedText.replace(/[^\d.-]/g, '')
  const num = parseFloat(cleanText)
  
  if (!isNaN(num)) {
    // 检查范围
    let finalNum = num
    if (props.min !== undefined && finalNum < props.min) {
      finalNum = props.min
    }
    if (props.max !== undefined && finalNum > props.max) {
      finalNum = props.max
    }
    
    // 格式化显示
    inputValue.value = formatDisplayValue(finalNum)
    
    numberValue.value = finalNum
    emit('change', finalNum)
  }
}

defineExpose({ validate, isValidate })
</script>

<template>
  <el-input
      v-model="inputValue"
      type="number"
      :size="elSize"
      :placeholder="placeholder"
      :disabled="disabled"
      :readonly="readonly"
      :clearable="clearable"
      :validate-event="false"
      @input="onInput"
      @clear="onClear"
      @blur="onBlur"
      @focus="onFocus"
      @keyup="onKeyup"
      @keydown="onKeydown"
      @paste="onPaste"
      v-bind="attrs"
  >
    <!-- 前缀插槽 -->
    <template v-if="prefix || $slots.prefix" #prefix>
      <slot name="prefix">{{ prefix }}</slot>
    </template>
    
    <!-- 后缀插槽 -->
    <template v-if="suffix || $slots.suffix" #suffix>
      <slot name="suffix">{{ suffix }}</slot>
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