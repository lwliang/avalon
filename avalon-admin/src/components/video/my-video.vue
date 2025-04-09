<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

const props = defineProps({
    src: String,
    width: String,
    height: String,
})

</script>

<template>
    <view class="w-[94px] h-[94px] bgc flex justify-center items-center rounded cursor-pointer overflow-hidden">
        <video :src="src" :width="width" :height="height"/>
    </view>
</template>

<style scoped>

</style>