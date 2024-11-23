<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {ref, watch} from 'vue';
import {arrow, autoUpdate, useFloating, offset, flip, shift, computePosition} from '@floating-ui/vue';
import './my-popover.css'
import {onClickOutside} from '@vueuse/core'
import {optionType, popoverTrigger, popoverType} from "./my-popover.ts";
import {onMounted} from "@vue/runtime-dom";


const props = defineProps({
    placement: {
        type: popoverType,
        default: 'bottom'
    },
    trigger: {
        type: popoverTrigger,
        default: 'hover'
    },
    content: String,
    option: optionType,
    fullWidth: Boolean,
    arrowShow: {
        type: Boolean,
        default: true
    },
    width: String
})

const emit = defineEmits(['itemSelect', 'popperShow', 'popperHide'])
const popper_container = ref(null)
onClickOutside(popper_container, () => {
    if (props.trigger === 'click') {
        show.value = false
    }
})


const reference = ref<any>(null);
const floating = ref(null);
const floatingArrow = ref(null);
const show = ref(false)
const {floatingStyles, middlewareData, placement} = useFloating(reference, floating, {
    placement: props.placement,
    middleware: [offset(8), flip(), shift(), arrow({element: floatingArrow})],
});

const dynamicStyles = {
    width: props.width || undefined
}

let time: any = null;

watch(() => show.value, (newValue) => {
    if (newValue) {
        emit('popperShow')
    } else {
        emit('popperHide')
    }
})

const showPopper = () => {
    if (props.trigger === 'click') {
        show.value = !show.value
    }
}

const showMousePopper = () => {
    if (props.trigger === 'hover') {
        if (time) {
            clearTimeout(time);
            time = null;
        }
        show.value = true
    }
}

const hideMousePopper = () => {
    if (props.trigger === 'hover') {
        time = setTimeout(() => {
            show.value = false
        }, 300)
    }
}

const toggleShow = () => {
    if (props.trigger === 'click') {
        if (!show.value)
            show.value = true
    }
}

const optionClick = () => {
    show.value = false
}

const toggleHide = () => {
    if (show.value)
        show.value = false
}

const itemSelectClick = (key: string) => {
    emit('itemSelect', key)
    showPopper();
}

defineExpose({
    show: toggleShow,
    hide: toggleHide
})

</script>

<template>
    <div ref="popper_container" :class="['inline-block', 'relative',{'w-full':fullWidth}]">
        <div :class="['inline-block',{'w-full':fullWidth}]" ref="reference" @click="showPopper"
             @mouseenter="showMousePopper"
             @mouseleave="hideMousePopper">
            <slot></slot>
        </div>
        <div v-if="show"
             :class="{popover:true,'w-full':fullWidth,'popover-p':!fullWidth,'popover-full-p':fullWidth}"
             ref="floating"
             :style="[dynamicStyles,floatingStyles]"
             @mouseenter="showMousePopper"
             @mouseleave="hideMousePopper">
            <div class="flex justify-center items-center flex-wrap">
                <template v-if="$slots.option">
                    <div class="w-full" @click="optionClick">
                        <slot name="option"></slot>
                    </div>
                </template>
                <template v-if="option">
                    <div class="w-full cursor-pointer hover:bg-gray-300 px-4" v-for="(value,key) in option"
                         :key="key"
                         @click="itemSelectClick(key)">
                        {{ value }}
                    </div>
                </template>
                <template v-if="content">
                    {{ content }}
                </template>
            </div>
            <div class="flex justify-end">
                <slot name="footer"></slot>
            </div>
            <div v-if="arrowShow" :class="{'arrow':true,'border-t':!placement.startsWith('top'),
            'border-l':!placement.startsWith('top'),
            'border-b':placement.startsWith('top'),
            'border-r':placement.startsWith('top')}"
                 ref="floatingArrow"
                 :style="{ position: 'absolute',
                left:  middlewareData.arrow?.x != null ? `${middlewareData.arrow.x}px` : '',
                top:  middlewareData.arrow?.y != null ? `${middlewareData.arrow.y}px`: '',
                [placement.startsWith('top') ? 'bottom' : 'top']:'-4px'}"
            ></div>
        </div>
    </div>

</template>

<style scoped>

</style>