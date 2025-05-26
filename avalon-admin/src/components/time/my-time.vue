<template>
  <MyPopover ref="popoverRef" placement="bottom" trigger="click" popperClass="w-fit" @show="onPopperShow">
    <template #default>
      <div v-if="props.type === 'timerange'" class="time-range-inputs">
        <MyInput
            v-model="startTimeOnlyTime"
            :border="border"
            :readonly="!editable"
            :disabled="disabled"
            :clearable="clearable"
            :placeholder="props.startPlaceholder ?? '开始时间'"
            @clear="onClear"
            @change="onStartInput"
            style="width: 120px;"
        />
        <span class="range-separator" style="margin: 0 8px;">{{ rangeSeparator }}</span>
        <MyInput
            v-model="endTimeOnlyTime"
            :border="border"
            :readonly="!editable"
            :disabled="disabled"
            :clearable="clearable"
            :placeholder="props.endPlaceholder ?? '结束时间'"
            @clear="onClear"
            @change="onEndInput"
            style="width: 120px;"
        />
      </div>
      <MyInput
          v-else
          v-model="startTimeOnlyTime"
          suffix-icon-type="far"
          suffix-icon="clock"
          :border="border"
          :readonly="!editable"
          :disabled="disabled"
          :clearable="clearable"
          :placeholder="displayPlaceholder"
          @clear="onClear"
          @change="onStartInput"
      />
    </template>

    <!-- 弹出内容：时间面板 -->
    <template #content>
      <TimePanel
          ref="timePanelRef"
          :type="type"
          :startTime="startTimeOnlyTime.value"
          :endTime="endTimeOnlyTime?.value"
          :format="format"
          @select="onSelect"
      />
    </template>
  </MyPopover>
</template>

<script setup lang="ts">
import {ref, computed, watch} from 'vue'
import dayjs from 'dayjs'
import MyPopover from "../popover/my-popover.vue";

import TimePanel from "../calendar/TimePanel.vue"; // 你需要有这个时间选择面板
import FormField from "../../model/FormField"
import {normalizeAndFormatTime} from "../../util/dateUtils.ts";

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
})

const startTime = defineModel<FormField>({required: true})
const startTimeOnlyTime = ref<FormField>(new FormField(''))
const endTime = defineModel<FormField>('end', {required: false})
const endTimeOnlyTime = ref<FormField>(new FormField(''))

watch(() => [startTime.value, startTime.value.value], () => {
      if (startTime.value && startTime.value.value) {
        startTimeOnlyTime.value.value = dayjs(startTime.value.value).format(props.format)
      } else {
        startTimeOnlyTime.value.value = ''
      }
    }
    , {immediate: true})

watch(() => [endTime.value, endTime?.value?.value], () => {
      if (endTime.value && endTime.value.value) {
        endTimeOnlyTime.value.value = dayjs(endTime.value.value).format(props.format)
      } else {
        endTimeOnlyTime.value.value = ''
      }
    }
    , {immediate: true})

const popoverRef = ref()
const timePanelRef = ref()
const emit = defineEmits<{
  (e: 'change', v: string | [string, string]): void
  (e: 'focus'): void
  (e: 'blur'): void
}>()

const onPopperShow = () => {
  if (timePanelRef.value) {
    timePanelRef.value.scrollToSelected();
  }
}

const rangeSeparator = computed(() => props.rangeSeparator ?? ' ~ ')
const format = computed(() => props.format ?? 'HH:mm:ss')
const displayPlaceholder = computed(() =>
    props.type === 'timerange'
        ? `${props.startPlaceholder ?? '开始时间'}${rangeSeparator.value}${props.endPlaceholder ?? '结束时间'}`
        : props.startPlaceholder ?? '请选择时间'
)


function onClear() {
  startTime.value.value = ''
  if (endTime.value) endTime.value.value = ''
  emit('change', props.type === 'timerange' ? ['', ''] : '')
}

function onStartInput(val: string) {
  if (!props.editable) return
  const time = normalizeAndFormatTime(val, props.format)
  if (time) {
    startTime.value.value = time
  } else {
    startTime.value.value = ''
  }
  onPopperShow()
}

function onEndInput(val: string) {
  if (!props.editable) return
  if (endTime.value) {
    const time = normalizeAndFormatTime(val, props.format)
    if (time) {
      endTime.value.value = time
    } else {
      endTime.value.value = ''
    }
    onPopperShow()
  }
}

function onSelect(val: string | [string, string]) {
  if (props.type === 'timerange') {
    if (Array.isArray(val)) {
      startTimeOnlyTime.value.value = val[0] as string || ''
      startTime.value.value = normalizeAndFormatTime(startTimeOnlyTime.value.value, props.format)
      if (endTime.value) {
        endTimeOnlyTime.value.value = val[1] as string || ''
        endTime.value.value = normalizeAndFormatTime(endTimeOnlyTime.value.value, props.format)
      } else {

      }
      emit('change', [startTime.value.value, endTime.value?.value ?? ''])
      if (startTime.value.value && endTime.value?.value) closePopover()
    }
  } else {
    startTimeOnlyTime.value.value = val as string
    startTime.value.value = normalizeAndFormatTime(startTimeOnlyTime.value.value, props.format)
    emit('change', startTime.value.value)
    closePopover()
  }
}


function onFocus() {
  emit('focus')
}

function onBlur() {
  emit('blur')
}

function closePopover() {
  if (popoverRef.value && typeof popoverRef.value.close === 'function') {
    popoverRef.value.close()
  }
}
</script>

<style scoped>
.time-range-inputs {
  display: flex;
  align-items: center;
}

.range-separator {
  user-select: none;
}
</style>
