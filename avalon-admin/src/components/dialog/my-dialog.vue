<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import './my-dialog.css'
import MyIcon from "../icon/my-icon.vue";
import MyButton from "../button/my-button.vue";
import myOverlay from '../overlay/my-overlay.vue';
import {useDraggable} from '@vueuse/core'
import {computed, nextTick, ref, watch} from "vue";

const props = withDefaults(defineProps<{
  zIndex?: number,
  title: string,
  show: boolean,
  dialogClass?: string
  dialogStyle?: string | object
}>(), {
  zIndex: 2000
})

const dialogRef = ref<HTMLElement | null>(null)
const position = ref({x: 0, y: 0})
// 只允许通过头部拖动
const dragHandle = ref<HTMLElement | null>(null)

useDraggable(dialogRef, {
  initialValue: position.value,
  handle: dragHandle,
  onMove({x, y}) {
    position.value.x = x
    position.value.y = y
  },
})

const emit = defineEmits(['close', 'sure'])

function centerDialog() {
  nextTick(() => {
    const dialog = dialogRef.value;
    if (dialog) {
      const {width, height} = dialog.getBoundingClientRect();
      const x = (window.innerWidth - width) / 2;
      const y = (window.innerHeight - height) / 2;
      position.value = {x, y};
    }
  });
}

// 只在show变true时居中一次
watch(
    () => props.show,
    (val, oldVal) => {
      if (val && !oldVal) {
        centerDialog();
      }
    }, {immediate: true}
);
const closeClick = () => {
  emit('close')
}

const sureClick = () => {
  emit('sure')
}

const mergedStyle = computed(() => {
  // 兼容 string/object/array
  let style = {}
  if (Array.isArray(props.dialogStyle)) {
    style = [{zIndex: props.zIndex}, ...props.dialogStyle].filter(Boolean)
  }
  if (props.dialogStyle) {
    style = [{zIndex: props.zIndex}, props.dialogStyle]
  }

  return [style, {
    left: position.value.x + 'px',
    top: position.value.y + 'px',
  }]
})

</script>

<template>
  <myOverlay :show="show">
    <div ref="dialogRef" :class="['dialog min-w-[500px] pl-4 pr-4 pb-4 bg-background flex flex-col flex-shrink-0',dialogClass]"
         :style="mergedStyle">
      <div class="dialog-dragger h-4" :style="{ cursor: 'move' }" ref="dragHandle">

      </div>
      <div class="dialog-header flex">
        <div class="dialog-title flex-1 select-none">
          {{ title }}
        </div>
        <div class="dialog-close pr-2">
          <MyIcon class="cursor-pointer" icon="xmark" type="fas" @click="closeClick"></MyIcon>
        </div>
      </div>
      <div class="dialog-content flex-1">
        <slot name="default"></slot>
      </div>
      <div class="dialog-footer flex justify-end">
        <template v-if="$slots.footer">
          <slot name="footer"></slot>
        </template>
        <template v-else>
          <MyButton type="info" rounded @click="closeClick">取消</MyButton>
          <MyButton class="ml-3" type="primary" rounded @click="sureClick">确认</MyButton>
        </template>
      </div>
    </div>
  </myOverlay>
</template>

<style scoped>

</style>