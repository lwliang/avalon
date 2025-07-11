<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {computed} from "vue";
import {ElDialog, ElButton} from 'element-plus'


const props = withDefaults(defineProps<{
  modelValue?: boolean
  title?: string
  width?: string | number
  fullscreen?: boolean
  top?: string
  modal?: boolean
  appendToBody?: boolean
  lockScroll?: boolean
  closeOnClickModal?: boolean
  closeOnPressEscape?: boolean
  showClose?: boolean
  beforeClose?: (done: () => void) => void
  draggable?: boolean
  center?: boolean
  destroyOnClose?: boolean
  bodyClass?: string
  bodyStyle?: object
  direction?: 'ltr' | 'rtl'
  closeIcon?: string
  openDelay?: number
  closeDelay?: number
  trapFocus?: boolean
  focusOnShow?: boolean
  focusOnHide?: boolean
  role?: string
  ariaLabel?: string
  ariaDescribedby?: string
  ariaLabelledby?: string
  ariaModal?: boolean
  zIndex?: number
  dialogClass?: string
  dialogStyle?: string | object
}>(), {
  modelValue: false,
  title: '',
  width: '50%',
  fullscreen: false,
  top: '15vh',
  modal: true,
  appendToBody: false,
  lockScroll: true,
  closeOnClickModal: true,
  closeOnPressEscape: true,
  showClose: true,
  draggable: false,
  center: false,
  destroyOnClose: false,
  direction: 'ltr',
  closeIcon: 'Close',
  openDelay: 0,
  closeDelay: 0,
  trapFocus: true,
  focusOnShow: true,
  focusOnHide: true,
  role: 'dialog',
  ariaModal: true,
  zIndex: 2000
})

const emit = defineEmits<{
  (e: 'update:modelValue', value: boolean): void
  (e: 'open'): void
  (e: 'opened'): void
  (e: 'close'): void
  (e: 'closed'): void
  (e: 'sure'): void
}>()

const dialogVisible = computed({
  get: () => props.modelValue,
  set: (val: boolean) => {
    emit('update:modelValue', val)
  }
})

const closeClick = () => {
  emit('close')
  dialogVisible.value = false
}

const sureClick = () => {
  emit('sure')
}
</script>

<template>
  <el-dialog
    v-model="dialogVisible"
    :title="title"
    :width="width"
    :fullscreen="fullscreen"
    :top="top"
    :modal="modal"
    :append-to-body="appendToBody"
    :lock-scroll="lockScroll"
    :close-on-click-modal="closeOnClickModal"
    :close-on-press-escape="closeOnPressEscape"
    :show-close="showClose"
    :before-close="beforeClose"
    :draggable="draggable"
    :center="center"
    :destroy-on-close="destroyOnClose"
    :body-class="bodyClass || dialogClass"
    :body-style="bodyStyle || dialogStyle"
    :direction="direction"
    :close-icon="closeIcon"
    :open-delay="openDelay"
    :close-delay="closeDelay"
    :trap-focus="trapFocus"
    :focus-on-show="focusOnShow"
    :focus-on-hide="focusOnHide"
    :role="role"
    :aria-label="ariaLabel"
    :aria-describedby="ariaDescribedby"
    :aria-labelledby="ariaLabelledby"
    :aria-modal="ariaModal"
    :z-index="zIndex"
    @open="() => emit('open')"
    @opened="() => emit('opened')"
    @close="closeClick"
    @closed="() => emit('closed')">
    <slot name="default"></slot>
    <template #footer>
      <template v-if="$slots.footer">
        <slot name="footer"></slot>
      </template>
      <template v-else>
        <el-button type="info" @click="closeClick">取消</el-button>
        <el-button type="primary" @click="sureClick">确认</el-button>
      </template>
    </template>
  </el-dialog>
</template>

<style scoped>

</style>