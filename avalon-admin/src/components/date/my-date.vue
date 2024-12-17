<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import MyInput from "../input/my-input.vue";
import {
    getDaysInMonth,
    getYear,
    getMonth,
    getDay,
    getFirstDayOfMonth,
    getBeforeMonth,
    getNextMonth
} from "../../util/dateUtils.ts";
import FormField from "../../model/FormField.ts";
import {ref} from "vue";
import {borderStyleType} from "../icon/my-icon.ts";
import MyInnerPopover from "../popover/my-inner-popover.vue";

interface monthDay {
    year: number
    month: number,
    day: number
}

const props = defineProps({
    htmlId: String,
    htmlName: String,
    required: Boolean,
    readonly: Boolean,
    border: {
        type: borderStyleType,
        default: 'round'
    }
})

const formField = defineModel({
    type: FormField,
    required: true
})

const activeLeftLeftColor = ref({color: '#222222'})
const activeLeftColor = ref({color: '#222222'})
const activeRightRightColor = ref({color: '#222222'})
const activeRightColor = ref({color: '#222222'}) //
const arrowMouseEnter = (color: any) => {
    color.color = 'rgb(59 130 246)'
}
const arrowMouseLeave = (color: any) => {
    color.color = '#222222'
}
const year = ref()
const month = ref()
const day = ref()
const editMode = ref<"year" | "month" | "day">('day') // 编辑模式

const days = ref<Array<monthDay>[]>([])
const weeks = ref<string[]>(['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'])

const reComputeDay = () => {
    const dateStr = `${year.value}-${month.value}-${day.value}`
    const daySum = getDaysInMonth(dateStr)
    let dayCount = 0;
    for (let i = 0; i < Math.ceil(daySum / 7); i++) { // 获取最大周数
        const weekDays: monthDay[] = [];
        let maxWeeks = 7; // 生成最大周数
        let week = 0; // 开始的周
        if (i == 0) { // 第一周
            week = getFirstDayOfMonth(dateStr)
            const beforeDateStr = getBeforeMonth(dateStr);
            const beforeMonth = getMonth(beforeDateStr);
            const beforeYear = getYear(beforeDateStr);
            let beforeDaySum = getDaysInMonth(beforeDateStr);
            for (let j = 0; j < week; j++) {
                weekDays[week - j - 1] = {year: beforeYear, month: beforeMonth, day: beforeDaySum};
                beforeDaySum--;
            }
        }
        let lastI = 0;
        for (let j = week; j < maxWeeks; j++) {
            dayCount++;
            lastI = j;
            if (dayCount > daySum) {
                break;
            }
            weekDays[j] = {year: year.value, month: month.value, day: dayCount};
        }

        if (i == Math.ceil(daySum / 7) - 1) { // 最后一周
            const nextDateStr = getNextMonth(dateStr);
            const nextMonth = getMonth(nextDateStr);
            const nextYear = getYear(nextDateStr);
            let nextDaySum = 1

            for (let j = lastI; j < maxWeeks; j++) {
                weekDays[j] = {year: nextYear, month: nextMonth, day: nextDaySum};
                nextDaySum++;
            }
        }

        days.value.push(weekDays);
    }
}


year.value = getYear(formField.value.value)
month.value = getMonth(formField.value.value)
day.value = getDay(formField.value.value)
reComputeDay()


const monthSub = () => {
    month.value--;
    if (month.value <= 0) {
        year.value--;
        month.value = 12;
    }
    days.value.splice(0, days.value.length)
    reComputeDay();
}

const setDate = (yearTemp: number, monthTemp: number, dayTemp: Number) => {
    day.value = dayTemp
    month.value = monthTemp
    year.value = yearTemp
    formField.value.value = `${year.value}-${month.value}-${day.value}`
    days.value.splice(0, days.value.length)
    reComputeDay();
}

const monthAdd = () => {
    month.value++;
    if (month.value > 12) {
        year.value++;
        month.value = 1;
    }
    days.value.splice(0, days.value.length)
    reComputeDay();
}

const addYear = () => {
    year.value++;
    days.value.splice(0, days.value.length)
    reComputeDay();
}

const subYear = () => {
    year.value--;
    days.value.splice(0, days.value.length)
    reComputeDay();
}

const getCurrentDateStr = (day: any) => {
    return `${year.value}-${month.value}-${day}`
}

</script>

<template>
    <MyInnerPopover placement="bottom" trigger="click" width="332px">
        <template #default>
            <MyInput v-model="formField" suffix-icon-style="far" suffix-icon="calendar" :border="border"></MyInput>
        </template>

        <template #option>
            <div class="w-full rounded">
                <div class="flex w-full pt-2 pb-4 px-2">
                    <div class="px-2 cursor-pointer date-day" @click.stop="subYear"
                         @mouseenter="arrowMouseEnter(activeLeftLeftColor)"
                         @mouseleave="arrowMouseLeave(activeLeftLeftColor)">
                        <MyIcon type="fas" icon="angles-left" :color="activeLeftLeftColor.color"></MyIcon>
                    </div>
                    <div class="px-2 cursor-pointer" @mouseenter="arrowMouseEnter(activeLeftColor)"
                         @click.stop="monthSub"
                         @mouseleave="arrowMouseLeave(activeLeftColor)">
                        <MyIcon type="fas" icon="chevron-left" :color="activeLeftColor.color"></MyIcon>
                    </div>
                    <div class="flex flex-1">
                        <div class="flex-1 text-center cursor-pointer date-day"> {{ year }}</div>
                        <div class="flex-1 text-center cursor-pointer date-day"> {{ month }}</div>
                    </div>

                    <div class="px-2 cursor-pointer" @mouseenter="arrowMouseEnter(activeRightColor)"
                         @mouseleave="arrowMouseLeave(activeRightColor)" @click.stop="monthAdd">
                        <MyIcon type="fas" icon="chevron-right" :color="activeRightColor.color"></MyIcon>
                    </div>
                    <div class="px-2 cursor-pointer" @click.stop="addYear"
                         @mouseenter="arrowMouseEnter(activeRightRightColor)"
                         @mouseleave="arrowMouseLeave(activeRightRightColor)">
                        <MyIcon type="fas" icon="angles-right" :color="activeRightRightColor.color"></MyIcon>
                    </div>
                </div>
                <table class="w-full table-fixed">
                    <tbody>
                    <tr>
                        <th v-for="week in weeks" :key="week">
                            <div class="py-1">{{ week }}</div>
                        </th>
                    </tr>
                    <template v-for="(weekday,index) in days" :key="index">
                        <tr>
                            <th v-for="(dayx,dayIndex) in weekday" :key="dayIndex">
                                <div class="py-1 cursor-pointer flex justify-center items-center">
                                    <div
                                        @click="setDate(dayx.year, dayx?.month, dayx?.day)"
                                        :class="['date-day' ,'w-[24px]', 'h-[24px]', 'text-xs', 'flex', 'justify-center' ,'items-center',
                                        {'date-day-active': dayx.month == month && getCurrentDateStr(dayx?.day) == formField.value,
                                        'date-day-grey': dayx.month != month}]">
                                        {{ dayx?.day }}
                                    </div>
                                </div>
                            </th>
                        </tr>
                    </template>
                    </tbody>
                </table>
            </div>
        </template>
    </MyInnerPopover>
</template>

<style scoped>
.date-day:hover {
    @apply text-primary;
}

.date-day-active {
    @apply bg-primary;
    color: white !important;
    border-radius: 50%;
}

.date-day-grey {
    @apply text-muted;
}
</style>