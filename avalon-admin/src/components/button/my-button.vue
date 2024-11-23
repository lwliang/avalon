<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import './my-button.css'
import MyIcon from "../icon/my-icon.vue";
import {btnActionType, btnType} from "./my-button.ts";
import {iconStyleType} from "../icon/my-icon.ts";

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
        default: "#FFF"
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

</script>

<template>
    <button :class="['select-none',{'btn':true, [`btn-${type}`]:true, 'btn-rounded':rounded, 'btn-link':isLink}]"
            :link="isLink"
            @click="btn_click">
        <my-icon v-if="icon" :type="iconStyle" :icon="icon" :color="iconColor" class="mr-1"></my-icon>
        <slot></slot>
    </button>
</template>

<style scoped>

</style>