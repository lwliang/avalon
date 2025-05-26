<template>
  <div class="w-64">
    <!-- 日历表格标题，显示当前月份 -->
    <div v-if="showTitle" class="flex justify-center items-center">
      <span>{{ title }}</span>
    </div>
    <!-- 日历表格，7列（星期天-星期六） -->
    <div class="grid grid-cols-7  text-xs text-center">
      <!-- 星期标题行 -->
      <span v-for="d in days" :key="d"
            class="h-[32px] w-[32px] flex items-center justify-center font-bold text-text-secondary">{{ d }}</span>
      <!-- 日历格子 -->

      <span
          v-for="date in calendar"
          :key="date.key"
          :class="[
          ' flex items-center justify-center  cursor-pointer hover:text-primary h-[32px] w-[32px]',
          // 当前月高亮，否则灰色
          date.inCurrent ? 'text-text-primary' : 'text-text-disabled',
           // 头部日期
        isRangeStart(date.date) ? 'rounded-l-full' : '',
          // 尾部日期
        isRangeEnd(date.date) ? 'rounded-r-full' : '',
          // 范围选中高亮
          isInRange(date.date) ? 'bg-primary-light-9' : '',
        ]"
          @click="select(date.date)"
      >
        <span :class="['flex items-center justify-center  h-[24px] w-[24px]',
         isRangeStart(date.date) ? 'bg-primary text-white rounded-full' : '',
          // 尾部日期
        isRangeEnd(date.date) ? 'bg-primary text-white rounded-full' : '',
        // 选中日期样式
        isSelected(date.date) ? 'bg-primary text-white rounded-full hover:text-white' :'']">
        {{ date.day }}
        </span>

      </span>
    </div>
  </div>
</template>

<script setup lang="ts">
import {computed} from 'vue'
import dayjs from 'dayjs'

/**
 * props说明:
 * - monthOffset: 相对于今天的月份偏移量（0=本月, 1=下月, -1=上月等）
 * - selected: 当前选中的日期
 * - range: 当前选择的范围（[开始, 结束]），用于范围高亮
 */
const props = defineProps<{
  monthOffset?: number,
  selected?: any,
  range?: [any, any],
  showTitle?: boolean,
}>()
const emit = defineEmits<{
  (e: 'select', date: Date): void
}>()

// 当前日期
const today = dayjs()
// baseMonth: 当前要展示的月份
const baseMonth = computed(() => today.add(props.monthOffset ?? 0, 'month'))
// 标题：形如 2024年05月
const title = computed(() => baseMonth.value.format('YYYY年MM月'))
// 星期标题
const days = ['日', '一', '二', '三', '四', '五', '六']

/**
 * 计算要展示的日历格子
 * - 填充上月、下月的空白天
 * - inCurrent: 是否属于当前月
 * - day: 日期数字
 */
const calendar = computed(() => {
  // 本月第一天
  const firstDay = baseMonth.value.startOf('month')
  // 日历表格的起始日期（本月第一天所在周的周日）
  const start = firstDay.startOf('week')
  // 日历表格的结束日期（本月最后一天所在周的周六）
  const end = firstDay.endOf('month').endOf('week')
  const arr = []
  let date = start
  // 遍历所有要渲染的日期格子
  while (date.isBefore(end) || date.isSame(end, 'day')) {
    arr.push({
      key: date.format('YYYY-MM-DD'),  // 唯一key
      date: date.toDate(),              // 日期对象
      day: date.date(),                 // 日
      inCurrent: date.month() === baseMonth.value.month() // 是否当前月
    })
    date = date.add(1, 'day')
  }
  return arr
})

/**
 * 点击某天，向上传递
 */
function select(date: Date) {
  emit('select', date)
}

/**
 * 判断该日期是否被选中
 */
function isSelected(date: Date) {
  if (!props.selected) return false
  return dayjs(date).isSame(props.selected, 'day')
}

/**
 * 判断该日期是否处于范围内（用于范围高亮）
 */
function isInRange(date: Date) {
  if (!props.range) return false
  const [start, end] = props.range
  if (!start || !end) return false
  // 只高亮"范围内部"，含首尾
  return dayjs(date).isSameOrAfter(start, 'day') && dayjs(date).isSameOrBefore(end, 'day')
}

/**
 * 判断该日期是否为范围的开始（头部）
 */
function isRangeStart(date: Date) {
  if (!props.range) return false
  const [start] = props.range
  if (!start) return false
  return dayjs(date).isSame(start, 'day')
}

/**
 * 判断该日期是否为范围的结束（尾部）
 */
function isRangeEnd(date: Date) {
  if (!props.range) return false
  const [, end] = props.range
  if (!end) return false
  return dayjs(date).isSame(end, 'day')
}
</script>
