<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2025/07/28
 */
import { ref, inject, getCurrentInstance, onMounted } from "vue";
import { invokeMethod } from "../../api/modelApi.ts";
import ServiceInvokeParam from "../../model/ServiceInvokeParam.ts";

const props = defineProps<{
  height?: string;
  serviceName: string;
  action?: string;
}>();
const registerComponent = inject("registerReportComponent") as (
  id: string,
  instance: any
) => void;
const instance = getCurrentInstance();

const loading = ref(false);
const loadData = (condition: any) => {
  loading.value = true;
  invokeMethod(props.serviceName, {
    serviceName: props.serviceName,
    method: props.action || 'getReportTable',
    param: condition,
  } as ServiceInvokeParam)
    .then((reportTable) => {
      record.value = reportTable.data;
      visibleFields.value = reportTable.columns;
    })
    .finally(() => {
      loading.value = false;
    });
};

onMounted(() => {
  if (registerComponent && instance) {
    registerComponent(instance.uid.toString(), {loadData});
  }
});

const record = ref<any[]>([]);
const visibleFields = ref<any[]>([]);
defineExpose({
  loadData,
});
</script>

<template>
  <div class="w-full h-full" v-loading="loading">
    <el-table
      :data="record"
      :height="height || '100%'"
      stripe
      border
      :show-header="true"
      class="w-full"
    >
      <!-- 数据列 -->
      <el-table-column
        v-for="(field, colIndex) in visibleFields"
        :key="colIndex"
        :prop="field.name"
        :label="field.label"
        :min-width="120"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          <div class="w-full h-full">
            {{ row[field.name] }}
          </div>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<style scoped></style>
