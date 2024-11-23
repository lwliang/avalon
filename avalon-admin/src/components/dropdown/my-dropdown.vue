<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {popoverTrigger} from "../popover/my-popover.ts";

const props = defineProps({
    trigger: {
        type: popoverTrigger,
        default: 'hover'
    },
})
import MyPopover from "../popover/my-popover.vue";
</script>

<template>
    <MyPopover ref="popper" placement="bottom" :trigger="trigger">
        <template #default>
            <div class="inline-flex cursor-pointer">
                <slot name="default"></slot>
            </div>

        </template>

        <template #option>
            <div class="dropdown-item flex flex-col">
                <slot name="dropdown"></slot>
            </div>
        </template>
    </MyPopover>
</template>

<style scoped>

</style>