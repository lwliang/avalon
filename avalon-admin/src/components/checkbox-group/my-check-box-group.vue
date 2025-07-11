<script setup lang="ts">
import { computed } from 'vue'
import type { CheckboxGroupProps } from './types'

const props = defineProps<CheckboxGroupProps>()
const checkboxValues = defineModel<string[] | number[]>({ required: true })

const emit = defineEmits<{
  (e: 'valueChange', value: any): void
}>()

// 处理值变化
const handleChange = (value: any[]) => {
  checkboxValues.value = value
  emit('valueChange', value)
}
</script>

<template>
  <div>
    <label v-if="props.label" class="block mb-2 text-text-regular">{{ props.label }}</label>
    <el-checkbox-group
      v-model="checkboxValues"
      :size="props.size"
      :disabled="props.disabled"
      @change="handleChange"
    >
      <slot />
    </el-checkbox-group>
  </div>
</template>
