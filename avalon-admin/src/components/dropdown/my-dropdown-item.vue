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
  <div class="cursor-pointer box-border whitespace-nowrap  hover:bg-background-component px-4 py-1"
       @click="itemClick(label)">
    <template v-if="$slots.default">
      <slot></slot>
    </template>
    <template v-else>{{ label }}</template>
  </div>
</template>

<style scoped>

</style>