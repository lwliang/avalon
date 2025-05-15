<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import Field from "../../../../model/Field.ts";
import MyTable from "../../../../components/table/my-table.vue";
import {computed, ref} from "vue";
import {useGlobalFieldDataStore} from "../../../../global/store/fieldStore.ts";
import {getTemplate, XMLParserResult} from "../../../../xml/XMLParserResult.ts";
import {parserEx} from "../../../../xml/XMLParser.ts";
import ActionView from "../../../../model/view/ActionView.ts";
import {useGlobalServiceDataStore} from "../../../../global/store/serviceStore.ts";
import {useRoute} from "vue-router";
import FormField from "../../../../model/FormField.ts";
import Snowflake from "../../../../model/Snowflake.ts";
import {getActionTreeView} from "../../../../api/commonApi.ts";
import {getCurrentInstance} from "vue";
import Service from "../../../../model/Service.ts";
import {FieldTypeEnum} from "../../../../model/enum-type/FieldTypeEnum.ts";
import ServiceSearchModel from "../../../../components/model/service-search-model/service-search-model.vue";
import {getModelAllApi} from "../../../../api/modelApi.ts";
import ShowField from "../../../../model/ShowField.ts";

const serviceFieldStore = useGlobalFieldDataStore()
const serviceStore = useGlobalServiceDataStore()
const route = useRoute();
const instance = getCurrentInstance();
const tableHeight = ref('600px');

defineOptions({
    name: 'MySubTree',
})

const findParent = (name: string) => {
    let parent = instance?.parent;
    while (parent) {
        if (parent.type.name === name) {
            return true
        }
        parent = parent.parent;
    }
    return false;
}

if (findParent('MyFormModel')) {
    tableHeight.value = '100%'
}

const props = defineProps<{
    record: FormField,
    fields?: String, // 显示的字段，如果为空，则从service获取
    service: string,
    parentService: string, // 委托继承时使用
    delegate: boolean,
    field: string,// 关系字段
    title: string
}>()

const getServiceName = computed(() => {
    if (props.delegate) {
        return props.parentService;
    }
    return props.service
})

const view = ref<ActionView | undefined>(undefined)
const serviceName = ref<string>(props.service as string)
const serviceFields: Field[] = []
serviceFieldStore.getFieldByServiceNameAsync(serviceName.value).then(data => {
    serviceFields.push(...data)
})
const primaryService = ref<Service>()
serviceStore.getServiceByNameAsync(serviceName.value).then(data => {
    primaryService.value = data;
})

let template_fields = ref<string[]>([]);
let template_full_fields = ref<string[]>([]);
let services_fields = ref<ShowField[]>([]);
const fieldsParser = async () => {
    if (!props.fields) return;
    const serviceFields = await serviceFieldStore.getFieldByServiceNameAsync(serviceName.value)

    for (let fieldStr of props.fields.split(",")) {
        template_fields.value.push(fieldStr);
        template_full_fields.value.push(fieldStr);
        const field = serviceFields.find(f => f.name === fieldStr)
        if (field) {
            services_fields.value.push({Field: field, originField: fieldStr, serviceName: serviceName.value})
        }
    }
}
if (!props.fields) { // one2many/many2many 关系需要显示的字段列表 没有则获取tree视图对应的字段
    getActionTreeView(props.service).then(data => {
        if (data && data.length) {
            view.value = data[0]
            renderView();
        }
    })
} else {
    fieldsParser();
}


const xmlTemplate = ref<any>(null)

const renderView = () => {
    if (view.value) {
        parserXml(view.value.arch)
    }
}


let parserResult: XMLParserResult | null = null;


const parserXml = async (str: string) => {
    parserResult = await parserEx(str, props.service)
    xmlTemplate.value = getTemplate(parserResult);
    const keyService = await serviceStore.getServiceByNameAsync(serviceName.value)
    template_fields.value.splice(0, template_fields.value.length)
    template_fields.value.push(...parserResult.fields.map(x => x.name))
    if (!template_fields.value.includes(keyService.keyField)) {
        template_fields.value.push(keyService.keyField)
    }

    template_full_fields.value.splice(0, template_full_fields.value.length)
    template_full_fields.value.push(...parserResult.fullFields.map(x => x.name))
    if (!template_full_fields.value.includes(keyService.keyField)) {
        template_full_fields.value.push(keyService.keyField)
    }

    services_fields.value.splice(0, services_fields.value.length)
    const serviceFields = await serviceFieldStore.getFieldByServiceNameAsync(serviceName.value)
    for (const key of template_full_fields.value) {
        const field = serviceFields.find(f => f.name === key)
        if (field) {
            services_fields.value.push({Field: field, originField: key, serviceName: serviceName.value})
        }
    }
}

const fieldComputed = computed(() => {
    return services_fields.value
})

const subShow = ref(false)
const subRowId = ref(undefined)
const selectRow = ref<any>()

const many2ManyShow = ref(false)

const addRow = () => {
    if (props.record.Field?.type && props.record.Field?.type == FieldTypeEnum.One2manyField) {
        subRowId.value = undefined
        selectRow.value = undefined
        subShow.value = true
    } else {
        if (props.record.Field?.type && props.record.Field?.type == FieldTypeEnum.Many2oneField) {
            subRowId.value = undefined
            selectRow.value = undefined
            subShow.value = true
        } else {
            many2ManyShow.value = true
        }
    }
}

const many2ManyClose = () => {
    many2ManyShow.value = false
}

const many2ManySure = (ids: any[]) => {
    console.log('many2ManySure', ids)
    getModelAllApi(props.fields as string, `('${primaryService.value?.keyField}',in,${ids.join(',')})`, props.service).then(data => {
        if (!props.record.value) {
            props.record.value = []
        }
        for (let datum of data) {
            const item: any = {
                id: Symbol(Snowflake.getNextId()),
            }
            item[`${props.record.Field?.relativeForeignKeyName}`] = {...datum}
            props.record.value.push(item)
        }
        many2ManyShow.value = false
    })
}

const subRowCloseClick = () => {
    subShow.value = false
}

const subRowSureClick = (row: any) => {
    subShow.value = false
    if (selectRow.value) {
        Object.assign(selectRow.value, row)
    } else {
        if (!props.record.value) {
            props.record.value = []
        }
        if (!subRowId.value) {
            if (primaryService.value) {
                row[primaryService.value.keyField] = Symbol(Snowflake.getNextId())
            }
        }
        props.record.value.push(row)
    }
}

const rowDeleteClick = (row: any) => {
    if (props.record.Field?.type && props.record.Field?.type == FieldTypeEnum.Many2manyField) {
        const key = props.record.Field.relativeForeignKeyName;
        if (primaryService.value) {
            const keyName = primaryService.value.keyField;
            const index = props.record.value.findIndex((x: any) => x[key][keyName] == row[keyName])
            if (index >= 0) {
                props.record.value.splice(index, 1)
            }
        }

    } else {
        if (props.record.value) {
            if (primaryService.value) {
                const keyField = primaryService.value.keyField;
                const i = props.record.value.findIndex((item: any) => item[keyField] == row[keyField])
                if (i >= 0) {
                    props.record.value.splice(i, 1)
                }
            }
        }
    }
}

const rowClick = (row: any) => {
    if (primaryService.value) {
        subRowId.value = row[primaryService.value.keyField]
        selectRow.value = row
        subShow.value = true
    }
}

const getRecordComputed = computed(() => {
    if (props.record.Field?.type && props.record.Field?.type == FieldTypeEnum.Many2manyField) {
        const key = props.record.Field.relativeForeignKeyName;

        return props.record.value.map((x: any) => x[key])
    }
    return props.record.value as any[];
})

</script>

<template>
    <div>
        <div class="py-2">
            <MyTable :height="tableHeight" :record="getRecordComputed" :fields="fieldComputed"
                     :service-name="serviceName"
                     :showDeleteBtn="true"
                     @rowDeleteClick="rowDeleteClick"
                     @rowClick="rowClick"
                     :showBtnRow="true"
                     @row-add-click="addRow">

            </MyTable>
        </div>
        <MyFormModel v-if="subShow" :show="subShow"
                     :title="title"
                     :service="getServiceName"
                     :row-id="subRowId"
                     :old-record-row="selectRow"
                     @close="subRowCloseClick"
                     @sure="subRowSureClick"></MyFormModel>
        <ServiceSearchModel :title="title" :show="many2ManyShow" :service="service" @close="many2ManyClose"
                            @sure="many2ManySure"/>
    </div>

</template>

<style scoped>

</style>