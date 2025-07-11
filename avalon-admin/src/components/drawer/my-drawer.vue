<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {computed} from 'vue'

const props = withDefaults(defineProps<{
  modelValue?: boolean
  title?: string
  size?: string | number
  direction?: 'rtl' | 'ltr' | 'ttb' | 'btt'
  withHeader?: boolean
  modal?: boolean
  appendToBody?: boolean
  lockScroll?: boolean
  closeOnClickModal?: boolean
  closeOnPressEscape?: boolean
  showClose?: boolean
  beforeClose?: (done: () => void) => void
  destroyOnClose?: boolean
  customClass?: string
  customStyle?: object
  openDelay?: number
  closeDelay?: number
  zIndex?: number
  focusOnShow?: boolean
  focusOnHide?: boolean
  role?: string
  ariaLabel?: string
  ariaDescribedby?: string
  ariaLabelledby?: string
  ariaModal?: boolean
  show?: boolean
  overlayClass?: string
  overlayStyle?: string | object
  closeOnClick?: boolean
  teleport?: boolean
}>(), {
  modelValue: false,
  title: '',
  size: '30%',
  direction: 'rtl',
  withHeader: true,
  modal: true,
  appendToBody: false,
  lockScroll: true,
  closeOnClickModal: true,
  closeOnPressEscape: true,
  showClose: true,
  destroyOnClose: false,
  openDelay: 0,
  closeDelay: 0,
  focusOnShow: true,
  focusOnHide: true,
  role: 'dialog',
  ariaModal: true,
  show: false,
  closeOnClick: true,
  teleport: true
})

const emit = defineEmits<{
  (e: 'update:modelValue', value: boolean): void
  (e: 'open'): void
  (e: 'opened'): void
  (e: 'close'): void
  (e: 'closed'): void
}>()

const drawerVisible = computed({
  get: () => props.modelValue || props.show,
  set: (val: boolean) => {
    emit('update:modelValue', val)
  }
})

// 将自定义属性映射到 el-drawer 属性
const drawerDirection = computed(() => {
  const directionMap: Record<string, string> = {
    'rtl': 'rtl',
    'ltr': 'ltr',
    'ttb': 'ttb',
    'btt': 'btt'
  }
  return directionMap[props.direction] || 'rtl'
})

const drawerSize = computed(() => {
  if (typeof props.size === 'number') {
    return `${props.size}px`
  }
  return props.size
})
</script>

<template>
  <el-drawer
    v-model="drawerVisible"
    :title="title"
    :size="drawerSize"
    :direction="drawerDirection"
    :with-header="withHeader"
    :modal="modal"
    :append-to-body="appendToBody"
    :lock-scroll="lockScroll"
    :close-on-click-modal="closeOnClickModal"
    :close-on-press-escape="closeOnPressEscape"
    :show-close="showClose"
    :before-close="beforeClose"
    :destroy-on-close="destroyOnClose"
    :custom-class="customClass"
    :custom-style="customStyle"
    :open-delay="openDelay"
    :close-delay="closeDelay"
    :z-index="zIndex"
    :focus-on-show="focusOnShow"
    :focus-on-hide="focusOnHide"
    :role="role"
    :aria-label="ariaLabel"
    :aria-describedby="ariaDescribedby"
    :aria-labelledby="ariaLabelledby"
    :aria-modal="ariaModal"
    @open="() => emit('open')"
    @opened="() => emit('opened')"
    @close="() => emit('close')"
    @closed="() => emit('closed')">
    <slot name="default"></slot>
    <template #footer>
      <slot name="footer"></slot>
    </template>
  </el-drawer>
</template>

<style scoped>

</style>
