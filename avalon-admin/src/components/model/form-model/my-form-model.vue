<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import './my-form-model.css';
import MyOverlay from "../../overlay/my-overlay.vue";
import MyIcon from "../../icon/my-icon.vue";
import MyButton from "../../button/my-button.vue";
import {
  compile,
  ComponentInternalInstance,
  createVNode,
  defineComponent,
  getCurrentInstance,
  ref,
  shallowRef,
  watch
} from "vue";
import {useGlobalFieldDataStore} from "../../../global/store/fieldStore.ts";
import {useGlobalServiceDataStore} from "../../../global/store/serviceStore.ts";
import ActionView from "../../../model/view/ActionView.ts";
import {getModelDetailApi} from "../../../api/modelApi.ts";
import {parserEx} from "../../../xml/XMLParser.ts";
import Field from "../../../model/Field.ts";
import {getTemplate, XMLParserResult} from "../../../xml/XMLParserResult.ts";
import {FieldTypeEnum} from "../../../model/enum-type/FieldTypeEnum.ts";
import FormField from "../../../model/FormField.ts";
import {cloneDeep} from "lodash";
import {getActionFormView, getActionTreeView, getOnChangeFields, onChangeValue} from "../../../api/commonApi.ts";
import Service from "../../../model/Service.ts";
import {
  getFieldAllRecordRow,
  getJoinFirstField,
  getJoinLastField, getOldFieldRecordRow,
  getServiceField,
  hasJoin
} from "../../../util/fieldUtils.ts";
import {isObjectEmpty} from "../../../util/ObjectUtils.ts";

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
const {proxy} = getCurrentInstance() as ComponentInternalInstance;

const primaryService = ref<Service>()
serviceStore.getServiceByNameAsync(props.service).then(data => {
    primaryService.value = data
})
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
const recordRowWithField = ref<Record<string, FormField | any>>({})
const loadDataWithLayout = async () => {
    if (!(props.service && template_fields.value.length)) {
        return;
    }
    const serviceFields = await serviceFieldStore.getFieldByServiceNameAsync(props.service)
    const serviceInstance = await serviceStore.getServiceByNameAsync(props.service);
    if (props.rowId && !(typeof props.rowId == 'symbol')) {
        const data = await getModelDetailApi(props.rowId as number, template_fields.value.join(","),
            props.service)
        if (data) {
            Object.assign(recordRow.value, data)
            for (let key in recordRow.value) {
                const field = serviceFields.find(f => f.name === key) as Field
                if (field.type == FieldTypeEnum.One2manyField) {
                    recordRowWithField.value[key] = new FormField(recordRow.value[key], field)
                } else if (field.type == FieldTypeEnum.Many2oneField) {
                    if (serviceInstance.delegateField) {
                        const delegateField = JSON.parse(serviceInstance.delegateField)
                        let delegate = false;
                        for (let s in delegateField) {
                            if (delegateField[s] == field.name) {
                                delegate = true;
                                recordRowWithField.value[key] = {}
                                for (const sKey in recordRow.value[key]) {
                                    recordRowWithField.value[key][sKey] = new FormField(recordRow.value[key][sKey], field)
                                }
                                break;
                            }
                        }
                        if (!delegate) {
                            recordRowWithField.value[key] = new FormField(recordRow.value[key], field)
                        }
                    } else {
                        recordRowWithField.value[key] = new FormField(recordRow.value[key], field)
                    }
                } else {
                    recordRowWithField.value[key] = new FormField(recordRow.value[key], field)
                }
            }
            if (props.oldRecordRow) {
                mergeRecordRow(props.oldRecordRow, recordRowWithField.value)
            }
            template_component.value = createFormTemplateVNode();
        }
    } else {
        for (let key of template_fields.value) {
            if (hasJoin(key)) {
                const first = getJoinFirstField(key); // 只支持二级字段
                const firstField = await getServiceField(props.service, first);
                const last = getJoinLastField(key);
                const lastField = await getServiceField(props.service, key);
                if (!recordRowWithField.value[first]) {
                    recordRowWithField.value[first] = {}
                }
                if (lastField && (lastField.type == FieldTypeEnum.One2manyField ||
                    lastField.type == FieldTypeEnum.Many2manyField)) {
                    recordRowWithField.value[first][last] = new FormField([], lastField)
                } else if (lastField) {
                    recordRowWithField.value[first][last] = new FormField(undefined, lastField)
                }
            } else {
                let field = serviceFields.find(f => f.name === key)
                if (field && (field.type == FieldTypeEnum.One2manyField ||
                    field.type == FieldTypeEnum.Many2manyField)) {
                    recordRowWithField.value[key] = new FormField([], field)
                } else if (field) {
                    recordRowWithField.value[key] = new FormField(undefined, field)
                }
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
    const component = defineComponent({
        setup() {
            const vNode = compile(xmlTemplate.value)
            return () => {
                return createVNode(vNode, {...recordRowWithField.value})
            }
        }
    })

    return component;
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
    const tempService = await serviceStore.getServiceByNameAsync(props.service)
    const serviceFields = await serviceFieldStore.getFieldByServiceNameAsync(props.service)
    xmlTemplate.value = getTemplate(parserResult);
    template_fields.value.splice(0, template_fields.value.length)
    template_fields.value.push(...parserResult.fullFields.map(x => x.name))
    if (parserResult.one2ManyFields && parserResult.one2ManyFields.length) {
        for (let manyField of parserResult.one2ManyFields) {
            let find;
            if (!hasJoin(manyField)) { // 当前表字段
                find = serviceFields.find(x => x.name == manyField) as Field;
            } else {
                find = await getServiceField(props.service, manyField)
            }
            if (!find) {
                continue;
            }
            const viewData = await loadTreeView(find.relativeServiceName);
            const tempFields = serviceFieldStore.getFieldByServiceName(find.relativeServiceName)
            const relativeService = await serviceStore.getServiceByNameAsync(find.relativeServiceName)
            const parserResult2 = await parserEx(viewData.arch, find.relativeServiceName)
            for (let tempField of parserResult2.fullFields) {
                template_fields.value.push(`${manyField}.${tempField.name}`)
            }
            if (!template_fields.value.includes(`${manyField}.${relativeService.keyField}`)) {
                template_fields.value.push(`${manyField}.${relativeService.keyField}`)
            }
        }
    }
    if (!template_fields.value.includes(tempService.keyField)) {
        template_fields.value.push(tempService.keyField)
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
        if (value instanceof FormField) {
            if (value.isChanged()) {
                row[key] = value.value
            }
        } else {
            row[key] = {}
            for (let x in value) {
                if (value[x].isChanged()) {
                    row[key][x] = value[x].value
                }
            }
        }
    }
    if (props.rowId) {
        if (primaryService.value) {
            row[primaryService.value.keyField] = props.rowId
        }
    }
    emit('sure', row)
}
/**
 * 加载表单字段监听列表
 * @param serviceName
 */
const onChangeFields = ref<String[]>([])
const loadOnChangeField = async (serviceName: string) => {
  return getOnChangeFields(serviceName)
}
loadOnChangeField(props.service).then(data => {
  onChangeFields.value.push(...data)
})
watch(recordRowWithField.value, async () => {
  let changeFields: any = {}
  for (let fieldKey in recordRowWithField.value) {
    const fieldValue = recordRowWithField.value[fieldKey]
    if (fieldValue.isChanged()) {
      if (onChangeFields.value.includes(fieldKey)) {
        changeFields[fieldKey] = true
      }
    }
  }
  if (!isObjectEmpty(changeFields)) {
    const newRow = await getFieldAllRecordRow(recordRowWithField.value)
    const oldRow = await getOldFieldRecordRow(recordRowWithField.value)
    const changeRecordRow = await onChangeValue(props.service, changeFields, newRow, oldRow)
    if (changeRecordRow.value) { // 返回值
      for (const fieldKey in changeRecordRow.value) {
        recordRowWithField.value[fieldKey].value = changeRecordRow.value[fieldKey]
      }
    }
    if (changeRecordRow.warnings && changeRecordRow.warnings.length > 0) { // 有异常
      for (const warning of changeRecordRow.warnings) {
        proxy?.$notify.error(warning.title, warning.message)
      }
    }
  }
}, {
  deep: true
})
</script>

<template>
    <MyOverlay v-if="props.show">
        <div class="absolute-center w-4/5 h-3/4 bg-background flex flex-col">
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