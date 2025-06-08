<script setup lang="ts">
import {ref, computed, watch, nextTick, onMounted} from 'vue'
import dayjs from 'dayjs'
import {isValidTime} from "../../util/dateUtils.ts";

type TimeType = 'time' | 'timerange'

// 工具函数：将 string | Date 统一转为字符串
function parseTimeString(val: string | undefined, format: string): string {
  if (!val) return ''
  return isValidTime(val, format) ? val : ''
}

const props = withDefaults(
    defineProps<{
      type?: TimeType
      startTime: string // 时间字符串 12：22：33
      endTime?: string
      format?: 'HH:mm' | 'HH:mm:ss'
    }>(),
    {
      type: 'time',
      format: 'HH:mm:ss'
    }
)

const emit = defineEmits<{
  (e: 'select', time: string | [string, string]): void
  (e: 'click', event: MouseEvent): void
}>()

const hours = Array.from({length: 24}, (_, i) => String(i).padStart(2, '0'))
const minutes = Array.from({length: 60}, (_, i) => String(i).padStart(2, '0'))
const seconds = Array.from({length: 60}, (_, i) => String(i).padStart(2, '0'))
const showSecond = computed(() => props.format === 'HH:mm:ss')

// 单选
const selected = ref(parseTimeString(props.startTime, props.format))
const selectedSplit = computed(() => {
  const arr = (selected.value || '').split(':')
  return {
    hour: arr[0] ?? '00',
    minute: arr[1] ?? '00',
    second: arr[2] ?? '00'
  }
})

// 区间
const range = ref<[string, string]>([
  parseTimeString(props.startTime, props.format),
  parseTimeString(props.endTime, props.format)
])
const rangeSplit = computed(() =>
    range.value.map(str => {
      const arr = (str || '').split(':')
      return {
        hour: arr[0] ?? '00',
        minute: arr[1] ?? '00',
        second: arr[2] ?? '00'
      }
    })
)

// 外部变更同步
watch(
    () => props.startTime,
    v => {
      if (props.type === 'timerange') {
        range.value[0] = parseTimeString(v, props.format)
      } else {
        selected.value = parseTimeString(v, props.format)
      }
    },
    {immediate: true}
)
watch(
    () => props.endTime,
    v => {
      if (props.type === 'timerange') {
        range.value[1] = parseTimeString(v, props.format)
      }
    },
    {immediate: true}
)

// 滚动高亮
const selectedHourRef = ref<HTMLElement | null>(null)
const selectedMinuteRef = ref<HTMLElement | null>(null)
const selectedSecondRef = ref<HTMLElement | null>(null)
const rangeHourRefs = [ref<HTMLElement | null>(null), ref<HTMLElement | null>(null)]
const rangeMinuteRefs = [ref<HTMLElement | null>(null), ref<HTMLElement | null>(null)]
const rangeSecondRefs = [ref<HTMLElement | null>(null), ref<HTMLElement | null>(null)]

function setSelectedHourRef(el: any, h: string, idx?: 0 | 1) {
  if (props.type === 'timerange' && idx !== undefined) {
    if (h == rangeSplit.value[idx].hour && el) rangeHourRefs[idx].value = el
  } else if (h == selectedSplit.value.hour && el) {
    selectedHourRef.value = el
  }
}

function setSelectedMinuteRef(el: any, m: string, idx?: 0 | 1) {
  if (props.type === 'timerange' && idx !== undefined) {
    if (m == rangeSplit.value[idx].minute && el) rangeMinuteRefs[idx].value = el
  } else if (m == selectedSplit.value.minute && el) {
    selectedMinuteRef.value = el
  }
}

function setSelectedSecondRef(el: any, s: string, idx?: 0 | 1) {
  if (props.type === 'timerange' && idx !== undefined) {
    if (s == rangeSplit.value[idx].second && el) rangeSecondRefs[idx].value = el
  } else if (s == selectedSplit.value.second && el) {
    selectedSecondRef.value = el
  }
}

function scrollToSelected() {
  nextTick(() => {
    if (props.type === 'timerange') {
      rangeHourRefs[0].value?.scrollIntoView({block: 'center', behavior: 'auto'})
      rangeMinuteRefs[0].value?.scrollIntoView({block: 'center', behavior: 'auto'})
      showSecond.value && rangeSecondRefs[0].value?.scrollIntoView({block: 'center', behavior: 'auto'})
      rangeHourRefs[1].value?.scrollIntoView({block: 'center', behavior: 'auto'})
      rangeMinuteRefs[1].value?.scrollIntoView({block: 'center', behavior: 'auto'})
      showSecond.value && rangeSecondRefs[1].value?.scrollIntoView({block: 'center', behavior: 'auto'})
    } else {
      selectedHourRef.value?.scrollIntoView({block: 'center', behavior: 'auto'})
      selectedMinuteRef.value?.scrollIntoView({block: 'center', behavior: 'auto'})
      showSecond.value && selectedSecondRef.value?.scrollIntoView({block: 'center', behavior: 'auto'})
    }
  })
}

onMounted(scrollToSelected)
watch([selected, () => range.value[0], () => range.value[1]], scrollToSelected, {immediate: true})

// 选择
function selectTime(hour: string, minute: string, second?: string, idx?: 0 | 1) {
  const time = showSecond.value
      ? `${hour}:${minute}:${second ?? '00'}`
      : `${hour}:${minute}`
  if (props.type === 'timerange' && idx !== undefined) {
    range.value[idx] = time
    emit('select', [...range.value])
  } else {
    selected.value = time
    emit('select', time)
  }
}

// 高亮
function isSelected(
    hour: string,
    minute: string,
    second?: string,
    idx?: 0 | 1
) {
  const val = showSecond.value
      ? `${hour}:${minute}:${second ?? '00'}`
      : `${hour}:${minute}`
  if (props.type === 'timerange' && idx !== undefined) {
    return range.value[idx] === val
  }
  return selected.value === val
}

defineExpose({scrollToSelected})
</script>

<template>
  <div class="w-max p-3 shadow rounded bg-background" @click="emit('click', $event)">
    <template v-if="props.type === 'timerange'">
      <div class="flex gap-8">
        <!-- 开始时间 -->
        <div class="flex flex-col items-center">
          <div class="text-text-primary font-bold text-sm">开始时间</div>
          <div class="flex">
            <div class="h-48 overflow-y-auto text-xs">
              <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
              <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
              <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
              <div
                  v-for="h in hours"
                  :key="'start-h-' + h"
                  :ref="el => setSelectedHourRef(el, h, 0)"
                  class="cursor-pointer px-3 py-1 rounded text-center text-default select-none"
                  :class="isSelected(h, rangeSplit[0].minute, showSecond ? rangeSplit[0].second : undefined, 0)
                  ? 'bg-primary text-white'
                  : 'hover:bg-primary-light-8'"
                  @click="selectTime(h, rangeSplit[0].minute, showSecond ? rangeSplit[0].second : undefined, 0)"
              >
                <span>{{ h }}</span>
              </div>
              <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
              <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
              <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
            </div>
            <span class="mx-1 text-text-regular text-lg flex items-center">:</span>
            <div class="h-48 overflow-y-auto text-xs">
              <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
              <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
              <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
              <div
                  v-for="m in minutes"
                  :key="'start-m-' + m"
                  :ref="el => setSelectedMinuteRef(el, m, 0)"
                  class="cursor-pointer px-3 py-1 rounded text-center text-default select-none"
                  :class="isSelected(rangeSplit[0].hour, m, showSecond ? rangeSplit[0].second : undefined, 0)
                  ? 'bg-primary text-white'
                  : 'hover:bg-primary-light-8'"
                  @click="selectTime(rangeSplit[0].hour, m, showSecond ? rangeSplit[0].second : undefined, 0)"
              >{{ m }}
              </div>
              <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
              <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
              <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
            </div>
            <template v-if="showSecond">
              <span class="mx-1 text-text-regular text-lg flex items-center">:</span>
              <div class="h-48 overflow-y-auto">
                <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
                <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
                <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
                <div
                    v-for="s in seconds"
                    :key="'start-s-' + s"
                    :ref="el => setSelectedSecondRef(el, s, 0)"
                    class="cursor-pointer px-3 py-1 rounded text-center text-default select-none"
                    :class="isSelected(rangeSplit[0].hour, rangeSplit[0].minute, s, 0)
                    ? 'bg-primary text-white'
                    : 'hover:bg-primary-light-8'"
                    @click="selectTime(rangeSplit[0].hour, rangeSplit[0].minute, s, 0)"
                >{{ s }}
                </div>
                <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
                <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
                <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
              </div>
            </template>
          </div>
        </div>
        <!-- 结束时间 -->
        <div class="flex flex-col items-center">
          <div class="text-text-primary font-bold text-sm">结束时间</div>
          <div class="flex">
            <div class="h-48 overflow-y-auto">
              <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
              <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
              <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
              <div
                  v-for="h in hours"
                  :key="'end-h-' + h"
                  :ref="el => setSelectedHourRef(el, h, 1)"
                  class="cursor-pointer px-3 py-1 rounded text-center text-default select-none"
                  :class="isSelected(h, rangeSplit[1].minute, showSecond ? rangeSplit[1].second : undefined, 1)
                  ? 'bg-primary text-white'
                  : 'hover:bg-primary-light-8'"
                  @click="selectTime(h, rangeSplit[1].minute, showSecond ? rangeSplit[1].second : undefined, 1)"
              >{{ h }}
              </div>
              <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
              <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
              <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
            </div>
            <span class="mx-1 text-text-regular text-lg flex items-center">:</span>
            <div class="h-48 overflow-y-auto">
              <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
              <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
              <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
              <div
                  v-for="m in minutes"
                  :key="'end-m-' + m"
                  :ref="el => setSelectedMinuteRef(el, m, 1)"
                  class="cursor-pointer px-3 py-1 rounded text-center text-default select-none"
                  :class="isSelected(rangeSplit[1].hour, m, showSecond ? rangeSplit[1].second : undefined, 1)
                  ? 'bg-primary text-white'
                  : 'hover:bg-primary-light-8'"
                  @click="selectTime(rangeSplit[1].hour, m, showSecond ? rangeSplit[1].second : undefined, 1)"
              >{{ m }}
              </div>
              <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
              <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
              <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
            </div>
            <template v-if="showSecond">
              <span class="mx-1 text-text-regular text-lg flex items-center">:</span>
              <div class="h-48 overflow-y-auto">
                <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
                <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
                <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
                <div
                    v-for="s in seconds"
                    :key="'end-s-' + s"
                    :ref="el => setSelectedSecondRef(el, s, 1)"
                    class="cursor-pointer px-3 py-1 rounded text-center text-default select-none"
                    :class="isSelected(rangeSplit[1].hour, rangeSplit[1].minute, s, 1)
                    ? 'bg-primary text-white'
                    : 'hover:bg-primary-light-8'"
                    @click="selectTime(rangeSplit[1].hour, rangeSplit[1].minute, s, 1)"
                >{{ s }}
                </div>
                <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
                <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
                <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
              </div>
            </template>
          </div>
        </div>
      </div>
    </template>
    <template v-else>
      <div class="flex flex-col items-center">
        <div class="text-text-primary font-bold text-sm mb-2">选择时间</div>
        <div class="flex">
          <div class="h-48 overflow-y-auto text-xs">
            <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
            <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
            <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
            <div
                v-for="h in hours"
                :key="'single-h-' + h"
                :ref="el => setSelectedHourRef(el, h)"
                class="cursor-pointer px-3 py-1 rounded text-center text-default select-none"
                :class="isSelected(h, selectedSplit.minute, showSecond ? selectedSplit.second : undefined)
                ? 'bg-primary text-white'
                : 'hover:bg-primary-light-8'"
                @click="selectTime(h, selectedSplit.minute, showSecond ? selectedSplit.second : undefined)"
            >{{ h }}
            </div>
            <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
            <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
            <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
          </div>

          <span class="mx-1 text-text-regular text-lg flex items-center">:</span>
          <div class="h-48 overflow-y-auto text-xs">
            <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
            <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
            <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
            <div
                v-for="m in minutes"
                :key="'single-m-' + m"
                :ref="el => setSelectedMinuteRef(el, m)"
                class="cursor-pointer px-3 py-1 rounded text-center text-default select-none"
                :class="isSelected(selectedSplit.hour, m, showSecond ? selectedSplit.second : undefined)
                ? 'bg-primary text-white'
                : 'hover:bg-primary-light-8'"
                @click="selectTime(selectedSplit.hour, m, showSecond ? selectedSplit.second : undefined)"
            >{{ m }}
            </div>
            <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
            <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
            <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
          </div>
          <template v-if="showSecond">
            <span class="mx-1 text-text-regular text-lg flex items-center select-none">:</span>
            <div class="h-48 overflow-y-auto text-xs">
              <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
              <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
              <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
              <div
                  v-for="s in seconds"
                  :key="'single-s-' + s"
                  :ref="el => setSelectedSecondRef(el, s)"
                  class="cursor-pointer px-3 py-1 rounded text-center text-default select-none"
                  :class="isSelected(selectedSplit.hour, selectedSplit.minute, s)
                  ? 'bg-primary text-white'
                  : 'hover:bg-primary-light-8'"
                  @click="selectTime(selectedSplit.hour, selectedSplit.minute, s)"
              >{{ s }}
              </div>
              <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
              <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
              <div class="px-3 py-1 rounded text-center text-default select-none"> &nbsp;</div>
            </div>
          </template>
        </div>
      </div>
    </template>
  </div>
</template>
