<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
const props = defineProps<{ label: string }>()
const emit = defineEmits(['itemClick'])

const itemClick = (label: string) => {
    emit('itemClick', label)
}
</script>

<template>
    <div class="cursor-pointer box-border whitespace-nowrap  hover:bg-gray-200 px-4"
         @click="itemClick(label)">
        {{ label }}
    </div>
</template>

<style scoped>

</style>