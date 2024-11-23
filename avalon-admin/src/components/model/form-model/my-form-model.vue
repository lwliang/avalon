<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import './my-form-model.css';
import MyOverlay from "../../overlay/my-overlay.vue";
import MyIcon from "../../icon/my-icon.vue";
import MyButton from "../../button/my-button.vue";
import {compile, createVNode, defineComponent, ref, shallowRef} from "vue";
import {useGlobalFieldDataStore} from "../../../global/store/fieldStore.ts";
import {useGlobalServiceDataStore} from "../../../global/store/serviceStore.ts";
import ActionView from "../../../model/view/ActionView.ts";
import {getModelAllApi, getModelDetailApi} from "../../../api/modelApi.ts";
import {parserEx} from "../../../xml/XMLParser.ts";
import Field from "../../../model/Field.ts";
import {getTemplate, XMLParserResult} from "../../../xml/XMLParserResult.ts";
import {FieldTypeEnum} from "../../../model/enum-type/FieldTypeEnum.ts";
import FormField from "../../../model/FormField.ts";
import {cloneDeep} from "lodash";
import {getActionFormView, getActionTreeView} from "../../../api/commonApi.ts";

const emit = defineEmits(['close', 'sure'])
defineOptions({
    name: 'MyFormModel',
})

const props = defineProps<{
    title: string,
    show: boolean,
    service: string,
    oldRecordRow?: any,
    rowId: Symbol | number | undefined
}>()

const serviceFieldStore = useGlobalFieldDataStore()
const serviceStore = useGlobalServiceDataStore()
const serviceFields = serviceFieldStore.getFieldByServiceName(props.service)
const primaryKeyField = serviceFieldStore.getPrimaryKeyFieldByServiceName(props.service)

const view = ref<ActionView | undefined>(undefined)

const loadView = async (service: string) => {
    return await getActionFormView(service).then(data => {
        if (data && data.length) {
            return data[0]
        }
        return null
    })
}
loadView(props.service).then((dataView: any) => {
    if (dataView) {
        view.value = dataView
        renderView(dataView.arch).then(() => {
            loadDataWithLayout()
        })
    }
})

const recordRow = ref<any>({})
const recordRowWithField = ref<Record<string, FormField>>({})
const loadDataWithLayout = () => {
    if (!(props.service && template_fields.value.length)) {
        return;
    }
    if (props.rowId && !(typeof props.rowId == 'symbol')) {
        getModelDetailApi(props.rowId as number, template_fields.value.join(","),
            props.service,).then(data => {
            if (data) {
                Object.assign(recordRow.value, data)
                for (let key in recordRow.value) {
                    const field = serviceFields.find(f => f.name === key) as Field
                    if (field.type == FieldTypeEnum.One2manyField) {
                        recordRowWithField.value[key] = new FormField(recordRow.value[key], field)
                    } else {
                        recordRowWithField.value[key] = new FormField(recordRow.value[key], field)
                    }
                }
                if (props.oldRecordRow) {
                    mergeRecordRow(props.oldRecordRow, recordRowWithField.value)
                }
                template_component.value = createFormTemplateVNode();
            }
        })
    } else {
        for (let key of template_fields.value) {
            const field = serviceFields.find(f => f.name === key)
            if (field && field.type == FieldTypeEnum.One2manyField) {
                recordRowWithField.value[key] = new FormField([], field)
            } else if (field) {
                recordRowWithField.value[key] = new FormField(undefined, field)
            }
        }
        if (props.oldRecordRow) {
            mergeRecordRow(props.oldRecordRow, recordRowWithField.value)
        }
        template_component.value = createFormTemplateVNode();
    }
}

const mergeRecordRow = (src: any, dst: any) => {
    for (const srcKey in src) {
        if (src[srcKey] instanceof File) {
            dst[srcKey].reset(src[srcKey])
            continue
        }
        const srcValue = cloneDeep(src[srcKey]);
        if (dst[srcKey]) {
            dst[srcKey].reset(srcValue)
        }
    }
}

const createFormTemplateVNode = () => {
    return defineComponent({
        setup() {
            const vNode = compile(xmlTemplate.value)
            return () => {
                return createVNode(vNode, {...recordRowWithField.value})
            }
        }
    })
}

const xmlTemplate = ref<any>(null)
const template_component = shallowRef<any>(null)
let template_fields = ref<string[]>([]);

let parserResult: XMLParserResult | null = null;

const renderView = async (arch: string) => {
    await parserXml(arch)
}

const parserXml = async (str: string) => {
    parserResult = await parserEx(str, props.service)
    xmlTemplate.value = getTemplate(parserResult);
    template_fields.value.splice(0, template_fields.value.length)
    template_fields.value.push(...parserResult.fullFields.map(x => x.name))
    if (parserResult.one2ManyFields && parserResult.one2ManyFields.length) {
        for (let manyField of parserResult.one2ManyFields) {
            const find = serviceFields.find(x => x.name == manyField) as Field;
            const viewData = await loadTreeView(find.relativeServiceName);
            const tempFields = serviceFieldStore.getFieldByServiceName(find.relativeServiceName)
            const tempKeyField = serviceFieldStore.getPrimaryKeyFieldByServiceName(find.relativeServiceName)
            const parserResult2 = await parserEx(viewData.arch, find.relativeServiceName)
            for (let tempField of parserResult2.fullFields) {
                template_fields.value.push(`${manyField}.${tempField.name}`)
            }
            if (!template_fields.value.includes(`${manyField}.${tempKeyField.name}`)) {
                template_fields.value.push(`${manyField}.${tempKeyField.name}`)
            }
        }
    }
    if (!template_fields.value.includes(primaryKeyField.name)) {
        template_fields.value.push(primaryKeyField.name)
    }
}

const loadTreeView = async (service: string) => {
    return await getActionTreeView(service).then(data => {
        if (data && data.length) {
            return data[0]
        }
        return null
    })
}

const closeClick = () => {
    emit('close')
}

const sureClick = async () => {
    const row: any = {}
    for (let key in recordRowWithField.value) {
        const value = recordRowWithField.value[key]
        if (value.isChanged()) {
            row[key] = value.value
        }
    }
    if (props.rowId) {
        row[primaryKeyField.name] = props.rowId
    }
    emit('sure', row)
}

</script>

<template>
    <MyOverlay v-if="props.show">
        <div class="absolute-center w-4/5 h-3/4 bg-white flex flex-col">
            <div class="model-head">
                <div class="dialog-title flex-1">
                    {{ title }}
                </div>
                <div class="dialog-close pr-2">
                    <MyIcon class="cursor-pointer" icon="xmark" type="fas" @click="closeClick"></MyIcon>
                </div>
            </div>
            <div class="model-content flex-1 overflow-hidden">
                <component :is="template_component"/>
            </div>
            <div class="model-footer">
                <MyButton type="info" rounded @click="closeClick">取消</MyButton>
                <MyButton class="ml-3" type="primary" rounded @click="sureClick">确认</MyButton>
            </div>
        </div>
    </MyOverlay>
</template>

<style scoped>

</style>