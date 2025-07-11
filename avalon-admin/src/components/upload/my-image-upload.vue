<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import { computed, ref } from "vue";
import { getFileUploadUrl } from "../../api/env.ts";
import { uploadImage } from "../../api/fileUploadApi.ts";
import { ElMessage } from 'element-plus';
import type { UploadProps, UploadFile } from 'element-plus';

const props = defineProps({
    htmlId: String,
    htmlName: String,
    required: Boolean,
    readonly: Boolean,
})

const modelValue = defineModel<string>({
    type: String,
    default: ''
})

const mainUploadRef = ref()
const hiddenUploadRef = ref()
const isUploading = ref(false)

// 获取图片显示URL
const getImageUrl = computed(() => {
    if (modelValue.value) {
        return getFileUploadUrl(modelValue.value)
    }
    return ''
})

// 是否有图片
const hasImage = computed(() => {
    return !!modelValue.value
})

// 预览图片列表
const previewSrcList = computed(() => {
    return hasImage.value ? [getImageUrl.value] : []
})

// 上传前的检查
const beforeUpload: UploadProps['beforeUpload'] = (file) => {
    const isImage = file.type.startsWith('image/')
    const isLt2M = file.size / 1024 / 1024 < 2

    if (!isImage) {
        ElMessage.error('只能上传图片文件!')
        return false
    }
    if (!isLt2M) {
        ElMessage.error('上传图片大小不能超过 2MB!')
        return false
    }
    return true
}

// 直接上传文件到服务器
const uploadFile = async (file: File) => {
    try {
        isUploading.value = true
        const data = await uploadImage(file)
        if (data && data.url) {
            modelValue.value = data.url
        }
    } catch (error) {
        ElMessage.error('上传失败')
    } finally {
        isUploading.value = false
    }
}

// 文件选择改变 - 直接上传
const handleChange: UploadProps['onChange'] = async (file) => {
    if (file.raw && beforeUpload(file.raw)) {
        await uploadFile(file.raw)
    }
}

// 文件移除
const handleRemove = () => {
    modelValue.value = ''
}

// 重新上传
const handleReupload = () => {
    const currentUploadRef = hasImage.value ? hiddenUploadRef.value : mainUploadRef.value
    if (currentUploadRef) {
        const input = currentUploadRef.$el.querySelector('input[type="file"]')
        if (input) {
            input.click()
        }
    }
}

// 验证函数
const validate = () => {
    if (!props.required) {
        return true;
    }
    return !!modelValue.value
}

defineExpose({ validate })
</script>

<template>
    <div class="image-upload-container">
        <!-- 没有图片时的上传区域 -->
        <el-upload
            v-if="!hasImage"
            ref="mainUploadRef"
            class="upload-area"
            :auto-upload="false"
            :show-file-list="false"
            accept="image/*"
            :before-upload="beforeUpload"
            :on-change="handleChange"
            :disabled="readonly || isUploading"
        >
            <div class="upload-placeholder">
                <el-icon class="upload-icon" v-if="!isUploading">
                    <Plus />
                </el-icon>
                <el-icon class="upload-icon" v-else>
                    <Loading />
                </el-icon>
                <div class="upload-text">
                    {{ isUploading ? '上传中...' : '点击上传图片' }}
                </div>
            </div>
        </el-upload>

        <!-- 有图片时的显示区域 -->
        <div v-else class="image-display">
            <el-image
                :src="getImageUrl"
                fit="cover"
                class="uploaded-image"
                :preview-src-list="previewSrcList"
            />
            
            <!-- 操作按钮 - 顶部左右分布 -->
            <div class="action-buttons">
                <el-tooltip content="重新上传" placement="top">
                    <el-button
                        type="warning"
                        :icon="'Edit'"
                        circle
                        size="small"
                        @click.stop="handleReupload"
                        :disabled="readonly || isUploading"
                        class="action-btn action-btn--left"
                    />
                </el-tooltip>
                
                <el-tooltip content="删除" placement="top">
                    <el-button
                        type="danger"
                        :icon="'Delete'"
                        circle
                        size="small"
                        @click.stop="handleRemove"
                        :disabled="readonly || isUploading"
                        class="action-btn action-btn--right"
                    />
                </el-tooltip>
            </div>
        </div>

        <!-- 隐藏的上传组件用于重新上传 -->
        <el-upload
            v-if="hasImage"
            ref="hiddenUploadRef"
            style="display: none"
            :auto-upload="false"
            :show-file-list="false"
            accept="image/*"
            :before-upload="beforeUpload"
            :on-change="handleChange"
            :disabled="readonly || isUploading"
        />
    </div>
</template>

<style scoped>
.image-upload-container {
    position: relative;
    width: 94px;
    height: 94px;
}

.upload-area {
    width: 100%;
    height: 100%;
}

.upload-area :deep(.el-upload) {
    width: 100%;
    height: 100%;
    border: 2px dashed var(--el-border-color);
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
}

.upload-area :deep(.el-upload:hover) {
    border-color: var(--el-color-primary);
}

.upload-placeholder {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100%;
    background-color: var(--el-fill-color-lighter);
}

.upload-icon {
    font-size: 24px;
    color: var(--el-text-color-secondary);
    margin-bottom: 4px;
}

.upload-icon:deep(.el-icon) {
    animation: rotate 1s linear infinite;
}

@keyframes rotate {
    from {
        transform: rotate(0deg);
    }
    to {
        transform: rotate(360deg);
    }
}

.upload-text {
    font-size: 12px;
    color: var(--el-text-color-secondary);
    text-align: center;
    line-height: 1.2;
}

.image-display {
    position: relative;
    width: 100%;
    height: 100%;
    border-radius: 6px;
    overflow: hidden;
    cursor: pointer;
}

.uploaded-image {
    width: 100%;
    height: 100%;
    border-radius: 6px;
}

.action-buttons {
    position: absolute;
    top: 4px;
    left: 0;
    right: 0;
    width: 100%;
    height: 0;
    z-index: 10;
    opacity: 0;
    transition: opacity 0.3s ease;
}

.image-display:hover .action-buttons {
    opacity: 1;
}

.action-btn {
    position: absolute;
    top: 0;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
    backdrop-filter: blur(4px);
    transition: all 0.3s ease;
}

.action-btn--left {
    left: 4px;
    right: auto;
}

.action-btn--right {
    right: 4px;
    left: auto;
}

/* 响应式设计 */
@media (max-width: 768px) {
    .action-buttons {
        top: 2px;
    }
    
    .action-btn--left {
        left: 2px;
    }
    
    .action-btn--right {
        right: 2px;
    }
    
    .action-btn {
        transform: scale(0.8);
    }
}

/* 禁用状态样式 */
.upload-area :deep(.el-upload.is-disabled) {
    cursor: not-allowed;
    opacity: 0.6;
}

.upload-area :deep(.el-upload.is-disabled:hover) {
    border-color: var(--el-border-color);
}
</style>