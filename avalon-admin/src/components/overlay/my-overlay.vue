<script setup lang="ts">
import {withDefaults, defineProps, defineEmits, computed, provide} from 'vue'

const props = withDefaults(defineProps<{
  show?: boolean
  zIndex?: number | string
  overlayClass?: string
  overlayStyle?: string | object
  closeOnClick?: boolean
  teleport?: boolean
}>(), {
  show: false,
  zIndex: 2000,
  closeOnClick: true,
  teleport: true,
})

const emit = defineEmits<{
  (e: 'click'): void
  (e: 'close'): void
}>()

function onClickOverlay() {
  emit('click')
  if (props.closeOnClick) emit('close')
}

const mergedStyle = computed(() => {
  // 兼容 string/object/array
  if (Array.isArray(props.overlayStyle)) {
    return [{zIndex: props.zIndex}, ...props.overlayStyle].filter(Boolean)
  }
  if (props.overlayStyle) {
    return [{zIndex: props.zIndex}, props.overlayStyle]
  }
  return {zIndex: props.zIndex}
})

provide('overlay', {
  teleported: false
})

</script>

<template>
  <Teleport v-if="teleport" to="body">
    <transition name="overlay-fade">
      <div
          v-show="show"
          class="overlay"
          :class="overlayClass"
          :style="mergedStyle"
          @click="onClickOverlay"
      >
        <slot/>
      </div>
    </transition>
  </Teleport>
  <transition name="overlay-fade" v-else>
    <div
        v-show="show"
        class="overlay"
        :class="overlayClass"
        :style="mergedStyle"
        @click="onClickOverlay"
    >
      <slot/>
    </div>
  </transition>
</template>

<style scoped>
.overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.5);
  transition: opacity 0.2s;
}

/* 动画可自定义 */
.overlay-fade-enter-active,
.overlay-fade-leave-active {
  transition: opacity 0.2s;
}

.overlay-fade-enter-from,
.overlay-fade-leave-to {
  opacity: 0;
}

.overlay-fade-enter-to,
.overlay-fade-leave-from {
  opacity: 1;
}
</style>
