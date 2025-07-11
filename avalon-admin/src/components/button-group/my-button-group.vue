<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import type {ButtonGroupProps} from './types'
import {provide} from 'vue'

const props = defineProps<ButtonGroupProps>()

// 提供按钮组属性给子组件
provide('buttonGroupProps', {
  size: props.size,
  type: props.type
})
</script>

<template>
  <el-button-group
    :size="size"
    :type="type">
    <slot/>
  </el-button-group>
</template>

<style scoped>

</style>