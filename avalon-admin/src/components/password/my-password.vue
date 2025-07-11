<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import { ref, watch, computed } from "vue";
import { View, Hide } from '@element-plus/icons-vue'

const props = defineProps({
  required: Boolean,
  placeholder: {
    type: String,
    default: '请输入密码'
  },
  disabled: Boolean,
  clearable: {
    type: Boolean,
    default: true
  },
  showPassword: {
    type: Boolean,
    default: true
  },
  size: {
    type: String,
    default: 'default'
  }
})

const emit = defineEmits<{
  (e: 'clear'): void
  (e: 'change', value: string): void
  (e: 'blur'): void
  (e: 'focus'): void
  (e: 'keyup', event: KeyboardEvent): void
}>()

const formField = defineModel<string>({
  default: ''
})

// 验证状态
const isValidate = ref(true)

// 监听值变化进行验证
watch(formField, () => {
  setValidate(true)
})

const setValidate = (valid: boolean) => {
  isValidate.value = valid
}

const validate = () => {
  if (!props.required) {
    setValidate(true)
    return true;
  }
  const isValid = !!(formField.value && formField.value.trim())
  setValidate(isValid)
  return isValid
}

// 处理输入变化
const handleInput = (value: string) => {
  formField.value = value
  emit('change', value)
}

// 处理清空
const handleClear = () => {
  formField.value = ''
  emit('clear')
  emit('change', '')
}

// 处理焦点事件
const handleFocus = () => {
  emit('focus')
}

const handleBlur = () => {
  validate()
  emit('blur')
}

// 处理键盘事件
const handleKeyup = (event: KeyboardEvent) => {
  emit('keyup', event)
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
      v-model="formField"
      type="password"
      :placeholder="placeholder"
      :disabled="disabled"
      :clearable="clearable"
      :show-password="showPassword"
      :size="size"
      :validate-event="false"
      @input="handleInput"
      @clear="handleClear"
      @focus="handleFocus"
      @blur="handleBlur"
      @keyup="handleKeyup"
  />
</template>

<style scoped>
/* 可以根据需要添加样式 */
</style>