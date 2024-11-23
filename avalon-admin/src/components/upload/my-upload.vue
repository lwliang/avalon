<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {computed, ref} from "vue";
import MyImage from "../image/my-image.vue";
import FormField from "../../model/FormField.ts";
import {getFileUploadUrl} from "../../api/env.ts";

const props = defineProps({
    htmlId: String,
    htmlName: String,
    required: Boolean,
    readonly: Boolean,
})

const file = ref<HTMLElement | null>(null)
const selectFileClick = () => {
    if (file.value) {
        file.value.click();
    }
}

const fileBlob = ref<File>()
const fileUrl = ref<string>()

const fileChange = (event: any) => {
    fileBlob.value = event.target.files[0]
    fileUrl.value = URL.createObjectURL(event.target.files[0]);
    formField.value.value = event.target.files[0];
}

const getImageUrl = computed(() => {
    if (formField.value.value) {
        if (formField.value.value instanceof File) {
            if (fileUrl.value) {
                return fileUrl.value
            } else {
                fileUrl.value = URL.createObjectURL(formField.value.value);
                return fileUrl.value;
            }
        } else {
            return getFileUploadUrl(formField.value.value)
        }
    }
    return formField.value.value
})

const formField = defineModel({
    type: FormField,
    required: true
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
    <div class="w-[94px] h-[94px] bgc flex justify-center items-center rounded cursor-pointer overflow-hidden"
         @click="selectFileClick">
        <img v-if="!formField.value" src="/upload.png" alt="" width="30" height="30">
        <MyImage v-if="formField.value" :src="getImageUrl" alt="上传图片" width="94" height="94"/>
        <input type="file" hidden="hidden" ref="file" @change="fileChange">
    </div>
</template>

<style scoped>

</style>