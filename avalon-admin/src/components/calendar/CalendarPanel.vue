<script setup lang="ts">
import {ref, computed, watch} from 'vue'
import dayjs from 'dayjs'
import DateTable from './DateTable.vue'
import TimePanel from './TimePanel.vue'
import FormField from "../../model/FormField.ts";
import MyPopover from "../popover/my-popover.vue";
import MyButton from "../button/my-button.vue";
import {ButtonClickEvent} from "../../model/ButtonClickEvent.ts";

const props = withDefaults(defineProps<{
  type?: 'date' | 'datetime' | 'daterange' | 'datetimerange',
  startDate?: string | Date,
  endDate?: string | Date,
  format?: string, // 'YYYY-MM-DD' 或 'YYYY-MM-DD HH:mm:ss'
  showTime?: boolean
}>(), {
  type: 'date',
  format: 'YYYY-MM-DD HH:mm:ss',
  showTime: false,
})

const emit = defineEmits<{
  (e: 'select', date: Date | [Date | null, Date | null]): void
  (e: 'sure'): void,
  (e: 'click', event: MouseEvent): void
}>()

// 当前展示的月份偏移量（初始为0和1）
const monthOffset = ref<[number, number]>([0, 1])

// 单选的已选日期与时间
const selected = ref(props.startDate ? dayjs(props.startDate) : null)
const selectedTimeField = ref<FormField>(new FormField(props.startDate ? getTimeString(props.startDate) : '00:00:00'))
const selectedEndTimeField = ref<FormField>(new FormField(props.endDate ? getTimeString(props.endDate) : '00:00:00'))

// 范围选择
const range = ref<[any, any]>([
  props.startDate ? dayjs(props.startDate) : null,
  props.endDate ? dayjs(props.endDate) : null
])
const rangeTime = ref<[string, string]>([
  props.startDate ? getTimeString(props.startDate) : '00:00:00',
  props.endDate ? getTimeString(props.endDate) : '00:00:00'
])

// 同步外部变更
watch(() => props.startDate, v => {
  selected.value = v ? dayjs(v) : null
  range.value[0] = v ? dayjs(v) : null
  selectedTimeField.value.value = v ? getTimeString(v) : '00:00:00'
  rangeTime.value[0] = v ? getTimeString(v) : '00:00:00'
})
watch(() => props.endDate, v => {
  range.value[1] = v ? dayjs(v) : null
  selectedEndTimeField.value.value = v ? getTimeString(v) : '00:00:00'
  rangeTime.value[1] = v ? getTimeString(v) : '00:00:00'
})

// 顶部月份标题
const leftMonthTitle = computed(() =>
    dayjs().add(monthOffset.value[0], 'month').format('YYYY年MM月')
)
const rightMonthTitle = computed(() =>
    dayjs().add(monthOffset.value[1], 'month').format('YYYY年MM月')
)

// 月份切换逻辑
function prevMonth(idx: 0 | 1) {
  monthOffset.value[idx]--
}

function nextMonth(idx: 0 | 1) {
  monthOffset.value[idx]++
}

let selecting = ref(false)

function onSelectDate(val: string | Date) {
  if (isRangeType.value) {
    if (!selecting.value || !range.value[0] || (range.value[0] && range.value[1])) {
      range.value = [dayjs(val), null]
      selecting.value = true
      emitIfReady()
    } else {
      const start = range.value[0]
      const end = dayjs(val)
      if (end.isBefore(start, 'day')) {
        range.value = [end, start]
      } else {
        range.value[1] = end
      }
      selecting.value = false
      emitIfReady()
    }
  } else {
    selected.value = dayjs(val)
    emitIfReady()
  }
}

function onSelectTime(val: string, idx?: 0 | 1) {
  if (isRangeType.value) {
    if (idx !== undefined) {
      rangeTime.value[idx] = val
      if (idx == 0) {
        selectedTimeField.value.value = val
      } else {
        selectedEndTimeField.value.value = val
      }
      emitIfReady()
    }
  } else {
    selectedTimeField.value.value = val
    emitIfReady()
  }
}

const selectTimeChange = (val: string) => {

}

function emitIfReady() {
  if (isRangeType.value) {
    if (range.value[0] && range.value[1] && rangeTime.value[0] && rangeTime.value[1]) {
      const start = combineDateTime(range.value[0], rangeTime.value[0])
      const end = combineDateTime(range.value[1], rangeTime.value[1])
      emit('select', [start, end])
    } else {
      emit('select', [range.value[0]?.toDate() ?? null, range.value[1]?.toDate() ?? null])
    }
  } else {
    if (selected.value && (props.showTime || isDateTimeType.value)) {
      emit('select', combineDateTime(selected.value, selectedTimeField.value.value))
    } else if (selected.value) {
      emit('select', selected.value.toDate())
    }
  }
}

// 辅助
function getTimeString(val: string | Date) {
  const t = dayjs(val)
  return t.format('HH:mm:ss')
}

function combineDateTime(date: dayjs.Dayjs, time: string) {
  const d = date.format('YYYY-MM-DD')
  return dayjs(`${d} ${time}`, 'YYYY-MM-DD HH:mm:ss').toDate()
}

const isRangeType = computed(() => props.type === 'daterange' || props.type === 'datetimerange')
const isDateTimeType = computed(() => props.type === 'datetime' || props.type === 'datetimerange')
const showTimePanel = computed(() => props.showTime || isDateTimeType.value)
const timeFormat = computed(() => (props.format?.split(' ')[1] || 'HH:mm:ss'))

const timeInputPanelRef = ref()
const endTimeInputPanelRef = ref()
const endTimePanelRef = ref()
const starTimePanelRef = ref()
const timeInputRefPosition = () => {
  if (timeInputPanelRef.value) {
    timeInputPanelRef.value.scrollToSelected();
  }
}

const endTimeInputPanelRefPosition = () => {
  if (timeInputPanelRef.value) {
    timeInputPanelRef.value.scrollToSelected();
  }
}
const timePopperShowClick = () => {
  timeInputRefPosition()
  endTimeInputPanelRefPosition()
}

const starTimePanelRefHide = () => {
  if (starTimePanelRef.value) {
    starTimePanelRef.value.hide()
  }
}
const endTimePanelRefHide = () => {
  if (endTimePanelRef.value) {
    endTimePanelRef.value.hide()
  }
}

const sureBtnClick = (e: ButtonClickEvent) => {
  e.event.stopPropagation()
  starTimePanelRefHide()
  endTimePanelRefHide();
  emit('sure')
}

const onTimePanelClick = (e: MouseEvent) => {
  e.stopPropagation()
}
</script>

<template>
  <div class="w-max p-3 shadow rounded" @click="emit('click',$event)">
    <!-- 顶部 header -->
    <div class="flex justify-center items-center mb-2">
      <template v-if="isRangeType">
        <div class="flex items-center w-1/2">
          <my-button icon="arrow-left" size="small" circle plain @click="prevMonth(0)"/>
          <span class="flex-1 flex justify-center mx-2 text-base font-bold">{{ leftMonthTitle }}</span>
          <div class="w-[24px]"></div>
        </div>
        <div class="flex items-center w-1/2 justify-end">
          <div class="w-[24px]"></div>
          <span class="flex-1 flex justify-center mx-2 text-base font-bold">{{ rightMonthTitle }}</span>
          <my-button icon="arrow-right" size="small" circle plain @click="nextMonth(1)"/>
        </div>
      </template>
      <template v-else>
        <div class="flex items-center w-full justify-center">
          <my-button icon="arrow-left" size="small" circle plain @click="prevMonth(0)"/>
          <span class="flex-1 flex justify-center mx-2 text-base font-bold">{{ leftMonthTitle }}</span>
          <my-button icon="arrow-right" size="small" circle plain @click="nextMonth(0)"/>
        </div>
      </template>
    </div>

    <!-- 中部 日期表体 -->
    <div class="flex space-x-4">
      <template v-if="isRangeType">
        <DateTable
            :monthOffset="monthOffset[0]"
            :selected="range[0]"
            :range="range"
            :format="props.format"
            @select="onSelectDate"
        />
        <DateTable
            :monthOffset="monthOffset[1]"
            :selected="range[1]"
            :range="range"
            :format="props.format"
            @select="onSelectDate"
        />
      </template>
      <template v-else>
        <DateTable
            :monthOffset="monthOffset[0]"
            :selected="selected"
            :format="props.format"
            @select="onSelectDate"
        />
      </template>
    </div>

    <!-- 底部时间面板 -->
    <div v-if="showTimePanel" class="flex space-x-8 mt-3">
      <template v-if="isRangeType">
        <div class="flex-1">
          <MyPopover
              trigger="click"
              placement="top-start"
              @show="timePopperShowClick"
              popperClass="w-fit"
              ref="starTimePanelRef"
          >
            <div class="flex items-center gap-2">
              <span class="text-sm text-text-secondary">开始时间</span>
              <my-input
                  v-model="selectedTimeField"
                  size="small"
                  placeholder="请选择时间"
                  style="width: 120px"
                  @change="selectTimeChange"
              />

            </div>

            <template #content>
              <TimePanel
                  ref="timeInputPanelRef"
                  :format="timeFormat as 'HH:mm:ss' | 'HH:mm'"
                  :startTime="selectedTimeField.value"
                  @select="(t:any) => onSelectTime(t,0)"
                  @click="onTimePanelClick"
              />
            </template>
          </MyPopover>
        </div>
        <div class="flex-1">
          <MyPopover
              trigger="click"
              placement="top-start"
              @show="timePopperShowClick"
              popperClass="w-fit"
              ref="endTimePanelRef"
          >
            <div class="flex items-center gap-2">
              <span class="text-sm text-text-secondary">结束时间</span>
              <my-input
                  v-model="selectedEndTimeField"
                  size="small"
                  placeholder="请选择时间"
                  style="width: 100px"
                  @change="selectTimeChange"
              />
              <MyButton @click="sureBtnClick">OK</MyButton>
            </div>

            <template #content>
              <TimePanel
                  ref="endTimeInputPanelRef"
                  :format="timeFormat as 'HH:mm:ss' | 'HH:mm'"
                  :startTime="selectedEndTimeField.value"
                  @select="(t:any) => onSelectTime(t,1)"
                  @click="onTimePanelClick"
              />
            </template>
          </MyPopover>
        </div>
      </template>
      <template v-else>
        <MyPopover
            trigger="click"
            placement="top-start"
            @show="timePopperShowClick"
            popperClass="w-fit"
            ref="starTimePanelRef"
        >
          <div class="flex items-center gap-2">
            <span class="text-sm text-text-secondary">时间</span>
            <my-input
                v-model="selectedTimeField"
                size="small"
                placeholder="请选择时间"
                style="width: 120px"
                @change="selectTimeChange"
            />
            <MyButton @click="sureBtnClick">OK</MyButton>
          </div>

          <template #content>
            <TimePanel
                ref="timeInputPanelRef"
                :format="timeFormat as 'HH:mm:ss' | 'HH:mm'"
                :startTime="selectedTimeField.value"
                @select="(t:any) => onSelectTime(t)"
                @click="onTimePanelClick"
            />
          </template>
        </MyPopover>
      </template>
    </div>
  </div>
</template>
