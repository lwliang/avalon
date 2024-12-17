<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import './my-col.css'
</script>

<template>
    <div class="w-full 2xl:flex-1 xl:w-1/3 lg:w-1/2 sm:w-full grid grid-cols-[fit-content(150px)_1fr] px-2 gap-2">
        <slot></slot>
    </div>
</template>

<style scoped>

</style>