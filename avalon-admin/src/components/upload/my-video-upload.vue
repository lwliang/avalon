<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {computed, ref} from "vue";
import FormField from "../../model/FormField.ts";
import {getVideoUploadUrl} from "../../api/env.ts";

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
const videoUrl = ref<string>()

const fileChange = (event: any) => {
    if (event.target.files.length === 0) {
        return
    }
    fileBlob.value = event.target.files[0]
    videoUrl.value = URL.createObjectURL(event.target.files[0]);
    formField.value.value = event.target.files[0];
}

const getVideoUrl = computed(() => {
    if (formField.value.value) {
        if (formField.value.value instanceof File) {
            if (videoUrl.value) {
                return videoUrl.value
            } else {
                videoUrl.value = URL.createObjectURL(formField.value.value);
                return videoUrl.value;
            }
        } else {
            return getVideoUploadUrl(formField.value.value)
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

const video_ref_id = ref(null)
const openVideo = () => {
    if (video_ref_id.value && formField.value) {
        const video = video_ref_id.value as any;
        video.requestFullscreen();
        video.play()
    }
}

const StopVideo = () => {
    if (video_ref_id.value && formField.value) {
        const video = video_ref_id.value as any;
        video.pause()
    }
}

defineExpose({validate})

</script>

<template>
    <div
        class="w-[94px] h-[94px] bgc flex justify-center items-center rounded cursor-pointer overflow-hidden border relative video-container border-border border-solid"
        @click="selectFileClick">
        <img v-if="!formField.value" src="/upload.png" alt="" width="30" height="30">
        <video ref="video_ref_id" v-if="formField.value" :src="getVideoUrl" width="94" height="94"/>
        <input accept="video/*" type="file" hidden="hidden" ref="file" @change="fileChange">
        <div class="hidden m-upload absolute right-0 top-0 px-1" v-if="formField.value">
            <MyIcon @click.stop="openVideo" type="fas" icon="circle-play" class="mr-1"/>
            <MyIcon @click.stop="StopVideo" type="fas" icon="circle-pause"/>
        </div>
    </div>
</template>

<style scoped>
.video-container:hover .m-upload {
    @apply block;
}
</style>