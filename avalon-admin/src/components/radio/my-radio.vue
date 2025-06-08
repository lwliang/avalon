<script setup lang="ts">
import {computed, ref, watch, inject, onMounted} from 'vue'
import type {RadioProps} from './types'
import FormField from "../../model/FormField.ts";

const props = withDefaults(defineProps<RadioProps>(), {
  disabled: false,
  border: false
})

// v-model=FormField（单Radio和RadioGroup都复用此接口）
const formField = defineModel<FormField>({required: false})

const emit = defineEmits<{
  (e: 'change', value: any): void
  (e: 'blur'): void
}>()

// 是否在RadioGroup模式
const group = inject<any>('radioGroupContext', null)
const isGroup = !!group
const size = computed(() => props.size || group?.size?.value || 'default')
const disabled = computed(() => (props.disabled ?? group?.disabled?.value) || false)

const isChecked = computed(() => {
  if (isGroup) {
    return group.checkedValue.value === props.value
  } else {
    return formField.value && formField.value.value === props.value
  }
})

const handleChange = () => {
  if (disabled.value) return
  if (isGroup) {
    group.change(props.value)
  } else {
    if (formField.value) {
      formField.value.value = props.value
    }
  }
  emit('change', props.value)
}

// 样式相关
const sizeClass = computed(() => {
  switch (size.value) {
    case 'large':
      return 'w-6 h-6 text-lg'
    case 'small':
      return 'w-4 h-4 text-sm'
    default:
      return 'w-5 h-5 text-default'
  }
})

const borderClass = computed(() =>
    props.border
        ? 'border border-border border-solid p-2 rounded'
        : 'border-0'
)

const stateClass = computed(() => {
  if (disabled.value) {
    return isChecked.value
        ? 'text-text-disabled cursor-not-allowed bg-primary border-primary'
        : 'text-text-disabled cursor-not-allowed bg-background-disabled border-border-disabled'
  }
  if (isChecked.value) return 'bg-primary border-primary text-white'
  return 'bg-background border-border text-text'
})

const setValidate = (valid: boolean) => {
  if (formField.value) {
    formField.value.isValidate = valid
  }
}

const validate = () => {
  if (props.required) { // 必填
    if (formField.value && !formField.value.value) {
      formField.value.isValidate = false
      return false;
    }
  }

  setValidate(!!formField.value?.value)
  return formField.value?.isValidate
}

const inputBlurClick = () => {
  emit('blur')
}

defineExpose({validate})
</script>

<template>
  <label class="inline-flex items-center select-none relative" :class="[
    disabled ? 'cursor-not-allowed opacity-60' : 'cursor-pointer',
    borderClass
  ]">
    <input
        type="radio"
        :name="props.name"
        :value="props.value"
        :checked="isChecked"
        :disabled="disabled"
        class="peer appearance-none transition-all duration-150
        rounded-full
        focus:ring-1 focus:ring-primary focus:ring-opacity-50
        outline-none
        [box-shadow:none]
        bg-center bg-no-repeat
        border
        cursor-pointer
        border-solid
      "
        :class="[sizeClass, stateClass]"
        @change="handleChange"
        @blur="inputBlurClick"
    />
    <!-- 圆形选中点 -->
    <span class="absolute pointer-events-none flex items-center justify-center"
          :class="[sizeClass,  border? 'left-[5px]': 'left-[-2px]', 'transition']"
    >
      <span v-if="isChecked"
            class="inline-block rounded-full bg-white"
            :class="{
              'w-2 h-2': size === 'large',
              'w-1.5 h-1.5': size === 'default',
              'w-1 h-1': size === 'small'
            }"
      ></span>
    </span>
    <span class="ml-2" :class="disabled ? 'text-text-disabled' : 'text-text-regular'">
      <slot>{{ props.label }}</slot>
    </span>
  </label>
</template>

<style scoped>
label {
  position: relative;
}

input[type="radio"].w-6 {
  width: 1.25rem;
  height: 1.25rem;
}

input[type="radio"].w-5 {
  width: 1rem;
  height: 1rem;
}

input[type="radio"].w-4 {
  width: 0.75rem;
  height: 0.75rem;
}
</style>
