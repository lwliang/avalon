<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import { computed, watch, ref } from 'vue'

interface TextareaProps {
  required?: boolean
  readonly?: boolean
  disabled?: boolean
  placeholder?: string
  maxlength?: number
  showWordLimit?: boolean
  autosize?: boolean | { minRows?: number; maxRows?: number }
  resize?: 'none' | 'both' | 'horizontal' | 'vertical'
  pt?: number // padding-top
  rows?: number
  cols?: number
}

const props = withDefaults(defineProps<TextareaProps>(), {
  required: false,
  readonly: false,
  disabled: false,
  placeholder: '',
  showWordLimit: false,
  resize: 'vertical',
  pt: 0,
  rows: 3
})

const emit = defineEmits<{
  inputMessage: [value: string]
  change: [value: string]
  focus: [event: FocusEvent]
  blur: [event: FocusEvent]
}>()

const modelValue = defineModel<string>({
  default: ''
})

// 验证状态
const isValid = ref(true)

// 监听值变化，更新验证状态
watch(() => modelValue.value, () => {
  setValidate(true)
})

const setValidate = (valid: boolean) => {
  isValid.value = valid
}

// 验证函数
const validate = () => {
  if (props.required) {
    if (!modelValue.value || modelValue.value.trim() === '') {
      isValid.value = false
      return false
    }
  }
  
  setValidate(!!modelValue.value)
  return isValid.value
}

// 处理回车事件
const handleKeydown = (event: KeyboardEvent) => {
  if (event.key === 'Enter' && !event.shiftKey) {
    event.preventDefault()
    emit('inputMessage', modelValue.value || '')
  }
}

// 处理输入变化
const handleInput = (value: string) => {
  modelValue.value = value
  emit('change', value)
}

// 处理焦点事件
const handleFocus = (event: FocusEvent) => {
  emit('focus', event)
}

const handleBlur = (event: FocusEvent) => {
  emit('blur', event)
}

// 计算输入框样式
const inputStyle = computed(() => ({
  paddingTop: `${props.pt}px`
}))

// 计算自动调整大小配置
const autosizeConfig = computed(() => {
  if (typeof props.autosize === 'boolean') {
    return props.autosize
  }
  return props.autosize || { minRows: props.rows, maxRows: 10 }
})

defineExpose({ validate })
</script>

<template>
  <div class="textarea-wrapper">
    <el-input
      v-model="modelValue"
      type="textarea"
      :placeholder="props.placeholder"
      :readonly="props.readonly"
      :disabled="props.disabled"
      :maxlength="props.maxlength"
      :show-word-limit="props.showWordLimit"
      :autosize="autosizeConfig"
      :resize="props.resize"
      :rows="props.rows"
      :cols="props.cols"
      :class="{
        'is-error': !isValid
      }"
      :style="inputStyle"
      @keydown="handleKeydown"
      @input="handleInput"
      @focus="handleFocus"
      @blur="handleBlur"
    />
  </div>
</template>

<style scoped>
.textarea-wrapper {
  width: 100%;
  margin-top: 8px;
}

.textarea-wrapper :deep(.el-textarea__inner) {
  border-radius: 4px;
  transition: all 0.3s ease;
}

.textarea-wrapper :deep(.el-textarea.is-error .el-textarea__inner) {
  border-color: var(--el-color-danger);
}

.textarea-wrapper :deep(.el-textarea.is-error .el-textarea__inner:focus) {
  border-color: var(--el-color-danger);
  box-shadow: 0 0 0 1px var(--el-color-danger-light-8);
}
</style>