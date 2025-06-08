<script setup lang="ts">
import {ref, watch, nextTick, onMounted, onBeforeUnmount, computed, inject} from 'vue'
import {useFloating, offset, shift, flip, arrow, autoUpdate} from '@floating-ui/vue'
import {PopperProps} from "./types.ts";

const props = withDefaults(defineProps<PopperProps>(), {
  trigger: 'hover',
  placement: 'top',
  teleported: true,
  arrowShow: true,
  popperClass: 'py-1'
})

const emit = defineEmits(['show', 'before'])

const referenceRef = ref<HTMLElement | null>(null)
const floatingRef = ref<HTMLElement | null>(null)
const arrowRef = ref<HTMLElement | null>(null)
const open = ref(false)

const overlay = inject<any>('overlay', null)

const getTeleported = computed(() => {
  if (overlay) {
    return overlay.teleported;
  }

  return props.teleported;
})

const {floatingStyles, middlewareData, placement: actualPlacement} = useFloating(referenceRef, floatingRef, {
  placement: props.placement,
  middleware: [
    offset(8),
    flip(),
    shift(),
    arrow({element: arrowRef})
  ],
  whileElementsMounted: autoUpdate
})

function bindHoverEvents() {
  // reference
  if (referenceRef.value) {
    referenceRef.value.onmouseenter = () => {
      clearTimeout(leaveTimer)
      enterTimer = setTimeout(show, 80)
    }
    referenceRef.value.onmouseleave = () => {
      clearTimeout(enterTimer)
      leaveTimer = setTimeout(hide, 80)
    }
  }
  // floating
  if (floatingRef.value) {
    floatingRef.value.onmouseenter = () => {
      clearTimeout(leaveTimer)
      enterTimer = setTimeout(show, 80)
    }
    floatingRef.value.onmouseleave = () => {
      clearTimeout(enterTimer)
      leaveTimer = setTimeout(hide, 80)
    }
  }
}


// 事件响应
function show() {
  if (props.disabled) return
  emit('before')
  open.value = true
  nextTick(() => emit('show'))
}

function hide() {
  open.value = false
}

function toggle() {
  open.value ? hide() : show()
}

defineExpose({show, hide})

watch(() => props.visible, (val) => {
  open.value = !!val
})

let enterTimer: any, leaveTimer: any

function bindTriggerEvents(el: HTMLElement) {
  if (props.trigger === 'hover') {
    bindHoverEvents()
  } else if (props.trigger === 'click') {
    el.onclick = () => toggle()
    document.addEventListener('click', docHandler)
  } else if (props.trigger === 'focus') {
    el.onfocus = () => show()
    el.onblur = () => hide()
  } else if (props.trigger === 'contextmenu') {
    el.oncontextmenu = (e) => {
      e.preventDefault()
      show()
    }
    document.addEventListener('click', hide)
  }
}

function docHandler(e: MouseEvent) {
  if (!referenceRef.value?.contains(e.target as Node) && !floatingRef.value?.contains(e.target as Node)) {
    hide()
  }
}

onMounted(() => {
  if (referenceRef.value) {
    bindTriggerEvents(referenceRef.value)
  }

  // 监听浮层出现后再绑定浮层事件
  watch(open, (val) => {
    if (val && props.trigger === 'hover') {
      nextTick(() => {
        if (floatingRef.value) {
          bindHoverEvents()
        }
      })
    }
  })
})
onBeforeUnmount(() => {
  document.removeEventListener('click', docHandler)
})


const arrowPlacement = ref(props.placement)
watch(arrowPlacement, (val) => {
  if (arrowRef.value) {
    arrowRef.value.setAttribute('data-placement', val)
  }
})
const arrowStyle = computed(() => {
  const data = middlewareData.value.arrow || {} as any
  const placementStr = actualPlacement.value
  let style: Record<string, string> = {}
  if (data.x != null) style.left = `${data.x}px`
  if (data.y != null) style.top = `${data.y}px`
  // 让箭头紧贴弹层
  if (placementStr.startsWith('top')) {
    style.bottom = '-5px'
    arrowPlacement.value = 'top'
  } else if (placementStr.startsWith('bottom')) {
    style.top = '-5px'
    arrowPlacement.value = 'bottom'
  } else if (placementStr.startsWith('left')) {
    style.right = '-5px'
    arrowPlacement.value = 'left'
  } else if (placementStr.startsWith('right')) {
    style.left = '-5px'
    arrowPlacement.value = 'right'
  }
  return style
})

const popperMergedStyle = computed(() => {
  const base = {
    ...floatingStyles.value,
    width: typeof props.width === 'number' ? props.width + 'px' : props.width
  }
  if (!props.popperStyle) return base
  if (typeof props.popperStyle === 'string') {
    // 如果是字符串，合并到数组
    return [base, props.popperStyle]
  }
  // 是对象，则合并
  return {...base, ...props.popperStyle}
})
</script>

<template>
  <div ref="referenceRef" :class="['inline-block',defaultClass]">
    <slot/>
  </div>
  <Teleport v-if="getTeleported" to="body">
    <Transition
        enter-active-class="transition-opacity duration-150"
        leave-active-class="transition-opacity duration-150"
        enter-from-class="opacity-0"
        enter-to-class="opacity-100"
        leave-from-class="opacity-100"
        leave-to-class="opacity-0"
    >
      <div
          v-show="open && !disabled"
          ref="floatingRef"
          :style="popperMergedStyle"
          :class="[
              'z-50 rounded border border-solid border-border bg-background shadow-[0_2px_12px_0_rgba(0,0,0,0.16)]',
          popperClass
          ]"
          role="tooltip"
      >
        <div v-if="title" class="font-bold mb-1">{{ title }}</div>
        <div v-if="content">{{ content }}</div>
        <slot name="content"/>

        <div :data-placement="arrowPlacement" v-if="arrowShow" ref="arrowRef" :style="arrowStyle"
             class="arrow-popper absolute">
        </div>
      </div>
    </Transition>
  </Teleport>
  <!-- 非 teleport 模式同理 -->
  <Transition
      enter-active-class="transition-opacity duration-150"
      leave-active-class="transition-opacity duration-150"
      enter-from-class="opacity-0"
      enter-to-class="opacity-100"
      leave-from-class="opacity-100"
      leave-to-class="opacity-0"
  >
    <div
        v-if="!getTeleported && open && !disabled"
        ref="floatingRef"
        :style="popperMergedStyle"
        :class="[
              'z-50 rounded border border-solid border-border bg-background shadow-[0_2px_12px_0_rgba(0,0,0,0.16)]',
          popperClass
          ]"
        role="tooltip"
    >
      <div v-if="title" class="font-bold mb-1">{{ title }}</div>
      <div v-if="content">{{ content }}</div>
      <slot name="content"/>
      <div :data-placement="arrowPlacement" v-if="arrowShow" ref="arrowRef" :style="arrowStyle"
           class="arrow-popper absolute">

      </div>
    </div>
  </Transition>
</template>

<style scoped>
.arrow-popper {
  width: 10px;
  height: 10px;
  z-index: -1;
}

.arrow-popper::before {
  position: absolute;
  width: 10px;
  height: 10px;
  z-index: -1;
  content: " ";
  transform: rotate(45deg);
  @apply border border-solid border-border;
  background: var(--background-page);
  box-sizing: border-box;
}

.arrow-popper[data-placement=top]::before {
  border-top-color: transparent !important;
  border-left-color: transparent !important;
}

.arrow-popper[data-placement=bottom]::before {
  border-bottom-color: transparent !important;
  border-right-color: transparent !important;
}

.arrow-popper[data-placement=left]::before {
  border-left-color: transparent !important;
  border-bottom-color: transparent !important;
}

.arrow-popper[data-placement=right]::before {
  border-right-color: transparent !important;
  border-top-color: transparent !important;
}
</style>
