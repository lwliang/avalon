<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import MyPopover from "../popover/my-popover.vue";
import MyInput from "../input/my-input.vue";
import {getHour, getMinute} from "../../util/dateUtils.ts";
import FormField from "../../model/FormField.ts";
import {ref} from "vue";
import {popoverType} from "../popover/my-popover.ts";

const props = defineProps({
    htmlId: String,
    htmlName: String,
    required: Boolean,
    readonly: Boolean,
    inputWidth: String,
    placement: {
        type: popoverType,
        default: 'bottom'
    }
})

const formField = defineModel({
    type: FormField,
    required: true
})

const hours = ref<number[]>([1, 2, 3])
const minutes = ref<number[]>([1, 2, 3])
const seconds = ref<number[]>([1, 2, 3])

const hourUpClick = () => {
    for (let i = 0; i < hours.value.length; i++) {
        hours.value[i] = hours.value[i] - 1;
    }
    if (hours.value[1] < 0) {  // 第二个
        hours.value[0] = 22
        hours.value[1] = 23
        hours.value[2] = 24
    }
}
if (formField.value.value) {
    const hour = parseInt(formField.value.value.split(':')[0])
    const minute = parseInt(formField.value.value.split(':')[1])
    hours.value[0] = hour - 1
    hours.value[1] = hour
    hours.value[2] = hour + 1
    minutes.value[0] = minute - 1
    minutes.value[1] = minute
    minutes.value[2] = minute + 1
} else {
    const hour = getHour()
    const minute = getMinute()
    hours.value[0] = hour - 1
    hours.value[1] = hour
    hours.value[2] = hour + 1
    minutes.value[0] = minute - 1
    minutes.value[1] = minute
    minutes.value[2] = minute + 1
}


const hourDownClick = () => {
    for (let i = 0; i < hours.value.length; i++) {
        hours.value[i] = hours.value[i] + 1;
    }
    if (hours.value[1] > 23) {  // 第二个
        hours.value[0] = -1
        hours.value[1] = 0
        hours.value[2] = 1
    }
}

const minuteUpClick = () => {
    for (let i = 0; i < minutes.value.length; i++) {
        minutes.value[i] = minutes.value[i] - 1;
    }
    if (minutes.value[1] < 0) {  // 第二个
        minutes.value[0] = 58
        minutes.value[1] = 59
        minutes.value[2] = 60
    }
}

const minuteDownClick = () => {
    for (let i = 0; i < minutes.value.length; i++) {
        minutes.value[i] = minutes.value[i] + 1;
    }
    if (minutes.value[1] > 59) {  // 第二个
        minutes.value[0] = -1
        minutes.value[1] = 0
        minutes.value[2] = 1
    }
}
const secondUpClick = () => {
    for (let i = 0; i < seconds.value.length; i++) {
        seconds.value[i] = seconds.value[i] - 1;
    }
    if (seconds.value[1] < 0) {  // 第二个
        seconds.value[0] = 58
        seconds.value[1] = 59
        seconds.value[2] = 60
    }
}

const secondDownClick = () => {
    for (let i = 0; i < seconds.value.length; i++) {
        seconds.value[i] = seconds.value[i] + 1;
    }
    if (seconds.value[1] > 59) {  // 第二个
        seconds.value[0] = -1
        seconds.value[1] = 0
        seconds.value[2] = 1
    }
}

const sureTime = () => {
    formField.value.value = `${hours.value[1].toString().padStart(2, '0')}:${minutes.value[1].toString().padStart(2, '0')}`
}
</script>

<template>
    <MyPopover :placement="placement" trigger="click" width="182px">
        <template #default>
            <MyInput :inputWidth="props.inputWidth" v-model="formField" suffix-icon-style="far"
                     suffix-icon="clock"></MyInput>
        </template>

        <template #option>
            <div class="w-full rounded relative my-time-popper">
                <div class="flex px-4 py-2" @click.stop="void(0)">
                    <div class="flex-1 flex flex-col items-center">
                        <div class="cursor-pointer" @click="hourUpClick">
                            <MyIcon type="fas" icon="angle-up"></MyIcon>
                        </div>
                        <div class="cursor-pointer select-none">
                            <div v-for="(hour,index) in hours" :key="index" class="h-6">
                                {{
                                    hour >= 0 && hour <= 23 ? hour.toString().padStart(2, '0') : '  '
                                }}
                            </div>
                        </div>
                        <div class="cursor-pointer" @click="hourDownClick">
                            <MyIcon type="fas" icon="angle-down"></MyIcon>
                        </div>
                    </div>
                    <div class="flex-1 flex flex-col items-center">
                        <div class="cursor-pointer" @click="minuteUpClick">
                            <MyIcon type="fas" icon="angle-up"></MyIcon>
                        </div>
                        <div class="cursor-pointer select-none">
                            <div v-for="(minute,index) in minutes" :key="index" class="h-6">
                                {{
                                    minute >= 0 && minute <= 59 ? minute.toString().padStart(2, '0') : ' '
                                }}
                            </div>
                        </div>
                        <div class="cursor-pointer" @click="minuteDownClick">
                            <MyIcon type="fas" icon="angle-down"></MyIcon>
                        </div>
                    </div>
                </div>
                <div class="flex justify-end px-4 gap-4 py-2">
                    <div class="flex-1" @click.stop="void (0)"></div>
                    <MyButton type="info" is-link>Cancel</MyButton>
                    <MyButton is-link @click="sureTime">OK</MyButton>
                </div>
            </div>
        </template>
    </MyPopover>
</template>

<style scoped>
.my-time-popper::before {
    content: ' ';
    display: block;
    position: absolute;
    border: 1px solid #F4F4F5;
    width: 50%;
    left: 50%;
    top: 56px;
    transform: translateX(-50%);
}

.my-time-popper::after {
    content: ' ';
    display: block;
    position: absolute;
    border: 1px solid #F4F4F5;
    width: 50%;
    left: 50%;
    top: 79px;
    transform: translateX(-50%);
}
</style>