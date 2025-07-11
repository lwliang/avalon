<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/13 9:28
 */

import { ref, computed } from "vue";
import Field from "../../model/Field.ts";
import { useFieldStore } from "../../global/store/fieldStore.ts";

const props = defineProps({
  serviceName: String,
})

const emits = defineEmits(['selectField'])

const formField = defineModel<string>({
  default: ''
})

const fields = ref<Field[]>([])
const selectedField = ref<Field | null>(null)

// 加载字段数据
const loadFields = async () => {
  if (props.serviceName) {
    const fieldStore = useFieldStore()
    const data = await fieldStore.getFieldByServiceNameAsync(props.serviceName)
    fields.value = data
  }
}

loadFields()

// 将字段转换为树形结构
const treeData = computed(() => {
  return fields.value.map(field => ({
    id: field.name,
    label: field.label || field.name,
    name: field.name,
    type: field.type,
    children: []
  }))
})

// 处理字段选择
const handleFieldSelect = (fieldName: string) => {
  const field = fields.value.find(f => f.name === fieldName)
  if (field) {
    selectedField.value = field
    formField.value = field.name
    emits('selectField', field)
  }
}

// 处理树节点点击
const handleNodeClick = (data: any) => {
  handleFieldSelect(data.name)
}
</script>

<template>
  <el-select
    v-model="formField"
    placeholder="请选择字段"
    clearable
    filterable
    @change="handleFieldSelect"
    style="width:240px"
  >
    <el-option
      v-for="field in fields"
      :key="field.name"
      :label="field.label || field.name"
      :value="field.name"
    >
      <div class="flex items-center h-full">
        <span class="text-sm">{{ field.label}}</span>
        <span class="ml-2 text-xs text-gray-500">({{ field.name }})</span>
      </div>
    </el-option>
  </el-select>
</template>

<style scoped>
.el-select {
  width: 100%;
}
</style>