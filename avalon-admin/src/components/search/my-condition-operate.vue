<script setup lang="ts">
import Field from "../../model/Field.ts";
import { ref } from "vue";

const props = withDefaults(defineProps<{
  field?: Field,
  customStyle?: string,
  teleported?: boolean
}>(), {
  teleported: true
})

const emit = defineEmits<{
  (e: 'click', event: MouseEvent): void
}>()

const formField = defineModel<string>({
  default: ''
})

const options = ref([
  { value: '=', label: '等于' },
  { value: '>', label: '大于' },
  { value: '<', label: '小于' },
  { value: '>=', label: '大于等于' },
  { value: '<=', label: '小于等于' },
  { value: '!=', label: '不等于' },
  { value: 'like', label: '包含' },
  { value: 'notLike', label: '不包含' },
  { value: 'between', label: '介于' },
  { value: 'in', label: '在列表中' },
  { value: 'notIn', label: '不在列表中' }
])

const handleChange = (value: string) => {
  formField.value = value
  emit('click', new MouseEvent('click'))
}
</script>

<template>
  <el-select
    v-model="formField"
    placeholder="选择操作符"
    clearable
    :teleported="teleported"
    @change="handleChange"
    :style="customStyle"
  >
    <el-option
      v-for="option in options"
      :key="option.value"
      :label="option.label"
      :value="option.value"
    >
      <div class="flex items-center h-full">
        <span class="text-sm">{{ option.label }}</span>
        <span class="ml-2 text-xs text-gray-500">({{ option.value }})</span>
      </div>
    </el-option>
  </el-select>
</template>

<style scoped>
.el-select {
  width: 100%;
}
</style>