<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {ref, computed, watch, useAttrs} from 'vue'
import MyIcon from '../icon/my-icon.vue'
import type {InputProps} from './types'
import FormField from "../../model/FormField.ts";

const props = defineProps<InputProps>()
const formField = defineModel<FormField>({required: true})
const attrs = useAttrs()

const emit = defineEmits<{
  (e: 'clear'): void
  (e: 'change', value: any): void
  (e: 'blur', event: FocusEvent): void
  (e: 'focus', event: FocusEvent): void
  (e: 'suffixIconClick'): void
  (e: 'keyup', event: KeyboardEvent): void
}>()

const inputRef = ref<HTMLInputElement>()

const baseClass = computed(() => [
  // 布局&基础
  'w-full',
  'appearance-none',
  'transition-all',
  'duration-150',
  'placeholder:text-text-placeholder',
  'focus:border-primary',
  'outline-none',
  // 交互/禁用/只读
  props.disabled
      ? 'bg-fill-disabled text-text-disabled cursor-not-allowed border-border-disabled'
      : 'text-text-regular',
  props.readonly ? 'bg-fill-blank text-text-disabled cursor-default' : 'text-text-regular'
])
const sizeClass = computed(() => {
  switch (props.size) {
    case 'large':
      return ['h-12', 'text-lg', 'px-5']
    case 'small':
      return ['h-7', 'text-sm', 'px-2']
    default:
      return ['h-9', 'text-base', 'px-4']
  }
})

/** 清空按钮点击 */
function onClear() {
  if (formField.value) {
    formField.value.value = undefined
    formField.value.isValidate = true
  }
  emit('clear')
}

const onInputChange = () => {
  emit('change', formField.value.value)
}


/** 失焦事件 */
function onBlur(e: any) {
  validate()
  emit('blur', e)
}

/** 校验逻辑 */
function validate(): boolean {
  if (!formField.value) return true
  if (props.required && !formField.value.value) {
    formField.value.isValidate = false
    return false
  }
  if (props.pattern) {
    const regex = new RegExp(props.pattern)
    if (!regex.test(formField.value.value)) {
      formField.value.isValidate = false
      return false
    }
  }
  formField.value.isValidate = true
  return true
}

/** 自动恢复校验 */
watch(() => formField.value?.value, () => {
  if (formField.value?.isValidate === false) {
    formField.value.isValidate = true
  }
})


const suffixIconClick = () => {
  emit('suffixIconClick')
}

defineExpose({validate})
</script>

<template>
  <div :class="[`relative flex items-center group overflow-hidden border border-solid
  border-border-base bg-background hover:border-primary input-container`,{'rounded-lg':true}]">
    <!-- 前缀图标 -->
    <span v-if="props.prefixIcon" class="absolute left-3 flex items-center text-text-placeholder">
      <MyIcon :icon="props.prefixIcon" :type="props.prefixIconType" size="sm"/>
    </span>

    <!-- 输入框 -->
    <input
        ref="inputRef"
        :type="props.type || 'text'"
        v-model="formField.value"
        :placeholder="props.placeholder"
        :class="[
          baseClass,
          sizeClass,
          props.prefixIcon ? 'pl-10' : '',
          props.suffixIcon || props.clearable ? 'pr-10' : '',
        ]"
        :disabled="props.disabled"
        :readonly="props.readonly"
        @change="onInputChange"
        @blur="onBlur"
        autocomplete="off"
        v-bind="attrs"
        @keyup="emit('keyup',$event)"
    />

    <!-- 清除按钮 -->
    <button
        v-if="props.clearable && formField.value?.value && !props.disabled && !props.readonly"
        class="absolute right-3 flex items-center text-text-placeholder hover:text-primary transition-colors"
        tabindex="-1"
        @click="onClear"
        type="button"
    >
      <MyIcon icon="times-circle" type="fas" size="sm"/>
    </button>

    <!-- 后缀图标 -->
    <span @click="suffixIconClick" v-if="props.suffixIcon"
          class="absolute right-3 flex items-center text-text-placeholder">
      <MyIcon :class="{'cursor-pointer':props.type == 'password'}" :icon="props.suffixIcon" :type="props.suffixIconType"
              size="sm"/>
    </span>
    <!-- append slot：紧挨输入框右侧，无缝衔接 -->
    <template v-if="$slots.append">
       <span
           :class="['inline-flex items-center justify-center h-9 px-4 bg-fill-base border-box transition']">
        <slot name="append"/>
    </span>
    </template>

  </div>
</template>

<style>
.input-container:focus-within {
  @apply border-primary;
}
</style>