<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { useTheme } from '../../util/useTheme.ts'

const { toggleTheme, theme } = useTheme()

// 主题状态
const isDark = ref(theme.value === 'dark')

// 监听主题变化
watch(() => theme.value, (newTheme) => {
  isDark.value = newTheme === 'dark'
})

// 处理主题切换
const handleChange = (value: boolean) => {
  toggleTheme()
}

// 计算 switch 属性
const switchProps = computed(() => ({
  modelValue: isDark.value,
  activeValue: true,
  inactiveValue: false,
  size: 'default',
  width: 44,
  name: 'theme-switch',
  'aria-label': '主题切换'
}))
</script>

<template>
  <el-switch
    v-bind="switchProps"
    @change="handleChange"
    class="theme-switch"
  >
    <template #icon>
      <el-icon v-if="isDark" class="dark-icon">
        <Moon />
      </el-icon>
      <el-icon v-else class="light-icon">
        <Sunny />
      </el-icon>
    </template>
  </el-switch>
</template>

<style scoped>
.theme-switch {
  --el-switch-on-color: var(--el-color-primary);
  --el-switch-off-color: var(--el-color-info-light-3);
}

.theme-switch :deep(.el-switch__core) {
  border-radius: 12px;
}

.theme-switch :deep(.el-switch__action) {
  border-radius: 50%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.theme-switch :deep(.el-switch__inner) {
  font-size: 12px;
  color: #fff;
}

.theme-switch :deep(.dark-icon) {
  color: #fff;
  font-size: 12px;
}

.theme-switch :deep(.light-icon) {
  color: #fff;
  font-size: 12px;
}

/* 暗色主题下的样式调整 */
:global(.dark) .theme-switch {
  --el-switch-on-color: var(--el-color-primary);
  --el-switch-off-color: var(--el-color-info-light-5);
}
</style>
