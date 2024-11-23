<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import './my-overlay.css'

const props = defineProps({
    zIndex: {
        type: [String, Number],
        default: 1000
    }
})
</script>

<template>
    <teleport to="body">
        <div class="overlay" :style="{zIndex}">
            <slot></slot>
        </div>
    </teleport>
</template>

<style scoped>

</style>