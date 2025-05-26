<script setup lang="ts">
import {computed, ref} from 'vue'
import { useTheme } from '../../util/useTheme.ts'
import Switch from '../switch/my-switch.vue'
import FormField from "../../model/FormField.ts";

const { toggleTheme,theme } = useTheme()

// 太阳和月亮图标，支持 font-awesome 或自定义 svg
const activeIcon = computed(() => ['fas', 'moon'] as [string, string])    // dark
const inactiveIcon = computed(() => ['fas', 'sun'] as [string, string])   // light

const value = ref<FormField>(new FormField(theme.value))

const handleChange = () => {
  toggleTheme()
}
</script>

<template>
  <Switch
      @change="handleChange"
      v-model="value"
      active-value="dark"
      inactive-value="light"
      :active-icon="activeIcon"
      :active-icon-type="'fas'"
      :inactive-icon="inactiveIcon"
      :inactive-icon-type="'fas'"
      :size="'default'"
      :width="44"
      :name="'theme-switch'"
      :aria-label="'主题切换'"
  />
</template>
