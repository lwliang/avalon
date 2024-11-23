<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import FormField from "../../model/FormField.ts";
import {watch} from "vue";

const props = defineProps({
    htmlId: String,
    htmlName: String,
    required: Boolean,
    readonly: Boolean
})


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

    setValidate(!!formField.value?.value)
    return formField.value?.isValidate
}


defineExpose({validate})

</script>

<template>
    <div class="w-full relative">
        <textarea
            style="min-height: 80px"
            :class="['form-input-control', 'w-full', {'form-input-control-error': !formField.isValidate}]"
            v-if="formField"
            type="checkbox"
            v-model="formField.value" :id="htmlId" :readonly="readonly"
            :name="htmlName">
        </textarea>
    </div>


</template>

<style scoped>

</style>