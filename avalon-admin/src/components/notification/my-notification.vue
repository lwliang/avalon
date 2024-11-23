<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import './my-notification.css'
import {notificationType} from "./my-notification.ts";
import {computed, watch} from "vue";
import MyIcon from "../icon/my-icon.vue";

const emit = defineEmits(['remove'])

const props = defineProps({
    type: {
        type: notificationType,
        default: 'info'
    },
    title: String,
    content: String,
    duration: {
        type: Number,
        default: 5000
    }
})
let timer: any = null
watch(() => props.duration, (newValue) => {
    if (newValue) {
        timer = setTimeout(() => {
            timer = null;
            emit('remove')
        }, newValue)
    }
}, {
    immediate: true
})

const icon = computed(() => {
    switch (props.type) {
        case 'info':
            return 'circle-info'
        case 'success':
            return 'circle-check'
        case 'warning':
            return 'circle-exclamation'
        case 'error':
            return 'circle-xmark'
        default:
            return 'info-circle'
    }
})
const iconColor = computed(() => {
    switch (props.type) {
        case 'info':
            return 'var(--info-color)'
        case 'success':
            return 'var(--success-color)'
        case 'warning':
            return 'var(--warning-color)'
        case 'error':
            return 'var(--danger-color)'
        default:
            return 'var(--success-color)'
    }
})

const closeClick = () => {
    if(timer) {
        clearTimeout(timer);
        timer = null;
    }
    emit('remove')
}
</script>

<template>
    <div class="my-notification">
        <div>
            <my-icon type="fas" :icon="icon" :color="iconColor" size="lg"></my-icon>
        </div>
        <div class="flex-1 px-4">
            <div>
                {{ title }}
            </div>
            <div>
                {{ content }}
            </div>
        </div>
        <div>
            <my-icon class="cursor-pointer" icon="xmark" type="fas" @click="closeClick"></my-icon>
        </div>
    </div>
</template>

<style scoped>

</style>