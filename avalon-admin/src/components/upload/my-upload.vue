<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {computed, ref} from "vue";
import MyImage from "../image/my-image.vue";
import {getFileUploadUrl} from "../../api/env.ts";
import {uploadImage} from "../../api/fileUploadApi.ts";
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

// 直接上传文件到服务器
const uploadFile = async (file: File) => {
  try {
    isUploading.value = true
    const data = await uploadImage(file)
    if (data && data.url) {
      modelValue.value = data.url
      ElMessage.success('图片上传成功')
    } else {
      ElMessage.error('图片上传失败')
    }
  } catch (error) {
    console.error('上传失败:', error)
    ElMessage.error('图片上传失败')
  } finally {
    isUploading.value = false
  }
}

const fileChange = async (event: any) => {
  const selectedFile = event.target.files[0]
  if (selectedFile) {
    // 验证文件类型
    const isImage = selectedFile.type.startsWith('image/')
    const isLt2M = selectedFile.size / 1024 / 1024 < 2

    if (!isImage) {
      ElMessage.error('只能上传图片文件!')
      return
    }
    if (!isLt2M) {
      ElMessage.error('上传图片大小不能超过 2MB!')
      return
    }

    await uploadFile(selectedFile)
  }
  // 清空 input 值，允许重复选择同一文件
  event.target.value = ''
}

const getImageUrl = computed(() => {
  if (modelValue.value) {
    return getFileUploadUrl(modelValue.value)
  }
  return ''
})

const validate = () => {
  if (!props.required) {
    return true;
  }
  return !!modelValue.value
}

defineExpose({validate})

</script>

<template>
  <div
      class="w-[94px] h-[94px] bg-background flex justify-center items-center rounded cursor-pointer overflow-hidden border border-border border-solid relative"
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
    
    <!-- 已上传的图片 -->
    <MyImage v-if="modelValue && !isUploading" :src="getImageUrl" alt="上传图片" width="94" height="94"/>
    
    <!-- 文件输入框 -->
    <input 
      type="file" 
      hidden="hidden" 
      ref="file" 
      @change="fileChange"
      accept="image/*"
      :disabled="isUploading || readonly"
    >
  </div>
</template>

<style scoped>
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