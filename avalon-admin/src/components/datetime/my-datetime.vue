<template>
  <MyPopover ref="popoverRef" placement="bottom" trigger="click" popperClass="w-fit">
    <!-- 触发输入框 -->
    <template #default>
      <div v-if="props.type === 'datetimerange'" class="date-range-inputs flex">
        <MyInput
            v-model="startDate"
            :border="false"
            :readonly="!editable"
            :disabled="disabled"
            :clearable="clearable"
            :placeholder="props.startPlaceholder ?? '开始日期'"
            @clear="onClear"
            @change="onStartInput"
            @focus="onFocus"
            @blur="onBlur"
            style="width: 180px;"
        />
        <span class="range-separator" style="margin: 0 8px;">{{ rangeSeparator }}</span>
        <MyInput v-if="endDate"
                 v-model="endDate"
                 :border="false"
                 :readonly="!editable"
                 :disabled="disabled"
                 :clearable="clearable"
                 :placeholder="props.endPlaceholder ?? '结束日期'"
                 @clear="onClear"
                 @change="onEndInput"
                 @focus="onFocus"
                 @blur="onBlur"
                 style="width: 180px;"
        />
      </div>
      <MyInput
          v-else
          v-model="startDate"
          suffix-icon-type="far"
          suffix-icon="calendar"
          :border="border"
          :readonly="!editable"
          :disabled="disabled"
          :clearable="clearable"
          :placeholder="displayPlaceholder"
          @clear="onClear"
          @change="onStartInput"
          @blur="onBlur"
      />
    </template>

    <!-- 弹出内容：日历面板 -->
    <template #content>
      <CalendarPanel
          :type="type"
          :startDate="startDate.value"
          :endDate="endDate?.value"
          :format="format"
          @select="onSelect"
          @sure="sureClick"
          @click="onCalendarClick"
      />
    </template>
  </MyPopover>
</template>


<script setup lang="ts">
import {ref, computed} from 'vue'
import dayjs from 'dayjs'
import MyPopover from "../popover/my-popover.vue";
import CalendarPanel from "../calendar/CalendarPanel.vue";
import FormField from "../../model/FormField.ts";

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
})

/** 内部日期状态 */
const startDate = defineModel<FormField>({
  required: true,
})
const endDate = defineModel<FormField>('end', {
  required: false,
})

// emits 定义
const emit = defineEmits<{
  (e: 'change', v: string | [string, string]): void
  (e: 'focus'): void
  (e: 'blur'): void
}>()

// popover 控制
const popoverRef = ref()

function onStartInput(val: string) {
  if (!props.editable) return
  if (dayjs(val, format.value, true).isValid()) {
    startDate.value.value = val
  } else {
    startDate.value.value = ''
  }
}

function onEndInput(val: string) {
  if (!props.editable) return
  if (dayjs(val, format.value, true).isValid()) {
    if (endDate.value) endDate.value.value = val
  } else {
    if (endDate.value) endDate.value.value = ''
  }
}

/** 分隔符 */
const rangeSeparator = computed(() => props.rangeSeparator ?? ' - ')
/** 格式 */
const format = computed(() => props.format ?? 'YYYY-MM-DD')

/** 输入框内容 */
const inputString = ref('')


/** 占位符 */
const displayPlaceholder = computed(() =>
    props.type === 'datetimerange'
        ? `${props.startPlaceholder ?? '开始日期'}${rangeSeparator.value}${props.endPlaceholder ?? '结束日期'}`
        : props.startPlaceholder ?? '请选择日期'
)

/** 清空 */
function onClear() {
  startDate.value.value = ''
  if (endDate.value) {
    endDate.value.value = ''
  }

  inputString.value = ''
  emit('change', props.type === 'datetimerange' ? ['', ''] : '')
}

/** 支持直接输入 */
function onInput(val: string) {
  if (!props.editable) return
  if (props.type === 'datetimerange') {
    const [startStr, endStr] = val.split(rangeSeparator.value).map(s => s.trim())
    if (dayjs(startStr, format.value, true).isValid()) {
      startDate.value.value = startStr
    } else {
      startDate.value.value = ''
    }
    if (endDate.value) {
      if (dayjs(endStr, format.value, true).isValid()) {
        endDate.value.value = endStr
      } else {
        endDate.value.value = ''
      }
    }
  } else {
    if (dayjs(val, format.value, true).isValid()) {
      startDate.value.value = val
    } else {
      startDate.value.value = ''
    }
  }
}

/** 日历面板选择事件 */
function onSelect(val: Date | [Date | null, Date | null]) {
  if (props.type === 'datetimerange') {
    if (Array.isArray(val)) {
      startDate.value.value = val[0] ? formatValue(val[0]) : ''
      if (endDate.value) {
        endDate.value.value = val[1] ? formatValue(val[1]) : ''
      }
      inputString.value = startDate.value && endDate.value
          ? `${startDate.value}${rangeSeparator.value}${endDate.value}`
          : ''
      emit('change', [startDate.value.value, endDate.value?.value])
    }
  } else {
    startDate.value.value = val ? formatValue(val as Date) : ''
    inputString.value = startDate.value.value
    emit('change', startDate.value.value)
  }
}

/** focus/blur 事件 */
function onFocus() {
  emit('focus')
}

function onBlur() {
  emit('blur')
}

/** 关闭 Popover */
function closePopover() {
  if (popoverRef.value && typeof popoverRef.value.hide === 'function') {
    popoverRef.value.hide()
  }
}

/** 工具：日期格式化 */
function formatValue(val: string | Date): string {
  if (!val) return ''
  return dayjs(val).format(format.value)
}

const sureClick = () => {
  closePopover()
}
const onCalendarClick = (e: MouseEvent) => {
  //e.stopPropagation()
}
</script>
