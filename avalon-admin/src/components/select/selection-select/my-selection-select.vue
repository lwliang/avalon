<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import './my-selection-select.css'
import {ref, watch} from "vue";
import FormField from "../../../model/FormField.ts";
import {InputExpose} from "../../../global/input/InputExpose.ts";
import {getSelectionValueByServiceAndField} from "../../../cache/SelectionValueMemory.ts";
import MyPopover from "../../popover/my-popover.vue";
import {onMounted} from "@vue/runtime-dom";
import MyIcon from "../../icon/my-icon.vue";

const props = defineProps({
    htmlId: String,
    htmlName: String,
    required: Boolean,
    readonly: Boolean,
    service: String,
    field: String
})

const emit = defineEmits(['rightBtnClick'])

const formField = defineModel({
    type: FormField,
})
const labelValue = new FormField('');
labelValue.Field = formField.value?.Field
const labelField = ref<FormField>(labelValue)

watch(() => formField.value?.value, () => {
    setValidate(true)
})

const options = ref<Record<string, string>>({})

onMounted(() => {
    loadSelectionOptions();
})

const loadSelectionOptions = () => {
    if (props.service && props.field) {
        getSelectionValueByServiceAndField(props.service, props.field).then(data => {
            Object.assign(options.value, data)
            if (formField.value) {
                labelField.value.value = options.value[formField.value.value]
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
    if (labelField.value) {
        labelField.value.value = options.value[key]
    }
    if (formField.value) {
        formField.value.value = key
    }
}

defineExpose<InputExpose>({validate})

</script>

<template>
    <div class="flex relative">
        <MyPopover placement="bottom" trigger="click" :option="options" full-width @itemSelect="optionSelectClick">
            <div class="inline-flex w-full relative">
                <input
                    :class="['form-input-control','flex-1','w-full', 'rounded',{'form-input-control-error': !labelField.isValidate}]"
                    style="padding-right: 25px"
                    v-if="labelField"
                    type="text"
                    v-model="labelField.value" :id="htmlId" :readonly="true"
                    :name="htmlName">
                <MyIcon class="absolute right-[10px] top-[50%]" style="transform: translateY(-50%)" icon="caret-down"
                        type="fas" size="sm"></MyIcon>
            </div>
        </MyPopover>
    </div>


</template>

<style scoped>

</style>