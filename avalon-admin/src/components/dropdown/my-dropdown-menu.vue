<script setup lang="ts">
import { computed } from 'vue'

interface DropdownMenuProps {
  size?: 'large' | 'default' | 'small'
  maxHeight?: string | number
}

const props = withDefaults(defineProps<DropdownMenuProps>(), {
  size: 'default'
})

const emit = defineEmits<{
  (e: 'command', command: string | number | object): void
}>()

// 处理命令事件
const handleCommand = (command: string | number | object) => {
  emit('command', command)
}

// 计算 el-dropdown-menu 的属性
const dropdownMenuProps = computed(() => ({
  size: props.size,
  maxHeight: props.maxHeight
}))
</script>

<template>
  <el-dropdown-menu
    v-bind="dropdownMenuProps"
    @command="handleCommand"
  >
    <slot />
  </el-dropdown-menu>
</template>
