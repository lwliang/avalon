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
    fields?: Field[],
    service: string,
    field: string,
    title: string
}>()

const view = ref<ActionView | undefined>(undefined)
const serviceName = ref<string>(props.service as string)
const serviceId = ref<number>(serviceStore.getServiceIdByName(serviceName.value) as number)
const serviceFields = serviceFieldStore.getFieldByServiceId(serviceId.value)
const primaryKeyField = serviceFieldStore.getPrimaryKeyFieldByServiceId(serviceId.value)

if (!props.fields || props.fields.length == 0) {
    getActionTreeView(props.service).then(data => {
        if (data && data.length) {
            view.value = data[0]
            renderView();
        }
    })
}
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
    parserResult = await parserEx(str, props.service)
    xmlTemplate.value = getTemplate(parserResult);

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
    for (const key of template_full_fields.value) {
        const field = serviceFields.find(f => f.name === key)
        if (field) {
            services_fields.value.push(field)
        }
    }
}

const fieldComputed = computed(() => {
    if (props.fields && props.fields.length) return props.fields
    return services_fields.value
})

const subShow = ref(false)
const subRowId = ref(undefined)
const selectRow = ref<any>()
const addRow = () => {
    subRowId.value = undefined
    selectRow.value = undefined
    subShow.value = true
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
            row[primaryKeyField.name] = Symbol(Snowflake.getNextId())
        }
        props.record.value.push(row)
    }
}

const rowDeleteClick = (row: any) => {
    if (props.record.value) {
        const i = props.record.value.findIndex((item: any) => item[primaryKeyField.name] == row[primaryKeyField.name])
        if (i >= 0) {
            props.record.value.splice(i, 1)
        }
    }
}

const rowClick = (row: any) => {
    subRowId.value = row[primaryKeyField.name]
    selectRow.value = row
    subShow.value = true
}


</script>

<template>
    <div>
        <div>
            <MyButton type="primary" rounded @click="addRow">新增</MyButton>
        </div>
        <div class="py-2">
            <MyTable :height="tableHeight" :record="record.value" :fields="fieldComputed" :service-name="serviceName"
                     @rowDeleteClick="rowDeleteClick"
                     @rowClick="rowClick">

            </MyTable>
        </div>
        <MyFormModel v-if="subShow" :show="subShow"
                     :title="title"
                     :service="service"
                     :row-id="subRowId"
                     :old-record-row="selectRow"
                     @close="subRowCloseClick"
                     @sure="subRowSureClick"></MyFormModel>
    </div>

</template>

<style scoped>

</style>