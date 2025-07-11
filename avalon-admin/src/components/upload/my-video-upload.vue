<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {computed, ref} from "vue";
import {getVideoUploadUrl} from "../../api/env.ts";
import {uploadVideo} from "../../api/fileUploadApi.ts";
import {ElMessage} from 'element-plus';

const props = defineProps({
    htmlId: String,
    htmlName: String,
    required: Boolean,
    readonly: Boolean,
})

const file = ref<HTMLElement | null>(null)
const isUploading = ref(false)

const selectFileClick = () => {
    if (file.value && !isUploading.value && !props.readonly) {
        file.value.click();
    }
}

const modelValue = defineModel<string>({
    type: String,
    default: ''
})

// 直接上传视频到服务器
const uploadVideoFile = async (file: File) => {
    try {
        isUploading.value = true
        const data = await uploadVideo(file)
        if (data && data.url) {
            modelValue.value = data.url
            ElMessage.success('视频上传成功')
        } else {
            ElMessage.error('视频上传失败')
        }
    } catch (error) {
        console.error('上传失败:', error)
        ElMessage.error('视频上传失败')
    } finally {
        isUploading.value = false
    }
}

const fileChange = async (event: any) => {
    if (event.target.files.length === 0) {
        return
    }
    
    const selectedFile = event.target.files[0]
    if (selectedFile) {
        // 验证文件类型
        const isVideo = selectedFile.type.startsWith('video/')
        const isLt50M = selectedFile.size / 1024 / 1024 < 50

        if (!isVideo) {
            ElMessage.error('只能上传视频文件!')
            return
        }
        if (!isLt50M) {
            ElMessage.error('上传视频大小不能超过 50MB!')
            return
        }

        await uploadVideoFile(selectedFile)
    }
    // 清空 input 值，允许重复选择同一文件
    event.target.value = ''
}

const getVideoUrl = computed(() => {
    if (modelValue.value) {
        return getVideoUploadUrl(modelValue.value)
    }
    return ''
})

const validate = () => {
    if (!props.required) {
        return true;
    }
    return !!modelValue.value
}

const video_ref_id = ref(null)
const openVideo = () => {
    if (video_ref_id.value && modelValue.value) {
        const video = video_ref_id.value as any;
        video.requestFullscreen();
        video.play()
    }
}

const StopVideo = () => {
    if (video_ref_id.value && modelValue.value) {
        const video = video_ref_id.value as any;
        video.pause()
    }
}

defineExpose({validate})

</script>

<template>
    <div
        class="w-[94px] h-[94px] bgc flex justify-center items-center rounded cursor-pointer overflow-hidden border relative video-container border-border border-solid"
        :class="{ 'opacity-60 cursor-not-allowed': isUploading || readonly }"
        @click="selectFileClick">
        
        <!-- 上传中状态 -->
        <div v-if="isUploading" class="absolute inset-0 flex items-center justify-center bg-black bg-opacity-50 z-10">
            <el-icon class="text-white text-xl animate-spin">
                <Loading />
            </el-icon>
        </div>
        
        <!-- 默认上传图标 -->
        <img v-if="!modelValue && !isUploading" src="/upload.png" alt="" width="30" height="30">
        
        <!-- 已上传的视频 -->
        <video 
            ref="video_ref_id" 
            v-if="modelValue && !isUploading" 
            :src="getVideoUrl" 
            width="94" 
            height="94"
            preload="metadata"
        />
        
        <!-- 文件输入框 -->
        <input 
            accept="video/*" 
            type="file" 
            hidden="hidden" 
            ref="file" 
            @change="fileChange"
            :disabled="isUploading || readonly"
        >
        
        <!-- 视频控制按钮 -->
        <div class="hidden m-upload absolute right-0 top-0 px-1" v-if="modelValue && !isUploading">
            <el-button 
                type="primary" 
                size="small" 
                circle 
                @click.stop="openVideo"
                :icon="'VideoPlay'"
            />
            <el-button 
                type="warning" 
                size="small" 
                circle 
                @click.stop="StopVideo"
                :icon="'VideoPause'"
            />
        </div>
    </div>
</template>

<style scoped>
.video-container:hover .m-upload {
    @apply block;
}

.animate-spin {
    animation: spin 1s linear infinite;
}

@keyframes spin {
    from {
        transform: rotate(0deg);
    }
    to {
        transform: rotate(360deg);
    }
}
</style>