<script setup lang="ts">
/**
 * @Author lwlianghehe@gmail.com
 * @Date 2024-11-21 8:50
 */
import MyPopover from "../popover/my-popover.vue";
import MyInput from "../input/my-input.vue";
import {
    getDaysInMonth,
    getYear,
    getMonth,
    getDay,
    getFirstDayOfMonth,
    getTime,
    getDateTime, getDate
} from "../../util/dateUtils.ts";
import FormField from "../../model/FormField.ts";
import {ref, watch} from "vue";

const props = defineProps({
    htmlId: String,
    htmlName: String,
    required: Boolean,
    readonly: Boolean
})

const formField = defineModel({
    type: FormField,
    required: true
})
const timeFormField = ref(new FormField(''))
if (formField.value.value) {
    timeFormField.value.value = getTime(formField.value.value)
    formField.value.value = getDateTime(formField.value.value)
}

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
const selectDate = ref()
const editMode = ref<"year" | "month" | "day">('day') // 编辑模式

const days = ref<Array<Number>[]>([])
const weeks = ref<string[]>(['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'])

const reComputeDay = () => {
    const dateStr = `${year.value}-${month.value}-${day.value}`
    const daySum = getDaysInMonth(dateStr)
    let dayCount = 0;
    for (let i = 0; i < Math.ceil(daySum / 7); i++) { // 获取最大周数
        const weekDays = [];
        let maxWeeks = 7; // 生成最大周数
        let week = 0; // 开始的周
        if (i == 0) { // 第一周
            week = getFirstDayOfMonth(dateStr)
        }
        for (let j = week; j < maxWeeks; j++) {
            dayCount++;
            if (dayCount > daySum) {
                break;
            }
            weekDays[j] = dayCount;
        }
        days.value.push(weekDays);
    }
}


year.value = getYear(formField.value.value)
month.value = getMonth(formField.value.value)
day.value = getDay(formField.value.value)
selectDate.value = getDate(formField.value.value)
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

const setDate = (dayTemp: Number) => {
    day.value = dayTemp
    selectDate.value = `${year.value}-${month.value}-${day.value}`
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

const sureDatetimeClick = () => {
    formField.value.value = `${year.value}-${month.value}-${day.value} ${timeFormField.value.value}`
}

const datetimeChange = (datetime: string) => {
    if (datetime) {
        const regex = /^\d{4}-\d{2}-\d{2} \d{2}:\d{2}$/
        if (!regex.test(datetime)) {
            formField.value.value = getDateTime(datetime)
        }
    }
}
</script>

<template>
    <MyPopover placement="bottom" trigger="click" width="332px">
        <template #default>
            <MyInput @valueChange="datetimeChange" v-model="formField" suffix-icon-style="far"
                     suffix-icon="calendar"></MyInput>
        </template>

        <template #option>
            <div class="w-full rounded">
                <div class="flex w-full pt-2 pb-4 px-2">
                    <div class="px-2 cursor-pointer date-day" @click.stop="addYear"
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
                    <div class="px-2 cursor-pointer" @click.stop="subYear"
                         @mouseenter="arrowMouseEnter(activeRightRightColor)"
                         @mouseleave="arrowMouseLeave(activeRightRightColor)">
                        <MyIcon type="fas" icon="angles-right" :color="activeRightRightColor.color"></MyIcon>
                    </div>
                </div>
                <table class="w-full table-fixed" @click.stop="void(0)">
                    <tbody>
                    <tr>
                        <th v-for="week in weeks" :key="week">
                            <div class="py-1">{{ week }}</div>
                        </th>
                    </tr>
                    <template v-for="(weekday,index) in days" :key="index">
                        <tr>
                            <th v-for="(day,dayIndex) in weekday" :key="dayIndex">
                                <div class="py-1 cursor-pointer flex justify-center items-center">
                                    <div
                                        @click="setDate(day)"
                                        :class="['date-day' ,'w-[24px]', 'h-[24px]', 'text-xs', 'flex', 'justify-center' ,'items-center',
                                        {'date-day-active': getCurrentDateStr(day) == selectDate}]">
                                        {{ day }}
                                    </div>
                                </div>
                            </th>
                        </tr>
                    </template>
                    </tbody>
                </table>
                <div class="px-4 py-2 flex items-center">
                    <div class="flex-1" @click.stop="void(0)">
                        <MyTime placement="top" input-width="100px" v-model="timeFormField"></MyTime>
                    </div>

                    <div class="flex gap-2 ">
                        <MyButton is-link type="info">Cancel</MyButton>
                        <MyButton is-link @click="sureDatetimeClick">OK</MyButton>
                    </div>

                </div>
            </div>
        </template>
    </MyPopover>
</template>

<style scoped>
.date-day:hover {
    @apply text-blue-500;
}

.date-day-active {
    @apply bg-blue-500;
    color: white !important;
    border-radius: 50%;
}
</style>