<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
const props = defineProps<{ label: String, value: null, round?: Boolean, close?: Boolean }>()

const emit = defineEmits(['tagClick', 'deleteTag'])

const tagClick = () => {
    emit('tagClick', props.value)
}
const deleteTagClick = () => {
    emit('deleteTag', props.value)
}
</script>

<template>
    <div :class="['inline-flex justify-center items-center relative bgc rounded px-2 ',{'rounded-full': round}]"
         @click="tagClick">
        <template v-if="$slots.default">
            <slot name="default"></slot>
        </template>
        <template v-else>
            <div class="text-sm whitespace-nowrap mr-1">{{ label }}</div>
        </template>
        <div v-if="close" class="cursor-pointer" @click.stop="deleteTagClick">
            <MyIcon type="fas" icon="xmark" size="sm"></MyIcon>
        </div>
    </div>
</template>

<style scoped>

</style>