<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import kanbanTemplate from "./kanban-template.tsx";
import {ComponentInternalInstance, getCurrentInstance, ref, watch} from "vue";
import {LocationQueryValue, useRoute} from "vue-router";
import {getModelAllApi, getModelPageApi, invokeMethod} from "../../../../api/modelApi.ts";
import ActionView from "../../../../model/view/ActionView.ts";
import {getTemplate, XMLParserResult} from "../../../../xml/XMLParserResult.ts";
import {parserEx} from "../../../../xml/XMLParser.ts";
import {refreshPage} from "../../../../util/commonUtils.ts";
import {useGlobalFieldDataStore} from "../../../../global/store/fieldStore.ts";
import {useGlobalServiceDataStore} from "../../../../global/store/serviceStore.ts";
import {getActionKanbanView} from "../../../../api/commonApi.ts";
import MySearch from "../../../../components/search/my-search.vue";
import {getPageSize} from "../../../../api/env.ts";


const route = useRoute();
const serviceFieldStore = useGlobalFieldDataStore()
const serviceStore = useGlobalServiceDataStore()

const moduleName = ref<string>(route.params.module as string)
const serviceName = ref<string>(route.params.service as string)
const serviceId = ref<number>(serviceStore.getServiceIdByName(serviceName.value) as number)
const serviceFields = serviceFieldStore.getFieldByServiceId(serviceId.value)
const primaryKeyField = serviceFieldStore.getPrimaryKeyFieldByServiceId(serviceId.value)

const view = ref<ActionView | undefined>(undefined)
getActionKanbanView(serviceName.value).then(data => {
    if (data && data.length) {
        view.value = data[0]
        renderView();
    }
})


const xmlTemplate = ref<any>(null)

const renderView = () => {
    if (view.value) {
        parserXml(view.value.arch)
    }
}

let template_fields = ref<string[]>([]);

let parserResult: XMLParserResult | null = null;

const parserXml = async (str: string) => {
    parserResult = await parserEx(str, serviceName.value)
    xmlTemplate.value = getTemplate(parserResult)
    template_fields.value.splice(0, template_fields.value.length)
    template_fields.value.push(...parserResult.fullFields.map(field => field.name))
}


const {proxy} = getCurrentInstance() as ComponentInternalInstance;


const record = ref<any>([])
const pageNum = ref<number>(1)
const condition = ref<string>('')
const begin = ref<number>(0)
const end = ref<number>(0)
const total = ref<number>(0)
const loadData = () => {
    if (!(moduleName.value && serviceName.value && template_fields.value.length)) {
        return;
    }
    getModelPageApi(template_fields.value.join(","),
        condition.value, serviceName.value, pageNum.value).then(pageInfo => {
        if (pageInfo.data) {
            record.value.splice(0, record.value.length);
            record.value.push(...pageInfo.data)
        }
        begin.value = (pageInfo.pageCur - 1) * getPageSize() + 1
        end.value = begin.value + pageInfo.data.length - 1
        total.value = pageInfo.total
    })
}

watch(() => [moduleName.value, serviceName.value, template_fields.value.length], ([v1, v2, v3]) => {
    if (v1 && v2 && v3) {
        loadData()
    }
}, {
    immediate: true
})


const btnClick = (actionType: string, action: string, fields: any) => {
    const ids: any = []
    if (fields.id) {
        ids.push(fields.id)
    }

    const param = {
        method: action,
        ids,
        param: fields
    }
    if (serviceName.value) {
        invokeMethod(serviceName.value, param).then(data => {
            proxy?.$notify.success("提示", "操作成功");
            setTimeout(() => {
                refreshPage()
            }, 1000)
        })
    }
}
const conditionChange = (search: string) => {
    condition.value = search;
    pageNum.value = 1
    loadData();
}
const handlePageChange = (dir: string) => {
    if (dir == 'right') { // 下一页
        pageNum.value = pageNum.value + 1;
    } else {
        pageNum.value = pageNum.value - 1;
    }
    loadData()
}
</script>

<template>
    <div>
        <div class="pb-4 flex items-start w-full px-4">
            <div class="flex-1"></div>
            <div class="flex-1">
                <MySearch @conditionChange="conditionChange" :full-width="true" class="w-full"
                          :service="serviceName"/>
            </div>
            <div class="flex-1 flex justify-end">
                <MyPagination v-model:total="total" v-model:begin="begin" v-model:end="end"
                              @pageChange="handlePageChange"></MyPagination>
            </div>
        </div>
    </div>
    <div class="flex flex-wrap p-1">
        <div v-for="item in record" :key="item.id" class="flex bg-white p-3 border rounded m-2 w-[400px]">
            <kanbanTemplate v-if="xmlTemplate" :template="xmlTemplate" :fields="item"
                            @btnClick="btnClick"></kanbanTemplate>
        </div>
    </div>

</template>

<style scoped>

</style>