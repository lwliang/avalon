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

    readExcelContent(serviceName.value, fileBlob.value as File).then(data => {
        excelData.value = data
    })
}

const excelData = ref<any>(undefined)

const importExcelFile = () => {
    importExcel(serviceName.value, excelData.value).then(data => {
        proxy?.$notify.success("提示", "导入成功");
        goModelWindow(moduleName.value, serviceName.value, {})
    })
}
</script>

<template>
    <div class="flex flex-col h-full">
        <div class="flex-shrink-0 px-4 py-2">
            <input type="file" hidden="hidden" ref="file" @change="fileChange"
                   accept=".csv, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel">
            <MyButton class="mr-1" type="primary" @click="uploadExcelFile">上传文件</MyButton>
            <MyButton class="mr-1" type="success" @click="importExcelFile" v-if="excelData">导入</MyButton>
            <MyButton type="info" @click="cancelFile">取消</MyButton>
        </div>
        <div class="border-b h-1 flex-shrink-0">

        </div>
        <div class="flex-1 overflow-y-auto relative">
            <div v-if="!excelData"
                 class="absolute top-[40%] left-[50%] translate-x-[-50%] translate-y-[-50%] flex flex-col flex-1 items-center">
                <img src="/smiling_face.svg" alt="" width="120px">
                <div>上传要导入的 Excel 文件</div>
            </div>
            <div v-else class="h-full">
                <table class="data-table">
                    <thead>
                    <tr>
                        <th v-for="(field,index) in excelData.headers" :key="index">
                            {{ field }}
                        </th>
                    </tr>
                    <tr>
                        <th v-for="(field,index) in excelData.fields" :key="index">
                            {{ field }}
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="(row,index) in excelData.data" :key="index">
                        <td v-for="(header,indexRow) in excelData.headers" :key="indexRow">{{ row[header] }}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</template>

<style scoped>

</style>