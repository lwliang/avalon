<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import './my-password.css'
import FormField from "../../model/FormField.ts";
import {watch} from "vue";

const props = defineProps({
    htmlId: String,
    htmlName: String,
    required: Boolean,
})

const formField = defineModel({
    type: FormField,
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
    if (!props.required) {
        setValidate(true)
        return true;
    }
    setValidate(!!formField.value?.value)
    return formField.value?.isValidate
}

defineExpose({validate})
</script>

<template>
    <input :class="['form-input-control','rounded', {'form-input-control-error': !formField.isValidate}]"
           v-if="formField"
           type="password" v-model="formField.value" :id="htmlId"
           :name="htmlName">
</template>

<style scoped>

</style>