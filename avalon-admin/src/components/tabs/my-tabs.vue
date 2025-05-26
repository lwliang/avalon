<!-- Tabs.vue -->
<script setup lang="ts">
import {computed, ref, useSlots, watchEffect, onMounted, nextTick, watch} from 'vue'

const props = withDefaults(defineProps<{
  type?: 'default' | 'card' | 'border-card'
  tabPosition?: 'top' | 'right' | 'bottom' | 'left'
  stretch?: boolean
}>(), {
  type: 'default',
  tabPosition: 'top',
  stretch: false
})

const modelValue = defineModel<string | number>()

const emit = defineEmits<{
  (e: 'tab-change', name: string | number): void
}>()

const slots = useSlots()
const panels = computed(() =>
    (slots.default?.() ?? [])
        .filter(i =>
            typeof i.type === 'object' &&
            i.type &&
            'name' in i.type &&
            (i.type as any).name === 'TabPanel'
        )
        .map(vnode => ({
          vnode,
          props: vnode.props as any
        }))
)

// 当前 tab
const currentName = ref<string | number>(
    modelValue.value ??
    panels.value.find(i => !i.props.disabled)?.props.name
)

watchEffect(() => {
  if (modelValue.value !== undefined) currentName.value = modelValue.value
  else if (currentName.value === undefined && panels.value.length > 0)
    currentName.value = panels.value.find(i => !i.props.disabled)?.props.name
})

function setActiveTab(name: string | number, disabled: boolean) {
  if (disabled) return
  if (currentName.value !== name) {
    currentName.value = name
    emit('tab-change', name)
  }
}

// ----------- tab indicator（仅 default 类型下） ---------------
const tabRefs = ref<Array<HTMLElement | null>>([])
const indicatorLeft = ref(0)
const indicatorTop = ref(0)
const indicatorWidth = ref(0)
const indicatorHeight = ref(0)
const ulRef = ref<HTMLElement | null>(null)
const ulRefHeight = ref(0)
const ulRefWight = ref(0)

function updateIndicator() {
  nextTick(() => {
    if (props.type !== 'default') return
    const idx = panels.value.findIndex(panel => panel.props.name === currentName.value)
    const el = tabRefs.value[idx]
    if (el && ulRef.value) {
      const ulRect = ulRef.value.getBoundingClientRect()
      ulRefHeight.value = ulRect.height
      ulRefWight.value = ulRect.width
      const rect = el.getBoundingClientRect()
      indicatorLeft.value = rect.left - ulRect.left
      indicatorTop.value = rect.top - ulRect.top
      indicatorWidth.value = rect.width
      indicatorHeight.value = rect.height
    }
  })
}

onMounted(updateIndicator)
watch([currentName, panels], updateIndicator)
// --------------------------------------------------------------

const renderTabClass = (panel: any, idx: number) => {
  const active = panel.props.name === currentName.value
  const disabled = !!panel.props.disabled

  if (props.type === 'default') {
    return [
      'px-5 h-11 flex items-center text-default font-normal select-none relative',
      disabled
          ? 'cursor-not-allowed text-text-disabled'
          : (active
                  ? 'text-primary'
                  : 'text-text hover:text-primary transition-colors'
          ),
    ]
  }
  if (props.type === 'card') {
    return [
      // 卡片外观，易叠合
      'px-5 h-11 flex items-center text-default font-normal select-none border border-solid border-border bg-background relative top-[1px] transition-colors duration-200',
      disabled
          ? 'cursor-not-allowed text-text-disabled bg-background-disabled border-solid border-border-disabled'
          : active
              ? 'text-primary tab-card-active z-10 bg-background   z-10 '
              : 'text-text border-solid border-border hover:text-primary',
      // 合并并去除双线
      idx !== 0 ? '-ml-px' : ''
    ]
  }
  if (props.type === 'border-card') {
    return [
      'px-6 py-2 text-default border border-border  transition-all',
      disabled
          ? 'cursor-not-allowed text-text-disabled border-border-disabled'
          : (
              active
                  ? 'bg-background border-primary text-primary'
                  : ' border-border text-text hover:text-primary hover:bg-primary-light-7'
          ),
    ]
  }
  return []
}

const tabPositionClass = () => {
  if (props.tabPosition === 'top') {
    return []
  }

  if (props.tabPosition === 'bottom') {
    return []
  }

  if (props.tabPosition === 'left') {
    return [
      'flex',
      'flex-1'
    ]
  }

  if (props.tabPosition === 'right') {
    return []
  }
}


const tabPanelPositionClass = () => {
  if (props.tabPosition === 'top') {
    return []
  }

  if (props.tabPosition === 'bottom') {
    return []
  }

  if (props.tabPosition === 'left') {
    return [
      'flex-1',
    ]
  }

  if (props.tabPosition === 'right') {
    return []
  }
}

const tabIndicatorClass = () => {
  if (props.tabPosition === 'top') {
    return ["absolute bottom-0 left-0 h-[2px] bg-primary transition-all duration-200"]
  }

  if (props.tabPosition === 'bottom') {
    return []
  }

  if (props.tabPosition === 'left') {
    return [
      "absolute top-0 left-0 w-[2px] bg-primary transition-all duration-200"
    ]
  }

  if (props.tabPosition === 'right') {
    return []
  }
}

const tabIndicatorStyle = computed(() => {
  if (props.tabPosition === 'top') {
    return {
      width: indicatorWidth.value + 'px', transform: `translateX(${indicatorLeft.value}px)`
    }
  }

  if (props.tabPosition === 'bottom') {
    return {
      width: indicatorWidth.value + 'px', transform: `translateX(${indicatorLeft.value}px)`
    }
  }

  if (props.tabPosition === 'left') {
    return {
      height: indicatorHeight.value + 'px', transform: `translateY(${indicatorTop.value}px)`
    }
  }

  if (props.tabPosition === 'right') {
    return []
  }
})

const tabUlStyle = computed(() => {
  if (props.tabPosition === 'top') {
    return {}
  }

  if (props.tabPosition === 'bottom') {
    return {}
  }

  if (props.tabPosition === 'left') {
    return {
      width: (ulRefWight.value + 2) + 'px'
    }
  }

  if (props.tabPosition === 'right') {
    return []
  }
})
</script>

<template>
  <div
      class="tabs bg-background"
      :class="[
      `tabs-type-${type}`,
      tabPositionClass(),
      stretch && 'w-full',
      (type === 'border-card') ? 'border border-border border-solid' : '',
    ]"
  >
    <!-- Tab 导航栏 -->
    <div :class="['relative',
            (tabPosition === 'left') ? 'flex flex-col h-full flex-nowrap w-fit' : ''] ">
      <ul
          ref="ulRef"
          :class="[
          'relative flex items-center flex-1',
          (type === 'card') ? 'border-b border-t-0 border-l-0 border-r-0 border-border border-solid' : '',
          (type === 'border-card') ? 'bg-background-component' : '',
          tabPosition === 'top' || tabPosition === 'bottom' ? 'flex-row' : 'flex-col'
        ]"
          style="list-style:none;margin:0;padding:0;"
      >
        <li
            v-for="(panel, idx) in panels"
            :key="panel.props.name"
            :aria-disabled="panel.props.disabled ? 'true' : undefined"
            :class="renderTabClass(panel, idx)"
            class="inline-block"
            @click="setActiveTab(panel.props.name, panel.props.disabled)"
            :ref="el => tabRefs[idx] = el as HTMLElement | null"
            style="min-width: 56px; text-align: left; cursor: pointer;"
        >
          {{ panel.props.label }}
        </li>
      </ul>
      <!-- tab-indicator，仅default有 -->
      <div v-if="type === 'default'" :class="[
      (tabPosition === 'top') ?'relative h-[2px] bg-background-base':'',
      (tabPosition === 'left') ?'w-[2px] bg-background-base flex-shrink-0 absolute top-0 right-0':'']"
           :style="{height: tabPosition === 'left' ? ulRefHeight+'px' : '2px'}">
        <div :class="[tabIndicatorClass()]"
             :style="tabIndicatorStyle"
        ></div>
      </div>
    </div>
    <!-- 内容区 -->
    <div
        class="tab-panel  bg-background"
        :class="[{ 'w-full': stretch },
        tabPanelPositionClass()]"
    >
      <component
          v-for="panel in panels"
          v-show="panel.props.name === currentName"
          :is="panel.vnode"
          :key="panel.props.name"
      />
    </div>
  </div>
</template>

<style scoped>
.tabs-type-default ul li,
.tabs-type-card ul li {
  transition: color 0.2s, border-color 0.2s, background-color 0.2s;
}

.tab-card-active {
  @apply relative;
  @apply overflow-visible
}

.tab-card-active::after {
  content: " ";
  position: absolute;
  left: 0;
  right: 0;
  bottom: -1px;
  height: 2px;
  background: var(--background-page);
  z-index: 2;
}

</style>
