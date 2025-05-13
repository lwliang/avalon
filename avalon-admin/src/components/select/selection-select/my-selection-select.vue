<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import './my-selection-select.css'
import {ref, watch,computed} from "vue";
import FormField from "../../../model/FormField.ts";
import {InputExpose} from "../../../global/input/InputExpose.ts";
import {getSelectionValueByServiceAndField} from "../../../cache/SelectionValueMemory.ts";
import {onMounted} from "@vue/runtime-dom";
import MyIcon from "../../icon/my-icon.vue";
import MyInnerPopover from "../../popover/my-inner-popover.vue";
import {borderStyleType} from "../../icon/my-icon.ts";

const props = defineProps({
    htmlId: String,
    htmlName: String,
    required: Boolean,
    readonly: Boolean,
    service: String,
    field: String,
    border: {
        type: borderStyleType,
        default: 'round'
    }
})

const emit = defineEmits(['rightBtnClick'])

const formField = defineModel({
    type: FormField,
})
const labelValue = new FormField('');
labelValue.Field = formField.value?.Field
const labelField = ref<FormField>(labelValue)
const labelFields = ref<FormField[]>([]) // 多选时，labelField的值

watch(() => formField.value?.value, () => {
    setValidate(true)
    formatDisplay()
})

/**
 * 格式化显示
 */
const formatDisplay = () =>{
    if (labelField.value && labelField.value.value != options.value[formField.value?.value]) {
        if (formField.value?.Field && formField.value.Field.isMulti) { // 多选
            if(formField.value?.value) {
                const values = formField.value?.value.split(',')
                labelFields.value.splice(0, labelFields.value.length)
                for (const value of values) {
                    labelFields.value.push(new FormField({id: value, name: options.value[value]}, formField.value?.Field))
                }
            }
        } else { // 单选
            labelField.value.value = options.value[formField.value?.value]
        }
    }
}

const options = ref<Record<string, string>>({})

const computedOptions = computed(() => {
    const result:any = {}
    for(const key in  options.value) {
        if(labelFields.value.findIndex(field => field.value.id === key) < 0) {
            result[key] = options.value[key]
        }
    }
    return result
})

onMounted(() => {
    loadSelectionOptions();
})

const loadSelectionOptions = () => {
    if (props.service && props.field) {
        getSelectionValueByServiceAndField(props.service, props.field).then(data => {
            Object.assign(options.value, data)
            if (formField.value) {
                formatDisplay()
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


const optionSelectClick = (key: string) => {
    if (formField.value?.Field?.isMulti) { // 多选
        labelFields.value.push(new FormField({id: key, name: options.value[key]}, formField.value?.Field))
        labelField.value.value = null;
        if (formField.value) {
            formField.value.value = labelFields.value.map(field => field.value.id).join(',')
        }
    } else { // 单选
        if (labelField.value) {
            labelField.value.value = options.value[key]
        }
        if (formField.value) {
            formField.value.value = key
        }
    }
}

const deleteTagClick = (value: any) => {
    const index = labelFields.value.findIndex(field => field.value.id === value)
    if (index >= 0) {
        labelFields.value.splice(index, 1)
    }
    if (formField.value) {
        formField.value.value = labelFields.value.map(field => field.value.id).join(',')
    }
}

const tagClick = (value: any) => {
}

defineExpose<InputExpose>({validate})

</script>

<template>
    <div class="flex relative">
        <MyInnerPopover placement="bottom" trigger="click" :option="computedOptions" full-width @itemSelect="optionSelectClick">
            <div class="inline-flex w-full relative">
                <div class="flex flex-wrap gap-1" v-if="formField?.Field?.isMulti">
                    <template v-for="(fieldValue,index) in labelFields" :key="index">
                        <MyTag :close="!readonly" :round="true" 
                            :label="fieldValue.value.name"
                            :value="fieldValue.value.id" @deleteTag="deleteTagClick" @tagClick="tagClick"/>
                    </template>
                </div>
                <input
                    :class="['form-input-control','flex-1','w-full', 'rounded',{'form-input-control-error': !labelField.isValidate,
                    'border':border == 'round', 'border-b':border == 'bottom'}]"
                    style="padding-right: 25px"
                    v-if="labelField"
                    type="text"
                    v-model="labelField.value" :id="htmlId" :readonly="true"
                    :name="htmlName">
                <MyIcon class="absolute right-[10px] top-[50%]" style="transform: translateY(-50%)" icon="caret-down"
                        type="fas" size="sm"></MyIcon>
            </div>
        </MyInnerPopover>
    </div>


</template>

<style scoped>

</style>