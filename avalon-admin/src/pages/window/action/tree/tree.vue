<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {ComponentInternalInstance, getCurrentInstance, inject, ref, watch} from "vue";
import {useRoute} from "vue-router";
import ActionView from "../../../../model/view/ActionView.ts";
import {getModelAllApi, getModelPageApi} from "../../../../api/modelApi.ts";
import {getTemplate, XMLParserResult} from "../../../../xml/XMLParserResult.ts";
import {parserEx} from "../../../../xml/XMLParser.ts";
import MyButton from "../../../../components/button/my-button.vue";
import {useGlobalFieldDataStore} from "../../../../global/store/fieldStore.ts";
import {useGlobalServiceDataStore} from "../../../../global/store/serviceStore.ts";
import Field from "../../../../model/Field.ts";
import {getSelectionValueByServiceAndField} from "../../../../cache/SelectionValueMemory.ts";
import {FieldTypeEnum} from "../../../../model/enum-type/FieldTypeEnum.ts";
import {getActionTreeView} from "../../../../api/commonApi.ts";
import MyImage from "../../../../components/image/my-image.vue";
import {getFilePrefix, getFileUploadUrl, getPageSize} from "../../../../api/env.ts";
import MyTable from "../../../../components/table/my-table.vue";
import MyMany2manySelect from "../../../../components/select/many2may-select/my-many2many-select.vue";
import FormField from "../../../../model/FormField.ts";
import MyCheck from "../../../../components/check/my-check.vue";
import {getDateTime} from "../../../../util/dateUtils.ts";
import MySearch from "../../../../components/search/my-search.vue";


const serviceFieldStore = useGlobalFieldDataStore()
const serviceStore = useGlobalServiceDataStore()
const {proxy} = getCurrentInstance() as ComponentInternalInstance;
const route = useRoute();

const moduleName = ref<string>(route.params.module as string)
const serviceName = ref<string>(route.params.service as string)
const rowClickHandler = inject('rowClick') as (id: number | undefined) => void;
const serviceId = ref<number>(serviceStore.getServiceIdByName(serviceName.value) as number)
const serviceFields = serviceFieldStore.getFieldByServiceId(serviceId.value)
const primaryKeyField = serviceFieldStore.getPrimaryKeyFieldByServiceId(serviceId.value)

const view = ref<ActionView | undefined>(undefined)
getActionTreeView(serviceName.value).then(data => { // 加载xml
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
let template_full_fields = ref<string[]>([]);
let services_fields = ref<Field[]>([]);

let parserResult: XMLParserResult | null = null;

const parserXml = async (str: string) => {
    parserResult = await parserEx(str, serviceName.value)
    xmlTemplate.value = getTemplate(parserResult)

    template_fields.value.splice(0, template_fields.value.length)
    template_fields.value.push(...parserResult.fields.map(x => x.name))
    if (!template_fields.value.includes(primaryKeyField.name)) {
        template_fields.value.push(primaryKeyField.name)
    }

    template_full_fields.value.splice(0, template_full_fields.value.length)
    template_full_fields.value.push(...parserResult.fullFields.map(x => x.name))
    if (!template_full_fields.value.includes(primaryKeyField.name)) {
        template_full_fields.value.push(primaryKeyField.name)
    }

    services_fields.value.splice(0, services_fields.value.length)
    for (const key of template_fields.value) {
        const field = serviceFields.find(f => f.name === key)
        if (field) {
            services_fields.value.push(field)
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
        if (field.type == FieldTypeEnum.SelectionField) { // 得到字段对应的selection的值
            selectionDynamic.value[field.name] = await getSelectionValueByServiceAndField(serviceName.value, field.name)
        }
    }
    record.value.splice(0, record.value.length);
    record.value.push(...recordTemp)
}

watch(() => [moduleName.value, serviceName.value, template_fields.value.length], ([v1, v2, v3]) => {
    if (v1 && v2 && v3) {
        loadData()
    }
}, {
    immediate: true
})

const rowClick = (id: number) => {
    rowClickHandler(id)
}

const createServiceClick = () => {
    rowClickHandler(undefined)
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
</script>

<template>
    <div class="flex flex-col flex-wrap p-4 items-start h-full">
        <div class="pb-4 flex items-start w-full">
            <div class="flex-1">
                <my-button type="primary" rounded @click="createServiceClick">新增</my-button>
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
                <table class="w-full table-fixed border-collapse data-table">
                    <thead class="">
                    <tr>
                        <th class="text-left"
                            v-for="(field) in services_fields" :key="field.id">
                            {{ field.label }}
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="row in record" :key="row.id" class="cursor-pointer"
                        @click="rowClick(row[`id`])">
                        <td v-for="(field,index) in services_fields" :key="index">
                            <template v-if="field.type == FieldTypeEnum.SelectionField">
                                {{ selectionDynamic[field.name][row[field.name]] }}
                            </template>
                            <template v-else-if="field.type == FieldTypeEnum.Many2oneField">
                                {{
                                    row[field.name] ? row[field.name][serviceStore.getServiceByName(field.relativeServiceName).nameField] : ''
                                }}
                            </template>
                            <template v-else-if="field.type == FieldTypeEnum.Many2manyField">
                                {{ getMany2manyFormField(row, row[field.name], field) }}
                                <MyMany2manySelect v-model="row[field.name+'_many']" :readonly="true"
                                                   :ref="field.name+'_input'"
                                                   :service="field.relativeServiceName"
                                                   :field="field.name"
                                                   :htmlId="field.name"
                                                   :htmlName="field.name"/>
                            </template>
                            <template v-else-if="field.type == FieldTypeEnum.BooleanField">
                                {{ getFormField(row, row[field.name], field) }}
                                <MyCheck :ref="field.name+'_input'" v-model="row[field.name+'_field']" :readonly="true"
                                         :field="field.name"
                                         :htmlId="field.name"
                                         :htmlName="field.name"></MyCheck>
                            </template>
                            <template v-else-if="field.type == FieldTypeEnum.ImageField">
                                <MyImage width="50" height="50" :src="getFileUploadUrl(row[field.name])"></MyImage>
                            </template>
                            <template v-else-if="field.type == FieldTypeEnum.DateTimeField">
                                {{ row[field.name] ? getDateTime(row[field.name]) : '' }}
                            </template>
                            <template v-else>
                                {{ row[field.name] }}
                            </template>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

</template>

<style scoped>

</style>