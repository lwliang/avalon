<script setup lang="ts">
import {computed} from 'vue'

const props = withDefaults(defineProps<{
  src: string
  fit?: '' | 'fill' | 'contain' | 'cover' | 'none' | 'scale-down'
  loading?: 'eager' | 'lazy'
  alt?: string
  preview?: boolean
  width?: number | string
  height?: number | string
  round?: boolean,
  radius?: number | string
}>(), {
  width: 50,
  height: 50,
  loading: 'lazy'
})

const emit = defineEmits<{
  (e: 'load', ev: Event): void
  (e: 'error', ev: Event): void
}>()

const handleLoad = (e: Event) => emit('load', e)
const handleError = (e: Event) => emit('error', e)

const imageStyle = computed(() => {
  const style: Record<string, any> = {}
  if (props.width !== undefined) style.width = typeof props.width === 'number' ? `${props.width}px` : props.width
  if (props.height !== undefined) style.height = typeof props.height === 'number' ? `${props.height}px` : props.height
  if (props.radius !== undefined) style.borderRadius = typeof props.radius === 'number' ? `${props.radius}px` : props.radius
  if (props.round) style.borderRadius = '9999px'
  return style
})

const previewSrcList = computed(() => {
  return props.preview && props.src ? [props.src] : []
})
</script>

<template>
  <el-image
    :src="props.src"
    :alt="props.alt"
    :fit="props.fit || 'cover'"
    :style="imageStyle"
    :preview-src-list="previewSrcList"
    :initial-index="0"
    :hide-on-click-modal="false"
    :preview-teleported="true"
    @load="handleLoad"
    @error="handleError"
    v-if="props.src"
  >
    <template #error>
      <div class="image-error">
        <el-icon><Picture /></el-icon>
        <span>加载失败</span>
      </div>
    </template>
    
    <template #placeholder>
      <div class="image-placeholder">
        <el-icon class="is-loading"><Loading /></el-icon>
        <span>加载中...</span>
      </div>
    </template>
  </el-image>
</template>

<style scoped>
.image-error,
.image-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  background-color: var(--el-fill-color-lighter);
  color: var(--el-text-color-secondary);
  font-size: 12px;
}

.image-error .el-icon,
.image-placeholder .el-icon {
  font-size: 20px;
  margin-bottom: 4px;
}

.image-placeholder .is-loading {
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
</style>
