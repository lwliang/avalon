<template>
  <el-date-picker
      v-model="dateValue"
      :type="pickerType"
      :format="format"
      :value-format="format"
      :editable="editable"
      :disabled="disabled"
      :clearable="clearable"
      :placeholder="displayPlaceholder"
      :start-placeholder="startPlaceholder"
      :end-placeholder="endPlaceholder"
      :range-separator="rangeSeparator"
      @change="onChange"
      @focus="onFocus"
      @blur="onBlur"
      @clear="onClear"
  />
</template>

<script setup lang="ts">
import {ref, computed, watch} from 'vue'

// 类型定义
type DateTimePickerType = 'datetime' | 'datetimerange'

// props 定义
const props = withDefaults(defineProps<{
  type?: DateTimePickerType
  format?: string
  editable?: boolean
  disabled?: boolean
  clearable?: boolean
  border?: boolean
  startPlaceholder?: string
  endPlaceholder?: string
  rangeSeparator?: string
}>(), {
  type: 'datetime',
  editable: true,
  disabled: false,
  clearable: true,
  border: false,
  format: 'YYYY-MM-DD HH:mm:ss',
  startPlaceholder: '开始日期',
  endPlaceholder: '结束日期',
  rangeSeparator: ' - '
})

/** 内部日期状态 */
const startDate = defineModel<string>({
  default: ''
})
const endDate = defineModel<string>('end', {
  default: ''
})

// emits 定义
const emit = defineEmits<{
  (e: 'change', v: string | [string, string]): void
  (e: 'focus'): void
  (e: 'blur'): void
}>()

// Element Plus date picker 类型映射
const pickerType = computed(() => {
  if (props.type === 'datetimerange') {
    return 'datetimerange'
  }
  return 'datetime'
})

// 内部日期值，用于 el-date-picker
const dateValue = ref<string | [string, string] | null>(null)

// 初始化日期值
function initDateValue() {
  if (props.type === 'datetimerange') {
    const start = startDate.value || ''
    const end = endDate.value || ''
    if (start || end) {
      dateValue.value = [start, end]
    } else {
      dateValue.value = null
    }
  } else {
    const start = startDate.value || ''
    dateValue.value = start || null
  }
}

// 监听 string 变化，更新 dateValue
watch(() => [startDate.value, endDate.value], () => {
  initDateValue()
}, { immediate: true })

// 监听 dateValue 变化，更新 string
watch(dateValue, (newVal) => {
  if (props.type === 'datetimerange') {
    if (Array.isArray(newVal)) {
      startDate.value = newVal[0] || ''
      endDate.value = newVal[1] || ''
    } else {
      startDate.value = ''
      endDate.value = ''
    }
  } else {
    startDate.value = (typeof newVal === 'string' ? newVal : '') || ''
  }
})

/** 格式 */
const format = computed(() => props.format)

/** 占位符 */
const displayPlaceholder = computed(() => {
  if (props.type === 'datetimerange') {
    return undefined // 范围选择器不需要单独的 placeholder
  }
  return props.startPlaceholder || '请选择日期时间'
})

/** 日期选择变化事件 */
function onChange(value: string | [string, string] | null) {
  if (props.type === 'datetimerange') {
    if (Array.isArray(value)) {
      const [start, end] = value
      startDate.value = start || ''
      endDate.value = end || ''
      emit('change', [start || '', end || ''])
    } else {
      startDate.value = ''
      endDate.value = ''
      emit('change', ['', ''])
    }
  } else {
    const stringValue = (typeof value === 'string' ? value : '') || ''
    startDate.value = stringValue
    emit('change', stringValue)
  }
}

/** 清空 */
function onClear() {
  startDate.value = ''
  endDate.value = ''
  dateValue.value = null
  emit('change', props.type === 'datetimerange' ? ['', ''] : '')
}

/** focus/blur 事件 */
function onFocus() {
  emit('focus')
}

function onBlur() {
  emit('blur')
}
</script>

<style scoped>
/* 可以根据需要添加样式 */
</style>

