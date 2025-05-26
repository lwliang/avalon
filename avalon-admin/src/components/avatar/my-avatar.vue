<script setup lang="ts">
import {computed, ref} from 'vue'
import type {AvatarProps} from './types'
import myIcon from '../icon/my-icon.vue'

const props = withDefaults(defineProps<AvatarProps>(), {
  size: 'default',
  fit: 'cover',
  shape: 'square'
})

const emit = defineEmits<{
  (e: 'error', ev: Event): void
}>()

const hasLoadError = ref(false)
const imgShow = computed(() => props.src && !hasLoadError.value)

const sizeMap = {large: 50, default: 40, small: 24}
const avatarSize = computed(() =>
    typeof props.size === 'number' ? props.size : sizeMap[props.size] ?? sizeMap['default']
)

const textSize = computed(() =>
    props.size === 'large' ? 'text-lg'
        : props.size === 'small' ? 'text-sm'
            : 'text-default'
)

const radiusClass = computed(() =>
    props.shape === 'circle' ? 'rounded-full' : 'rounded-lg'
)

const avatarClass = computed(() => [
  'inline-flex items-center justify-center overflow-hidden select-none relative',
  radiusClass.value,
  'bg-background',
  'text-text',
  textSize.value
])

const avatarStyle = computed(() => ({
  width: `${avatarSize.value}px`,
  height: `${avatarSize.value}px`
}))

const objectFit = computed(() => props.fit ?? 'cover')

const getIconProps = computed(() => {
  if (!props.icon) return null
  if (Array.isArray(props.icon))
    return {icon: props.icon, type: props.iconType, size: Math.floor(avatarSize.value * 0.5) + 'px', color: "inherit"}
  return {icon: props.icon, type: props.iconType, size: Math.floor(avatarSize.value * 0.5) + 'px', color: "inherit"}
})

function handleError(e: Event) {
  hasLoadError.value = true
  emit('error', e)
}
</script>

<template>
  <span :class="avatarClass" :style="avatarStyle">
    <img
        v-if="imgShow"
        :src="props.src"
        :style="{ objectFit }"
        class="absolute w-full h-full left-0 top-0"
        @error="handleError"
        alt="avatar"
    />
    <span v-else class="w-full h-full flex items-center justify-center">
      <slot>
        <my-icon v-if="getIconProps" v-bind="getIconProps as any"/>
      </slot>
    </span>
  </span>
</template>
