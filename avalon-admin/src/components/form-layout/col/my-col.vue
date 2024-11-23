<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import './my-col.css'
</script>

<template>
    <div class="w-[50%] grid grid-cols-[fit-content(150px)_1fr] px-2 gap-2">
        <slot></slot>
    </div>
</template>

<style scoped>

</style>