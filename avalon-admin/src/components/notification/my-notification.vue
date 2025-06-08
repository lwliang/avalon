<script setup lang="ts">
import {computed, watch, onBeforeUnmount} from "vue";
import type {NotificationProps} from "./types";
import MyIcon from "../icon/my-icon.vue";

const props = withDefaults(defineProps<NotificationProps>(), {
  type: 'info',
  duration: 4500,
  position: 'top-right',
  showClose: true,
  offset: 0
})

const emit = defineEmits<{
  (e: 'remove'): void
}>()

let timer: number | null = null

const startTimer = () => {
  if (props.duration > 0) {
    timer = window.setTimeout(() => {
      timer = null
      emit('remove')
    }, props.duration)
  }
}
const clearTimer = () => {
  if (timer !== null) {
    clearTimeout(timer)
    timer = null
  }
}

watch(() => props.duration, () => {
  clearTimer()
  startTimer()
}, {immediate: true})

onBeforeUnmount(() => clearTimer())

// 图标选择
const icon = computed(() => {
  switch (props.type) {
    case 'success':
      return 'circle-check'
    case 'warning':
      return 'circle-exclamation'
    case 'danger':
      return 'circle-xmark'
    case 'info':
      return 'circle-info'
    default:
      return 'circle-info'
  }
})
// 主题色映射
const typeBgClass = computed(() => {
  switch (props.type) {
    case 'success':
      return 'bg-background border-success'
    case 'warning':
      return 'bg-background border-warning'
    case 'danger':
      return 'bg-background border-danger'
    case 'info':
      return 'bg-background border-info'
    default:
      return 'bg-background border-info'
  }
})
const typeTextClass = computed(() => {
  switch (props.type) {
    case 'success':
      return 'text-success'
    case 'warning':
      return 'text-warning'
    case 'danger':
      return 'text-danger'
    case 'info':
      return 'text-info'
    default:
      return 'text-info'
  }
})
const iconBgClass = computed(() => {
  switch (props.type) {
    case 'success':
      return 'bg-success-light-7'
    case 'warning':
      return 'bg-warning-light-7'
    case 'danger':
      return 'bg-danger-light-7'
    case 'info':
      return 'bg-info-light-7'
    default:
      return 'bg-info-light-7'
  }
})
const positionClass = computed(() => {
  switch (props.position) {
    case 'top-right':
      return 'top-0 right-6'
    case 'top-left':
      return 'top-0 left-6'
    case 'bottom-right':
      return 'bottom-0 right-6'
    case 'bottom-left':
      return 'bottom-0 left-6'
    default:
      return 'top-0 right-6'
  }
})
const offsetStyle = computed(() => {
  const offset = `${props.offset}px`
  if (props.position && props.position.includes('top')) return {top: offset}
  else return {bottom: offset}
})
const closeClick = () => {
  clearTimer()
  emit('remove')
}
</script>

<template>
  <transition name="notification-fade" appear>
    <div
        class="fixed z-50 min-w-[320px] max-w-[420px] px-4 py-3 mb-4 flex items-start rounded shadow-lg pointer-events-auto border border-solid bg-background"
        :class="[positionClass, typeBgClass]"
        :style="offsetStyle"
        @mouseenter="clearTimer"
        @mouseleave="startTimer"
    >
      <div :class="['rounded-full w-9 h-9 flex items-center justify-center', iconBgClass]">
        <MyIcon :class="[typeTextClass, 'text-lg']" type="fas" :icon="icon"/>
      </div>
      <div class="flex-1 px-4">
        <div class="font-bold text-text-primary text-base" v-if="props.title">{{ props.title }}</div>
        <div class="text-text-regular text-sm mt-0.5" v-if="props.message">{{ props.message }}</div>
        <slot/>
      </div>
      <button v-if="props.showClose" class="ml-3 mt-1 p-1 text-text-secondary hover:text-text-primary"
              @click="closeClick">
        <MyIcon icon="xmark" type="fas"/>
      </button>
    </div>
  </transition>
</template>

<style scoped>
.notification-fade-enter-active,
.notification-fade-leave-active {
  transition: opacity 0.3s, transform 0.3s;
}

.notification-fade-enter-from,
.notification-fade-leave-to {
  opacity: 0;
  transform: translateY(-10px) scale(0.98);
}

.notification-fade-enter-to,
.notification-fade-leave-from {
  opacity: 1;
  transform: translateY(0) scale(1);
}
</style>
