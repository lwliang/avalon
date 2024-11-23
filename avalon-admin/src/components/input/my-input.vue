<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import './my-input.css'
import FormField from "../../model/FormField.ts";
import {watch} from "vue";
import {iconStyleType} from "../icon/my-icon.ts";
import MyButton from "../button/my-button.vue";

const props = defineProps({
    htmlId: String,
    htmlName: String,
    required: Boolean,
    readonly: Boolean,
    icon: String,
    iconColor: {
        type: String,
        default: "#FFF"
    },
    iconStyle: {
        type: iconStyleType,
        default: 'far'
    },
    rightContent: String,
    suffixIcon: {
        type: String,
        required: false
    },
    suffixIconStyle: {
        type: iconStyleType,
        default: 'far'
    },
    pattern: String,
    inputWidth: {
        type: String,
        required: false
    }
})

const inputStyle = {
    width: props.inputWidth || undefined
}

const emit = defineEmits(['rightBtnClick', 'valueChange'])

const formField = defineModel({
    type: FormField,
    required: true
})

watch(() => formField.value?.value, () => {
    setValidate(true)
})

const setValidate = (valid: boolean) => {
    if (formField.value) {
        formField.value.isValidate = valid
    }
}

const validate = () => {
    if (props.required) { // 必填
        if (formField.value && !formField.value.value) {
            formField.value.isValidate = false
            return false;
        }
    }

    if (props.pattern) { // 数据规格匹配
        const regex = new RegExp(props.pattern);
        if (formField.value) {
            if (!regex.test(formField.value.value)) {
                setValidate(false)
                return false;
            }
        }
    }

    setValidate(!!formField.value?.value)
    return formField.value?.isValidate
}

const rightBtnClick = () => {
    emit('rightBtnClick')
}

const valueChange = () => {
    emit('valueChange', formField.value.value)
}

defineExpose({validate})

</script>

<template>
    <div class="flex relative">
        <div class="absolute top-1/2 -translate-y-1/2 pl-2">
            <MyIcon v-if="suffixIcon" :icon="suffixIcon"
                    :type="suffixIconStyle"></MyIcon>
        </div>
        <input
            :style="[{'paddingLeft':  suffixIcon ? '30px' : '0.75rem'},inputStyle]"
            :class="['form-input-control','flex-1', {'form-input-control-error': !formField.isValidate,'rounded-l':!!icon, 'rounded':!icon}]"
            v-if="formField"
            type="text"
            v-model="formField.value" :id="htmlId" :readonly="readonly"
            @change="valueChange"
            :name="htmlName">
        <MyButton @click="rightBtnClick" type="info" v-if="icon" :icon="icon" :icon-style="iconStyle" class="rounded-r"
                  :icon-color="iconColor">{{ rightContent }}
        </MyButton>
    </div>


</template>

<style scoped>

</style>