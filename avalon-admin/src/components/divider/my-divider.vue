<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import './my-divider.css'

defineProps({
    margin: {
        type: String,
        default: '0'
    },
    vertical: Boolean,
    height: {
        type: String,
        default: 'auto'
    }
})
</script>

<template>
    <div :class="[vertical ? 'my-divider-vertical' : 'my-divider']" :style="{margin, height}"></div>
</template>

<style scoped>

</style>