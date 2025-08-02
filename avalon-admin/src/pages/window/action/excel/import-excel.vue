<script setup lang="ts">/**
 * @author lwlianghehe@gmail.com
 * @date 2024/12/13 15:39
 */
import MyButton from "../../../../components/button/my-button.vue";
import router from "../../../../router";
import {goModelWindow} from "../../../../util/routerUtils.ts";
import {useRoute} from "vue-router";
import {ComponentInternalInstance, getCurrentInstance, ref} from "vue";
import {importExcel, readExcelContent} from "../../../../api/commonApi.ts";

const {proxy} = getCurrentInstance() as ComponentInternalInstance;

const props = defineProps({})
const route = useRoute();

const moduleName = ref<string>(route.params.module as string)
const serviceName = ref<string>(route.params.service as string)
const loading = ref<boolean>(false)
const uploadExcelFile = () => {
  if (file.value) {
    file.value.click();
  }
}
const file = ref<HTMLElement | null>(null)
const cancelFile = () => {
  goModelWindow(moduleName.value, serviceName.value, {})
}
const fileBlob = ref<File>()

const fileChange = (event: any) => {
  fileBlob.value = event.target.files[0]
  loading.value = true
  readExcelContent(serviceName.value, fileBlob.value as File).then(data => {
    excelData.value = data
    loading.value = false
  }).catch(() => {
    loading.value = false
  })
}

const excelData = ref<any>(undefined)

const importExcelFile = () => {
  loading.value = true
  importExcel(serviceName.value, excelData.value).then(data => {
    proxy?.$notify.success({
      title: "提示",
      message: "导入成功",
    });
    loading.value = false
    goModelWindow(moduleName.value, serviceName.value, {})
  }).catch(()=>{
    loading.value = false
  })
}
</script>

<template>
  <div class="p-4 flex flex-col h-full">
    <div class="flex-shrink-0 pb-4">
      <div class="h-[34px] flex items-center">
        <input type="file" hidden="hidden" ref="file" @change="fileChange"
               accept=".csv, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel"/>
        <el-button-group>
          <el-button type="primary" size="small" @click="uploadExcelFile">上传文件</el-button>
          <el-button type="success" size="small" @click="importExcelFile" v-if="excelData">导入</el-button>
          <el-button type="info" size="small" @click="cancelFile">取消</el-button>
        </el-button-group>
      </div>

    </div>
    <div class="border-b h-1 flex-shrink-0">

    </div>
    <div class="flex-1 overflow-y-auto relative">
      <div v-loading.fullscreen.lock="loading" v-if="!excelData"
           class="absolute top-[40%] left-[50%] translate-x-[-50%] translate-y-[-50%] flex flex-col flex-1 items-center">
        <img src="/smiling_face.svg" alt="" width="120px">
        <div>上传要导入的 Excel 文件</div>
      </div>
      <div v-else class="h-full">
        <el-table v-loading="loading" stripe style="width: 100%" :data="excelData.data">
          <el-table-column type="index" width="50" />
          <el-table-column v-for="(field,index) in excelData.headers" :key="index" :prop="field" :label="field+'('+excelData.fields[index]+')'">
            <template #default="scope">
              {{ scope.row[field] }}
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>