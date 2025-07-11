<script setup lang="ts">
import {computed, ref} from 'vue'
import {PopperProps} from "./types.ts";

const props = withDefaults(defineProps<PopperProps>(), {
  trigger: 'hover',
  placement: 'top',
  teleported: true,
  arrowShow: true,
  popperClass: 'py-1'
})

const emit = defineEmits(['show', 'before', 'hide', 'after-hide'])

// 创建 popover 的 ref
const popoverRef = ref()
const popoverShow = ref(false)

// Element Plus placement 映射
const elPlacement = computed(() => {
  const placementMap = {
    'top': 'top',
    'top-start': 'top-start',
    'top-end': 'top-end',
    'bottom': 'bottom',
    'bottom-start': 'bottom-start',
    'bottom-end': 'bottom-end',
    'left': 'left',
    'left-start': 'left-start',
    'left-end': 'left-end',
    'right': 'right',
    'right-start': 'right-start',
    'right-end': 'right-end'
  }
  return placementMap[props.placement || 'top']
})

// Element Plus trigger 映射
const elTrigger = computed(() => {
  const triggerMap = {
    'click': 'click',
    'focus': 'focus',
    'hover': 'hover',
    'contextmenu': 'contextmenu'
  }
  return triggerMap[props.trigger || 'hover']
})

// 处理显示事件
const handleShow = () => {
  emit('show')
}

// 处理隐藏事件
const handleHide = () => {
  emit('hide')
}

// 处理显示前事件
const handleBeforeShow = () => {
  emit('before')
}

// 处理隐藏后事件
const handleAfterHide = () => {
  emit('after-hide')
}

// 暴露的方法
const show = () => {
  popoverShow.value = true
}

const hide = () => {
  popoverRef.value?.hide()
}

// 向外暴露方法
defineExpose({
  show,
  hide
})
</script>

<template>
  <el-popover
    ref="popoverRef"
    v-model="popoverShow"
    :placement="elPlacement"
    :trigger="elTrigger"
    :title="title"
    :width="width"
    :disabled="disabled"
    :teleported="teleported"
    :popper-class="popperClass"
    :popper-style="popperStyle"
    :show-arrow="arrowShow"
    :default-class="defaultClass"
    @show="handleShow"
    @hide="handleHide"
    @before-show="handleBeforeShow"
    @after-hide="handleAfterHide"
  >
    <template #reference>
        <slot name="reference" />
    </template>
    
    <template v-if="content">
      {{ content }}
    </template>
    <template v-else>
      <slot name="default" />
    </template>
  </el-popover>
</template>

<style scoped>
/* 可以根据需要添加自定义样式 */
</style>
