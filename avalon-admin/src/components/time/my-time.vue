<template>
  <el-time-picker
      v-model="timeValue"
      :is-range="isRange"
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
import { ref, computed, watch } from 'vue'

type TimePickerType = 'time' | 'timerange'

const props = withDefaults(defineProps<{
  type?: TimePickerType
  format?: 'HH:mm' | 'HH:mm:ss'
  editable?: boolean
  disabled?: boolean
  clearable?: boolean
  border?: boolean
  startPlaceholder?: string
  endPlaceholder?: string
  rangeSeparator?: string
}>(), {
  type: 'time',
  format: 'HH:mm:ss',
  editable: true,
  disabled: false,
  clearable: true,
  border: false,
  startPlaceholder: '开始时间',
  endPlaceholder: '结束时间',
  rangeSeparator: ' ~ '
})

const startTime = defineModel<string>({
  default: ''
})
const endTime = defineModel<string>('end', {
  default: ''
})

const emit = defineEmits<{
  (e: 'change', v: string | [string, string]): void
  (e: 'focus'): void
  (e: 'blur'): void
}>()

// Element Plus time picker 配置
const isRange = computed(() => props.type === 'timerange')

// 内部时间值，用于 el-time-picker
const timeValue = ref<string | [string, string] | null>(null)

// 初始化时间值
function initTimeValue() {
  if (props.type === 'timerange') {
    const start = startTime.value || ''
    const end = endTime.value || ''
    if (start || end) {
      timeValue.value = [start, end]
    } else {
      timeValue.value = null
    }
  } else {
    const start = startTime.value || ''
    timeValue.value = start || null
  }
}

// 监听 string 变化，更新 timeValue
watch(() => [startTime.value, endTime.value], () => {
  initTimeValue()
}, { immediate: true })

// 监听 timeValue 变化，更新 string
watch(timeValue, (newVal) => {
  if (props.type === 'timerange') {
    if (Array.isArray(newVal)) {
      startTime.value = newVal[0] || ''
      endTime.value = newVal[1] || ''
    } else {
      startTime.value = ''
      endTime.value = ''
    }
  } else {
    startTime.value = (typeof newVal === 'string' ? newVal : '') || ''
  }
})

/** 格式 */
const format = computed(() => props.format)

/** 占位符 */
const displayPlaceholder = computed(() => {
  if (props.type === 'timerange') {
    return undefined // 范围选择器不需要单独的 placeholder
  }
  return props.startPlaceholder || '请选择时间'
})

/** 时间选择变化事件 */
function onChange(value: string | [string, string] | null) {
  if (props.type === 'timerange') {
    if (Array.isArray(value)) {
      const [start, end] = value
      startTime.value = start || ''
      endTime.value = end || ''
      emit('change', [start || '', end || ''])
    } else {
      startTime.value = ''
      endTime.value = ''
      emit('change', ['', ''])
    }
  } else {
    const stringValue = (typeof value === 'string' ? value : '') || ''
    startTime.value = stringValue
    emit('change', stringValue)
  }
}

/** 清空 */
function onClear() {
  startTime.value = ''
  endTime.value = ''
  timeValue.value = null
  emit('change', props.type === 'timerange' ? ['', ''] : '')
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
