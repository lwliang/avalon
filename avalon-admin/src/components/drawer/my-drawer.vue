<script setup lang="ts">
import {computed, watch, ref} from 'vue'
import Overlay from '../overlay/my-overlay.vue'

const props = withDefaults(defineProps<{
  show?: boolean,
  zIndex?: number | string,
  overlayClass?: string,
  overlayStyle?: string | object,
  closeOnClick?: boolean,
  teleport?: boolean,

  // Drawer-specific
  title?: string,
  size?: string | number,
  beforeClose?: () => boolean | Promise<boolean>,
  showClose?: boolean,
  direction?: 'rtl' | 'ltr' | 'ttb' | 'btt'
}>(), {
  show: false,
  zIndex: 2000,
  closeOnClick: true,
  teleport: true,
  showClose: true,
  direction: 'rtl',
  size: 400
})

const emit = defineEmits(['open', 'close'])

const visible = ref(props.show)
watch(() => props.show, v => visible.value = v)
watch(visible, v => v && emit('open'))

function getDrawerStyle() {
  const size = typeof props.size === 'number' ? `${props.size}px` : props.size
  switch (props.direction) {
    case 'rtl':
      return {width: size, right: 0, top: 0, bottom: 0, height: '100vh'}
    case 'ltr':
      return {width: size, left: 0, top: 0, bottom: 0, height: '100vh'}
    case 'ttb':
      return {height: size, left: 0, top: 0, right: 0, width: '100vw'}
    case 'btt':
      return {height: size, left: 0, bottom: 0, right: 0, width: '100vw'}
    default:
      return {width: size, right: 0, top: 0, bottom: 0, height: '100vh'}
  }
}

function handleClose() {
  // beforeClose 支持异步
  if (props.beforeClose) {
    const result = props.beforeClose()
    if (result instanceof Promise) {
      result.then(ok => {
        if (ok !== false) close()
      })
      return
    }
    if (result === false) return
  }
  close()
}

function close() {
  visible.value = false
  emit('close')
}
</script>

<template>
  <Overlay
      :show="visible"
      :z-index="zIndex"
      :overlay-class="overlayClass"
      :overlay-style="overlayStyle"
      :close-on-click="closeOnClick"
      :teleport="teleport"
      @close="handleClose"
  >
    <transition
        :name="{
        rtl: 'drawer-slide-rtl',
        ltr: 'drawer-slide-ltr',
        ttb: 'drawer-slide-ttb',
        btt: 'drawer-slide-btt'
      }[direction]"
        appear
    >
      <div
          v-show="visible"
          class="my-drawer bg-background fixed shadow-lg flex flex-col"
          :style="getDrawerStyle()"
          @click.stop
      >
        <div
            class="my-drawer-header flex items-center px-4 py-2 border-t-0 border-l-0 border-r-0 border-b border-border border-solid">
          <div class="flex-1 font-bold text-base">{{ title }}</div>
          <my-icon icon="xmark" class="cursor-pointer hover:text-primary" type="fas" v-if="showClose" @click="handleClose"/>
        </div>
        <div class="my-drawer-body flex-1 overflow-auto p-4">
          <slot/>
        </div>
        <div class="my-drawer-footer px-4 py-2 border-b-0 border-l-0 border-r-0 border-t border-border border-solid">
          <slot name="footer"/>
        </div>
      </div>
    </transition>
  </Overlay>
</template>

<style scoped>
.my-drawer {
  /* will be set position/flex/size by style binding */
  transition: all 0.3s cubic-bezier(.25, .8, .25, 1);
  z-index: 1;
}

.drawer-slide-rtl-enter-from, .drawer-slide-rtl-leave-to {
  transform: translateX(100%);
}

.drawer-slide-rtl-enter-to, .drawer-slide-rtl-leave-from {
  transform: translateX(0);
}

.drawer-slide-ltr-enter-from, .drawer-slide-ltr-leave-to {
  transform: translateX(-100%);
}

.drawer-slide-ltr-enter-to, .drawer-slide-ltr-leave-from {
  transform: translateX(0);
}

.drawer-slide-ttb-enter-from, .drawer-slide-ttb-leave-to {
  transform: translateY(-100%);
}

.drawer-slide-ttb-enter-to, .drawer-slide-ttb-leave-from {
  transform: translateY(0);
}

.drawer-slide-btt-enter-from, .drawer-slide-btt-leave-to {
  transform: translateY(100%);
}

.drawer-slide-btt-enter-to, .drawer-slide-btt-leave-from {
  transform: translateY(0);
}
</style>
