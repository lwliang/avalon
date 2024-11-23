<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22 12:30
 */

const total = defineModel('total', {type: Number, default: 0})
const begin = defineModel('begin', {type: Number, default: 0})
const end = defineModel('end', {type: Number, default: 0})
const emit = defineEmits(['pageChange'])

const pageChangeClick = (dir: string) => {
    if (dir == 'left') {
        if (begin.value > 1) {
            emit('pageChange', dir)
        }
    } else {
        if (end.value < total.value) {
            emit('pageChange', dir)
        }
    }
}


</script>

<template>
    <div class="flex items-center gap-1">
        <div class="flex items-center">
            <div style="color: #374151">{{ begin }}</div>
            <div style="color: #374151">-</div>
            <div style="color: #374151">{{ end }}</div>
        </div>
        <div style="color: #374151">/</div>
        <div style="color: #374151">{{ total }}</div>
        <div @click="pageChangeClick('left')" :class="['bgc px-3 py-0.5 rounded',{'cursor-pointer':begin > 1}]">
            <MyIcon type="fas" icon="angle-left" color="#AAA"></MyIcon>
        </div>
        <div @click="pageChangeClick('right')" :class="['bgc px-3 py-0.5 rounded',{'cursor-pointer':end < total}]">
            <MyIcon type="fas" icon="angle-right" color="#AAA"></MyIcon>
        </div>
    </div>
</template>

<style scoped>

</style>