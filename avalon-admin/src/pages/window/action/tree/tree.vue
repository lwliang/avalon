<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {ComponentInternalInstance, getCurrentInstance, inject, provide, ref, watch} from "vue";
import {useRoute} from "vue-router";
import ActionView from "../../../../model/view/ActionView.ts";
import {deleteMultiModelApi, getModelAllApi, getModelPageApi} from "../../../../api/modelApi.ts";
import {getTemplate, XMLParserResult} from "../../../../xml/XMLParserResult.ts";
import {parserEx} from "../../../../xml/XMLParser.ts";
import MyButton from "../../../../components/button/my-button.vue";
import {useGlobalFieldDataStore} from "../../../../global/store/fieldStore.ts";
import {useGlobalServiceDataStore} from "../../../../global/store/serviceStore.ts";
import Field from "../../../../model/Field.ts";
import {getSelectionValueByServiceAndField} from "../../../../cache/SelectionValueMemory.ts";
import {FieldTypeEnum} from "../../../../model/enum-type/FieldTypeEnum.ts";
import {exportExcel, getActionTreeView} from "../../../../api/commonApi.ts";
import MyImage from "../../../../components/image/my-image.vue";
import {getFilePrefix, getFileUploadUrl, getPageSize} from "../../../../api/env.ts";
import MyTable from "../../../../components/table/my-table.vue";
import MyMany2manySelect from "../../../../components/select/many2may-select/my-many2many-select.vue";
import FormField from "../../../../model/FormField.ts";
import MyCheck from "../../../../components/check/my-check.vue";
import {getDateTime} from "../../../../util/dateUtils.ts";
import MySearch from "../../../../components/search/my-search.vue";
import {useUserInfoStore} from "../../../../global/store/userInfoStore.ts";
import MyDebug from "../../../../components/debug/my-debug.vue";
import MyDialog from "../../../../components/dialog/my-dialog.vue";
import MyExportDialog from "../../../../components/dialog/my-export-dialog.vue";
import {goModelImport, goModelWindow} from "../../../../util/routerUtils.ts";
import {getServiceField, hasJoin} from "../../../../util/fieldUtils.ts";
import ShowField from "../../../../model/ShowField.ts";


const serviceFieldStore = useGlobalFieldDataStore()
const serviceStore = useGlobalServiceDataStore()
const userInfoStore = useUserInfoStore()

const {proxy} = getCurrentInstance() as ComponentInternalInstance;
const route = useRoute();

const moduleName = ref<string>(route.params.module as string)
const serviceName = ref<string>(route.params.service as string)
const rowClickHandler = inject('rowClick') as (id: number | undefined) => void;


const view = ref<ActionView | undefined>(undefined)
getActionTreeView(serviceName.value).then(data => { // 加载xml
    if (data && data.length) {
        view.value = data[0]
        renderView();
    }
})

const xmlTemplate = ref<any>(null)

const renderView = async () => {
    if (view.value) {
        await parserXml(view.value.arch)
        loadData()
    }
}

let template_fields = ref<string[]>([]);
let template_full_fields = ref<string[]>([]);
let services_fields = ref<ShowField[]>([]);

let parserResult: XMLParserResult | null = null;

const parserXml = async (str: string) => {
    const primaryKeyField = await serviceStore.getServiceByNameAsync(serviceName.value)
    const serviceFields = await serviceFieldStore.getFieldByServiceNameAsync(serviceName.value)
    parserResult = await parserEx(str, serviceName.value)
    xmlTemplate.value = getTemplate(parserResult)

    template_fields.value.splice(0, template_fields.value.length)
    template_fields.value.push(...parserResult.fields.map(x => x.name))
    if (!template_fields.value.includes(primaryKeyField.keyField)) {
        template_fields.value.push(primaryKeyField.keyField)
    }

    template_full_fields.value.splice(0, template_full_fields.value.length)
    template_full_fields.value.push(...parserResult.fullFields.map(x => x.name))
    if (!template_full_fields.value.includes(primaryKeyField.keyField)) {
        template_full_fields.value.push(primaryKeyField.keyField)
    }

    services_fields.value.splice(0, services_fields.value.length)
    for (const key of template_fields.value) {
        let field;
        if (!hasJoin(key)) {
            field = serviceFields.find(f => f.name === key)
        } else {
            field = await getServiceField(serviceName.value, key);
        }
        if (field) {
            services_fields.value.push({Field: field, originField: key, serviceName: serviceName.value})
        }
    }
}

const emit = defineEmits(['rowClick'])

const record = ref<any>([])
const pageNum = ref<number>(1)
const selectionDynamic = ref<any>({})
const condition = ref<string>('')
const begin = ref<number>(0)
const end = ref<number>(0)
const total = ref<number>(0)
const loadData = async () => {
    if (!(serviceName.value && moduleName.value && template_fields.value.length)) {
        return;
    }
    const recordTemp: any = []
    await getModelPageApi(template_full_fields.value.join(","),
        condition.value,
        serviceName.value,
        pageNum.value).then(pageInfo => {
        if (pageInfo.data) {
            recordTemp.push(...pageInfo.data)
        }
        begin.value = (pageInfo.pageCur - 1) * getPageSize() + 1
        end.value = begin.value + pageInfo.data.length - 1
        total.value = pageInfo.total
    })
    for (let field of services_fields.value) {
        if (field.Field.type == FieldTypeEnum.SelectionField) { // 得到字段对应的selection的值
            selectionDynamic.value[field.Field.name] = await getSelectionValueByServiceAndField(serviceName.value, field.Field.name)
        }
    }
    record.value.splice(0, record.value.length);
    record.value.push(...recordTemp)
}


const rowClick = (row: any) => {
    serviceStore.getServiceByNameAsync(serviceName.value).then(service => {
        rowClickHandler(row[service.keyField])
    })
}

const createServiceClick = () => {
    rowClickHandler(undefined)
}

const deleteServiceClick = () => {
    deleteShow.value = true
}

const getMany2manyFormField = (obj: any, value: any, field: Field) => {
    obj[field.name + '_many'] = new FormField(value, field)
    return ''
}
const getFormField = (obj: any, value: any, field: Field) => {
    obj[field.name + '_field'] = new FormField(value, field)
    return ''
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
const rowSelectCount = ref(0)
const rowSelectIds = ref<any[]>([])
const rowSelectChangeHandler = (selectCount: number, ids: any[]) => {
    rowSelectCount.value = selectCount

    rowSelectIds.value.splice(0, rowSelectIds.value.length)
    if (selectCount) {
        rowSelectIds.value.push(...ids)
    }
}

const deleteShow = ref(false)

const hideClick = () => {
    deleteShow.value = false
    rowSelectIds.value.splice(0, rowSelectIds.value.length)
}
const sureClick = () => {
    deleteMultiModelApi(rowSelectIds.value, serviceName.value as string).then(data => {
        proxy?.$notify.success('提示', '删除完成');
        deleteShow.value = false

        for (let deleteIdsKey of rowSelectIds.value) {
            const index = record.value.findIndex((x: any) => x.id == deleteIdsKey)
            if (index >= 0) {
                record.value.splice(index, 1)
            }
        }

    })
}

const exportShow = ref(false)
const exportClose = () => {
    exportShow.value = false
}
const exportOpen = () => {
    exportShow.value = true
}
const exportSure = async (fields: string) => {
    const primaryKeyField = await serviceStore.getServiceByNameAsync(serviceName.value)
    let condition = "";
    if (rowSelectIds.value.length) {
        condition = `(in,${primaryKeyField.keyField},${rowSelectIds.value.join(",")})`
    }
    exportExcel(serviceName.value, fields, condition, "").then(data => {
        proxy?.$notify.success("提示", "导出成功");
    })
}

const importExcelClick = () => {
    goModelImport(moduleName.value, serviceName.value, {})
}
</script>

<template>
    <div class="flex flex-col flex-wrap p-4 items-start h-full">
        <div class="pb-4 flex items-start w-full">
            <div class="flex-1">
                <my-button class="mr-0.5" type="primary" rounded @click="createServiceClick">新增</my-button>
                <my-button class="mr-0.5" type="primary" rounded @click="importExcelClick">导入</my-button>
                <my-button class="mr-0.5" v-if="rowSelectCount" type="success" rounded @click="exportOpen">
                    导出
                </my-button>
                <my-button class="mr-0.5" v-if="rowSelectCount" type="danger" rounded @click="deleteServiceClick">删除
                </my-button>
            </div>
            <div class="flex-1 px-4">
                <MySearch @conditionChange="conditionChange" :full-width="true" class="w-full"
                          :service="serviceName"></MySearch>
            </div>
            <div class="flex-1 flex justify-end">
                <MyPagination v-model:total="total" v-model:begin="begin" v-model:end="end"
                              @pageChange="handlePageChange"></MyPagination>
            </div>
        </div>
        <div class="flex-1 overflow-y-auto flex flex-col">
            <div class="flex-1 overflow-y-auto">
                <MyTable height="100%" :record="record" :fields="services_fields" :service-name="serviceName"
                         :showSelectBtn="true"
                         @rowClick="rowClick"
                         @rowSelectChange="rowSelectChangeHandler">

                </MyTable>
            </div>
        </div>
    </div>
    <MyDialog :show="deleteShow" @close="hideClick" @sure="sureClick" title="提示">
        确认删除吗?
    </MyDialog>
    <MyExportDialog :show="exportShow" :service="serviceName" @close="exportClose" @sure="exportSure"></MyExportDialog>
</template>

<style scoped>

</style>