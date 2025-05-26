<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

defineOptions({name: 'TabPanel'})

// 类型声明
const props = defineProps<{
  label: string
  name: string | number
  disabled?: boolean
}>()

</script>

<template>
  <div
      :aria-disabled="props.disabled ? 'true' : undefined"
      style="display: contents;"
  >
    <slot/>
  </div>
</template>
