<script setup lang="ts">
import { computed } from 'vue'

interface DropdownItemProps {
  command?: string | number | object
  disabled?: boolean
  divided?: boolean
  icon?: string
  size?: 'large' | 'default' | 'small'
}

const props = withDefaults(defineProps<DropdownItemProps>(), {
  disabled: false,
  divided: false,
  size: 'default'
})

const emit = defineEmits<{
  (e: 'click', command: string | number | object): void
}>()

// 处理点击事件
const handleClick = (event: Event) => {
  if (props.disabled) return
  emit('click', props.command || '')
}

// 计算 el-dropdown-item 的属性
const dropdownItemProps = computed(() => ({
  command: props.command,
  disabled: props.disabled,
  divided: props.divided,
  size: props.size
}))
</script>

<template>
  <el-dropdown-item
    v-bind="dropdownItemProps"
    @click="handleClick"
  >
    <template v-if="$slots.icon || props.icon" #icon>
      <slot name="icon">
        <el-icon v-if="props.icon">
          <component :is="props.icon" />
        </el-icon>
      </slot>
    </template>
    <slot />
  </el-dropdown-item>
</template>
