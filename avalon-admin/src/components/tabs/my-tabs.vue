<!-- Tabs.vue -->
<script setup lang="ts">
import { computed, useSlots, onMounted, nextTick } from 'vue'

interface TabsProps {
  type?: '' |'card' | 'border-card'
  tabPosition?: 'top' | 'right' | 'bottom' | 'left'
  stretch?: boolean
  closable?: boolean
  addable?: boolean
  editable?: boolean
  tabType?: 'card' | 'border-card'
  beforeLeave?: (activeName: string | number, oldActiveName: string | number) => boolean | Promise<boolean>
}

const props = withDefaults(defineProps<TabsProps>(), {
  type: '',
  tabPosition: 'top',
  stretch: false,
  closable: false,
  addable: false,
  editable: false
})

const modelValue = defineModel<string | number>({default: ''})

const emit = defineEmits<{
  (e: 'tab-change', name: string | number): void
  (e: 'tab-click', tab: any, event: Event): void
  (e: 'tab-remove', name: string | number): void
  (e: 'tab-add'): void
  (e: 'edit', targetName: string | number, action: 'remove' | 'add'): void
}>()

const slots = useSlots()

// 自动设置第一个 tabpane 为默认值
onMounted(async () => {
  await nextTick()
  if (!modelValue.value && slots.default) {
    // 获取所有 tabpane 子组件
    const tabPanes = slots.default()
    if (tabPanes && tabPanes.length > 0) {
      // 找到第一个有效的 tabpane
      for (const pane of tabPanes) {
        if (pane.props && pane.props.name) {
          modelValue.value = pane.props.name
          break
        }
      }
    }
  }
})

// 处理标签页变化
const handleTabChange = (name: string | number) => {
  emit('tab-change', name)
}

// 处理标签页点击
const handleTabClick = (tab: any, event: Event) => {
  emit('tab-click', tab, event)
}

// 处理标签页移除
const handleTabRemove = (name: string | number) => {
  emit('tab-remove', name)
}

// 处理添加标签页
const handleTabAdd = () => {
  emit('tab-add')
}

// 处理编辑操作
const handleEdit = (targetName: string | number, action: 'remove' | 'add') => {
  emit('edit', targetName, action)
}

// 计算 el-tabs 的属性
const tabsProps = computed(() => ({
  type: props.tabType || props.type,
  tabPosition: props.tabPosition,
  stretch: props.stretch,
  closable: props.closable,
  addable: props.addable,
  editable: props.editable,
  beforeLeave: props.beforeLeave
}))
</script>

<template>
  <el-tabs
    v-bind="tabsProps"
    v-model="modelValue"
    @tab-change="handleTabChange"
    @tab-click="handleTabClick"
    @tab-remove="handleTabRemove"
    @tab-add="handleTabAdd"
    @edit="handleEdit"
  >
    <slot />
  </el-tabs>
</template>


