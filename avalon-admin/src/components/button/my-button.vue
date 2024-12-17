<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import './my-button.css'
import MyIcon from "../icon/my-icon.vue";
import {btnActionType, btnType} from "./my-button.ts";
import {iconStyleType} from "../icon/my-icon.ts";
import {ref} from "vue";

const props = defineProps({
    type: {
        type: btnType,
        default: 'primary'
    },
    rounded: Boolean,
    isLink: Boolean,
    icon: String,
    iconColor: {
        type: String,
        default: ""
    },
    iconStyle: {
        type: iconStyleType,
        default: 'far'
    },
    action: {
        type: String,
        default: ''
    },
    actionType: {
        type: btnActionType,
        default: 'action'
    }
})

const emit = defineEmits(['click'])


const btn_click = () => {
    emit('click', props.actionType, props.action)
}

const iconColorInner = ref('var(--color-primary)')

if (props.type == 'success') {
    iconColorInner.value = 'var(--color-success)'
} else if (props.type == 'warning') {
    iconColorInner.value = 'var(--color-warning)'
} else if (props.type == 'danger') {
    iconColorInner.value = 'var(--color-danger)'
} else if (props.type == 'info') {
    iconColorInner.value = 'var(--color-info)'
}

</script>

<template>
    <button
        :class="['select-none','bg-primary',{'btn':true, [`btn-${type}`]:true, 'btn-rounded':rounded, 'btn-link':isLink}]"
        :link="isLink"
        @click="btn_click">
        <my-icon v-if="icon" :type="iconStyle" :icon="icon" :color="iconColor ? iconColor : iconColorInner"
                 class="mr-1"></my-icon>
        <slot></slot>
    </button>
</template>

<style scoped>

</style>