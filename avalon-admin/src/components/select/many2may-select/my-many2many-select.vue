<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {ref, watch} from "vue";
import FormField from "../../../model/FormField.ts";
import {InputExpose} from "../../../global/input/InputExpose.ts";
import MyPopover from "../../popover/my-popover.vue";
import {onMounted} from "@vue/runtime-dom";
import {getModelPageApi} from "../../../api/modelApi.ts";
import {useGlobalFieldDataStore} from "../../../global/store/fieldStore.ts";
import {useGlobalServiceDataStore} from "../../../global/store/serviceStore.ts";
import Service from "../../../model/Service.ts";
import {PopperAPI} from "../../popover/my-popover.ts";
import {useDebounceFn} from '@vueuse/core'
import MyIcon from "../../icon/my-icon.vue";
import MyTag from "../../tag/my-tag.vue";
import Snowflake from "../../../model/Snowflake.ts";

const serviceFieldStore = useGlobalFieldDataStore()
const serviceStore = useGlobalServiceDataStore()

const props = defineProps({
    htmlId: String,
    htmlName: String,
    required: Boolean,
    readonly: Boolean,
    service: String,
    field: String
})
const service = ref<Service>()
const emit = defineEmits(['rightBtnClick'])

// 格式 {id:1,name:"显示"}
const formField = defineModel({
    type: FormField,
    required: true
})
const labelValue = new FormField('');
labelValue.Field = formField.value?.Field
const labelField = ref<FormField>(labelValue)
const popperSelect = ref<PopperAPI | null>(null)

const relativeServiceName = ref('')
const relativeServiceId = ref('')
if (formField.value.Field) {
    const serviceName = formField.value.Field?.relativeServiceName
    serviceStore.getServiceByNameAsync(serviceName).then(data => {
        relativeServiceName.value = data.nameField;
        relativeServiceId.value = data.keyField;
    })
}

watch(() => formField.value?.value, () => {
    setValidate(true)
})

const options = ref<Record<string, any>[]>([])
let init = true

onMounted(() => {
    if (props.service) {
        service.value = serviceStore.getServiceByName(props.service);
    }
    if (formField.value?.value) {
        if (init) {
            formatName()
            init = false
        }

    } else {
        loadServiceOption()
    }
})

const formatName = () => {
    if (formField.value?.value) {
        const find = formField.value?.value ? formField.value?.value : null
        if (find) {
            labelField.value.value = find[service.value?.nameField as string]
        }
    }
}

const loadServiceOption = (name?: string) => {
    if (props.service && service.value) {
        let condition = "";
        if (formField.value.value) {
            const values = []
            for (let tag of formField.value.value) {
                if (formField.value.Field) {
                    values.push(tag[formField.value.Field.relativeForeignKeyName][relativeServiceId.value])
                }
            }
            if (values.length) {
                if (formField.value.Field) {
                    condition = `(notIn,${relativeServiceId.value} , ${values.join()})`
                }
            }
        }
        if (condition) {
            condition = `(&,${condition},(like,${service.value.nameField},"${name ? name : ''}"))`
        } else {
            condition = `(like,${service.value.nameField},"${name ? name : ''}")`
        }
        getModelPageApi(`${service.value.keyField},${service.value.nameField}`,
            condition,
            props.service,
            1).then(pageInfo => {
            options.value.splice(0, options.value.length)
            options.value.push(...pageInfo.data)
            if (init) {
                formatName()
                init = false
            }
        })
    }
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
        if (formField.value.Field) {
            const option: any = {}
            option['id'] = Symbol(Snowflake.getNextId())
            option[formField.value.Field.relativeForeignKeyName] = {...key}
            formField.value.value.push(option)
        }
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
    loadServiceOption(value)
}, 500)

const popperShow = () => {
    options.value.splice(0, options.value.length)
    loadServiceOption()
}
const deleteTagClick = (tagValue: any) => {
    const tagIndex = formField.value.value.findIndex((x: any) => x.id === tagValue.id)
    if (tagIndex >= 0) {
        formField.value.value.splice(tagValue, 1)
    }
}
const tagClick = (tagValue: any) => {
    console.log("tagClick", tagValue)
}

const many2Click = () => {

}
defineExpose<InputExpose>({validate})

</script>

<template>
    <div class="flex relative" @click="many2Click">
        <div class="inline-flex w-full relative items-center flex-wrap gap-1">
            <template v-for="(fieldValue,index) in formField.value" :key="index">
                <MyTag :close="!readonly" :round="true" v-if="relativeServiceName && formField.Field"
                       :label="fieldValue[formField.Field.relativeForeignKeyName][relativeServiceName]"
                       :value="fieldValue" @deleteTag="deleteTagClick" @tagClick="tagClick"/>
            </template>
            <MyPopover v-if="!readonly" class="min-w-48" style="flex:1" placement="bottom" trigger="click" full-width
                       ref="popperSelect"
                       @popperShow="popperShow">
                <template v-slot:default>
                    <div class="inline-flex relative w-full">
                        <input
                            :class="['form-input-control','flex-1','w-full', 'rounded',{'form-input-control-error': !labelField.isValidate}]"
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
                    <div class="max-h-[400px] overflow-y-auto flex flex-col w-full">
                        <div v-for="(value,index) in options" :key="index"
                             class="w-full cursor-pointer hover:bg-gray-300 px-4"
                             @click="optionSelectClick(value)">
                            {{ value[service?.nameField as string] }}
                        </div>
                        <div v-if="options.length == 0" class="w-full px-4">
                            无匹配数据
                        </div>
                    </div>
                </template>
            </MyPopover>
        </div>

    </div>


</template>

<style scoped>

</style>