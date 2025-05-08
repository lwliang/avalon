<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import './my-id-select.css'
import {ref, watch} from "vue";
import FormField from "../../../model/FormField.ts";
import {InputExpose} from "../../../global/input/InputExpose.ts";
import {onMounted} from "@vue/runtime-dom";
import {getModelPageApi} from "../../../api/modelApi.ts";
import {useGlobalFieldDataStore} from "../../../global/store/fieldStore.ts";
import {useGlobalServiceDataStore} from "../../../global/store/serviceStore.ts";
import Service from "../../../model/Service.ts";
import {PopperAPI} from "../../popover/my-popover.ts";
import {useDebounceFn} from '@vueuse/core'
import MyIcon from "../../icon/my-icon.vue";
import MyInnerPopover from "../../popover/my-inner-popover.vue";
import {borderStyleType} from "../../icon/my-icon.ts";
import ActionView from "../../../model/view/ActionView.ts";
import {getActionDownView} from "../../../api/commonApi.ts";
import {parserEx} from "../../../xml/XMLParser.ts";
import {getTemplate, XMLParserResult} from "../../../xml/XMLParserResult.ts";
import {getServiceField, hasJoin} from "../../../util/fieldUtils.ts";
import ShowField from "../../../model/ShowField.ts";
import MyTable from "../../table/my-table.vue";
import {FieldTypeEnum} from "../../../model/enum-type/FieldTypeEnum.ts";
import {getSelectionValueByServiceAndField} from "../../../cache/SelectionValueMemory.ts";

const serviceFieldStore = useGlobalFieldDataStore()
const serviceStore = useGlobalServiceDataStore()

const props = defineProps({
    htmlId: String,
    htmlName: String,
    required: Boolean,
    readonly: Boolean,
    service: {
        type: String,
        required: true
    },
    border: {
        type: borderStyleType,
        default: 'round'
    }
})
const serviceRef = ref<Service>()
const emit = defineEmits(['rightBtnClick'])

// 格式 {id:1,name:"显示"}
const formField = defineModel({
    type: FormField,
})
const labelValue = new FormField('');
labelValue.Field = formField.value?.Field
const labelField = ref<FormField>(labelValue)
const popperSelect = ref<PopperAPI | null>(null)

watch(() => formField.value?.value, () => {
    setValidate(true)
})

const options = ref<Record<string, any>[]>([])
let init = true // 需要初始化，格式化显示，这时候不用加载数据

onMounted(() => {
    if (props.service) {
        serviceRef.value = serviceStore.getServiceByName(props.service);
    }
    if (formField.value?.value) {
        if (init) {
            formatName()
            init = false
        }

    } else {
        initData()
    }
})
const view = ref<ActionView | undefined>(undefined)
getActionDownView(props.service).then(viewData => {
    if (viewData && viewData.length) {
        view.value = viewData[0]
        renderView();
    }
})

const initData = async () => {
    if (view.value) {
        loadDownData()
    } else {
        loadServiceOption()
    }
}

const renderView = async () => {
    if (view.value) {
        await parserXml(view.value.arch)
    }
}
const xmlTemplate = ref<any>(null)
let template_fields = ref<string[]>([]);
let template_full_fields = ref<string[]>([]);
let services_fields = ref<ShowField[]>([]);
let parserResult: XMLParserResult | null = null;

const parserXml = async (str: string) => {
    const primaryKeyField = await serviceStore.getServiceByNameAsync(props.service)
    const serviceFields = await serviceFieldStore.getFieldByServiceNameAsync(props.service)
    parserResult = await parserEx(str, props.service)
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
            field = await getServiceField(props.service, key);
        }
        if (field) {
            services_fields.value.push({Field: field, originField: key, serviceName: props.service})
        }
    }

}

const formatName = () => {
    if (formField.value?.value) {
        const find = formField.value?.value ? formField.value?.value : null
        if (find) {
            labelField.value.value = find[serviceRef.value?.nameField as string]
        }
    }
}

const record = ref<any>([])
const selectionDynamic = ref<any>({})

const rowClick = (row: any) => {
    if (formField.value) {
        formField.value.value = {...row}
    }
    if (labelField.value) {
        formatName()
    }
}

const loadServiceOption = async (name?: string) => {
    if (props.service && serviceRef.value) {
        getModelPageApi(`${serviceRef.value.keyField},${serviceRef.value.nameField}`,
            `('${serviceRef.value.nameField}',like,${name ? "'" + name + "'" : "''"})`,
            props.service,
            1, 20).then(pageInfo => {
            options.value.splice(0, options.value.length)
            options.value.push(...pageInfo.data)
            if (init) {
                formatName()
                init = false
            }
        })
    }
}

// 下载down 列表数据
const loadDownData = async (name?: string) => {
    const recordTemp: any = []
    if (!props.service || !serviceRef.value) {
        return recordTemp
    }
    await getModelPageApi(template_full_fields.value.join(","),
        `('${serviceRef.value.nameField}',like,${name ? "'" + name + "'" : "''"})`,
        props.service,
        1, 20).then(pageInfo => {
        if (pageInfo.data) {
            recordTemp.push(...pageInfo.data)
        }

    })
    for (let field of services_fields.value) {
        if (field.Field.type == FieldTypeEnum.SelectionField) { // 得到字段对应的selection的值
            selectionDynamic.value[field.Field.name] = await getSelectionValueByServiceAndField(props.service, field.Field.name)
        }
    }
    record.value.splice(0, record.value.length);
    record.value.push(...recordTemp)
}


const setValidate = (valid: boolean) => {
    if (formField.value) {
        formField.value.isValidate = valid
    }
}

const validate = (): boolean => {
    if (!props.required) {
        setValidate(true)
        return true;
    }
    setValidate(!!formField.value?.value)
    return formField.value ? formField.value.isValidate : false
}


const optionSelectClick = (key: Record<string, any>) => {
    if (formField.value) {
        formField.value.value = {...key}
    }
    if (labelField.value) {
        formatName()
    }
}
const labelInput = (e: any) => {
    const nameValue = e.target.value
    popperSelect.value?.show();
    doLabelInput(nameValue)
}
const doLabelInput = useDebounceFn((value: string) => {
    if (!view.value) {
        loadServiceOption(value)
    } else {
        loadDownData(value)
    }
    if (!value) {
        if (formField.value) {
            formField.value.value = null
        }
    }
}, 500)

const popperShow = () => {
    if (!view.value) {
        options.value.splice(0, options.value.length)
        loadServiceOption()
    } else {
        record.value.splice(0, record.value.length)
        loadDownData()
    }

}
defineExpose<InputExpose>({validate})

</script>

<template>
    <div class="flex relative">
        <MyInnerPopover placement="bottom" trigger="click" full-width ref="popperSelect" @popperShow="popperShow">
            <template v-slot:default>
                <div class="inline-flex w-full relative">
                    <input
                        :class="['form-input-control','flex-1','w-full', 'rounded',{'form-input-control-error': !labelField.isValidate,
                        'border':border == 'round', 'border-b':border == 'bottom'}]"
                        style="padding-right: 25px"
                        v-if="labelField"
                        type="text"
                        v-model="labelField.value" :id="htmlId"
                        @input="labelInput"
                        :name="htmlName">
                    <MyIcon class="absolute right-[10px] top-[50%]" style="transform: translateY(-50%)"
                            icon="caret-down"
                            type="fas" size="sm"></MyIcon>
                </div>

            </template>
            <template v-slot:option>
                <div class="flex flex-col w-full" v-if="!view">
                    <div v-for="(value,index) in options" :key="index"
                         class="w-full cursor-pointer hover:bg-select-hover px-4"
                         @click="optionSelectClick(value)">
                        {{ value[serviceRef?.nameField as string] }}
                    </div>
                    <div v-if="options.length == 0" class="w-full px-4">
                        无匹配数据
                    </div>
                </div>
                <div v-else>
                    <MyTable height="100%" :record="record" :fields="services_fields" :service-name="service"
                             @rowClick="rowClick">
                    </MyTable>
                </div>
            </template>
        </MyInnerPopover>
    </div>


</template>

<style scoped>

</style>