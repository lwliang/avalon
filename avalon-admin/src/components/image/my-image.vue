<script setup lang="ts">
import {ref, computed} from 'vue'

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

const showPreview = ref(false)
const handleLoad = (e: Event) => emit('load', e)
const handleError = (e: Event) => emit('error', e)

const fitStyle = computed(() => {
  const style: Record<string, any> = {objectFit: props.fit || undefined}
  if (props.width !== undefined) style.width = typeof props.width === 'number' ? `${props.width}px` : props.width
  if (props.height !== undefined) style.height = typeof props.height === 'number' ? `${props.height}px` : props.height
  if (props.radius !== undefined) style.borderRadius = typeof props.radius === 'number' ? `${props.radius}px` : props.radius
  if (props.round) style.borderRadius = '9999px'
  return style
})
</script>

<template>
  <div :style="fitStyle">
    <img
        :src="props.src"
        :alt="props.alt"
        :loading="props.loading"
        :style="fitStyle"
        @load="handleLoad"
        @error="handleError"
        v-if="props.src"
        @click="props.preview ? showPreview = true : undefined"
    />
    <div v-if="showPreview && props.preview"
         class="fixed inset-0 bg-black bg-opacity-60 flex items-center justify-center z-50"
         @click="showPreview=false"
    >
      <img :src="props.src" class="max-w-full max-h-full" :alt="alt"/>
    </div>
  </div>
</template>
