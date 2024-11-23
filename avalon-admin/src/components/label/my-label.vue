<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import './my-label.css'

defineProps({
    htmlFor: String,
    center: {
        type: Boolean,
        default: false
    }
})
</script>

<template>
    <label :class="['leading-[34px]',{'flex':center, 'justify-center':center,'justify-start':!center }]" :for="htmlFor">
        <slot></slot>
    </label>
</template>

<style scoped>

</style>