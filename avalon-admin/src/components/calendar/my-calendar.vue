<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {computed} from 'vue'

const props = withDefaults(defineProps<{
  value?: Date
  modelValue?: Date
  range?: [Date, Date]
  firstDayOfWeek?: number
  date?: Date
  hideHeader?: boolean
  title?: string
  showHeader?: boolean
  showToday?: boolean
  showWeekNumber?: boolean
  cellClassName?: (date: Date) => string | string[]
  cellStyle?: (date: Date) => object
  disabledDate?: (date: Date) => boolean
  format?: string
  valueFormat?: string
  size?: 'large' | 'default' | 'small'
  popperClass?: string
  popperStyle?: object
  teleported?: boolean
  readonly?: boolean
  clearable?: boolean
  editable?: boolean
  placeholder?: string
  startPlaceholder?: string
  endPlaceholder?: string
  rangeSeparator?: string
  defaultValue?: Date
  defaultTime?: Date
  isRange?: boolean
  unlinkPanels?: boolean
  prefixIcon?: string
  clearIcon?: string
  validateEvent?: boolean
  shortcuts?: Array<{ text: string; value: Date | (() => Date) }>
  id?: string
  name?: string
  label?: string
  tabindex?: string | number
  pickerOptions?: object
  arrowControl?: boolean
}>(), {
  value: undefined,
  modelValue: undefined,
  firstDayOfWeek: 1,
  date: () => new Date(),
  hideHeader: false,
  title: '',
  showHeader: true,
  showToday: true,
  showWeekNumber: false,
  format: 'YYYY-MM-DD',
  valueFormat: 'YYYY-MM-DD',
  size: 'default',
  teleported: true,
  readonly: false,
  clearable: true,
  editable: true,
  placeholder: '请选择日期',
  startPlaceholder: '开始日期',
  endPlaceholder: '结束日期',
  rangeSeparator: '至',
  isRange: false,
  unlinkPanels: false,
  prefixIcon: 'Calendar',
  clearIcon: 'CircleClose',
  validateEvent: true,
  arrowControl: false
})

const emit = defineEmits<{
  (e: 'update:value', value: Date): void
  (e: 'update:modelValue', value: Date): void
  (e: 'change', value: Date): void
  (e: 'blur', event: FocusEvent): void
  (e: 'focus', event: FocusEvent): void
  (e: 'calendar-change', value: Date): void
  (e: 'panel-change', date: Date, mode: string, view: string): void
  (e: 'visible-change', visible: boolean): void
  (e: 'range-change', value: [Date, Date]): void
  (e: 'select', value: Date): void
  (e: 'day-click', value: Date): void
  (e: 'month-click', value: Date): void
  (e: 'year-click', value: Date): void
}>()

// 计算属性
const calendarValue = computed({
  get: () => props.value || props.modelValue,
  set: (val: Date) => {
    emit('update:value', val)
    emit('update:modelValue', val)
  }
})

const isRangeType = computed(() => props.isRange || props.range !== undefined)
</script>

<template>
  <el-calendar
    v-model="calendarValue"
            :range="range"
    :first-day-of-week="firstDayOfWeek"
    :date="date"
    :hide-header="hideHeader"
    :title="title"
    :show-header="showHeader"
    :show-today="showToday"
    :show-week-number="showWeekNumber"
    :cell-class-name="cellClassName"
    :cell-style="cellStyle"
    :disabled-date="disabledDate"
    :format="format"
    :value-format="valueFormat"
    :size="size"
    :popper-class="popperClass"
    :popper-style="popperStyle"
    :teleported="teleported"
    :readonly="readonly"
    :clearable="clearable"
    :editable="editable"
    :placeholder="placeholder"
    :start-placeholder="startPlaceholder"
    :end-placeholder="endPlaceholder"
    :range-separator="rangeSeparator"
    :default-value="defaultValue"
    :default-time="defaultTime"
    :is-range="isRange"
    :unlink-panels="unlinkPanels"
    :prefix-icon="prefixIcon"
    :clear-icon="clearIcon"
    :validate-event="validateEvent"
    :shortcuts="shortcuts"
    :id="id"
    :name="name"
    :label="label"
    :tabindex="tabindex"
    :picker-options="pickerOptions"
    :arrow-control="arrowControl"
    @change="(val: Date) => emit('change', val)"
    @blur="(event: FocusEvent) => emit('blur', event)"
    @focus="(event: FocusEvent) => emit('focus', event)"
    @calendar-change="(val: Date) => emit('calendar-change', val)"
    @panel-change="(date: Date, mode: string, view: string) => emit('panel-change', date, mode, view)"
    @visible-change="(visible: boolean) => emit('visible-change', visible)"
    @range-change="(val: [Date, Date]) => emit('range-change', val)"
    @select="(val: Date) => emit('select', val)"
    @day-click="(val: Date) => emit('day-click', val)"
    @month-click="(val: Date) => emit('month-click', val)"
    @year-click="(val: Date) => emit('year-click', val)">
  </el-calendar>
</template>

<style scoped>

</style>
