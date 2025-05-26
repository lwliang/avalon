<script setup lang="ts">
import {computed, onMounted, ref, watch, inject} from 'vue'
import type {CheckboxProps} from './types'
import FormField from "../../model/FormField";

// 是否在group中
const group = inject<any>('checkboxGroupContext', null)
const isGroup = !!group
const size = computed(() => props.size || group?.size?.value || 'default')
const disabled = computed(() => (props.disabled ?? group?.disabled?.value) || false)


const props = withDefaults(defineProps<CheckboxProps>(), {
  trueValue: true,
  falseValue: false,
  border: false,
  disabled: false,
  indeterminate: false
})
const emit = defineEmits<{
  (e: 'change', value: any): void
  (e: 'blur'): void
}>()

const formField = defineModel<FormField | string | number | boolean>()

const checkboxRef = ref<HTMLInputElement>()

const isChecked = computed({
  get: () => {
    if (group) {
      // 群组模式绑定在 value数组
      return group.checkedValues.value.includes(props.trueValue)
    } else {
      // 单独模式
      if (formField.value instanceof FormField) {
        return formField.value && formField.value.value === props.trueValue
      } else {
        return formField.value && formField.value === props.trueValue
      }

    }
  },
  set: (val: boolean) => {
    if (group) {
      group.toggleCheck(props.trueValue, val)
    }
    if (formField.value instanceof FormField) {
      formField.value.value = val ? props.trueValue : props.falseValue
    } else {
      formField.value = val ? props.trueValue : props.falseValue
    }

  }
})

// 监听 indeterminate
watch(
    () => props.indeterminate,
    val => {
      if (checkboxRef.value) {
        checkboxRef.value.indeterminate = val
      }
    },
    {immediate: true}
)

onMounted(() => {
  if (checkboxRef.value) {
    checkboxRef.value.indeterminate = props.indeterminate
  }
})

// 动态 class
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

watch(() => formField.value, () => {
  if (formField.value instanceof FormField) {
    emit('change', formField.value?.value)
  } else {
    emit('change', formField.value)
  }

  setValidate(true)
}, {deep: true})


const setValidate = (valid: boolean) => {
  if (formField.value instanceof FormField) {
    formField.value.isValidate = valid
  }
}

const validate = () => {
  if (formField.value instanceof FormField) {
    if (props.required) { // 必填

      if (formField.value && !formField.value.value) {
        formField.value.isValidate = false
        return false;
      }

    }

    setValidate(!!formField.value?.value)
    return formField.value?.isValidate
  }
}

const inputBlurClick = () => {
  emit('blur')
}

defineExpose({validate})
</script>

<template>
  <label
      class="inline-flex items-center select-none relative"
      :class="[
      props.disabled ? 'cursor-not-allowed opacity-60' : 'cursor-pointer',
      borderClass,
      props.class
    ]"
  >
    <input
        ref="checkboxRef"
        type="checkbox"
        @blur="inputBlurClick"
        :checked="isChecked"
        :disabled="props.disabled"
        class="peer appearance-none transition-all duration-150
        rounded
        focus:ring-2 focus:ring-primary focus:ring-opacity-50
        outline-none
        bg-center bg-no-repeat
        border
        border-solid
        cursor-pointer
      "
        :class="[sizeClass, stateClass]"
        @change="isChecked = !isChecked"
    />
    <!-- 勾选/中线 Icon -->
    <span
        class="cursor-pointer absolute flex items-center justify-center pointer-events-none transition"
        :class="[sizeClass,  props.border? 'left-[7px]': 'left-0']"
    >
      <template v-if="isChecked && !props.indeterminate">
        <my-icon type="fas" icon="check" color="white"/>
      </template>
      <template v-else-if="props.indeterminate">
         <my-icon type="fas" icon="minus" color="white"/>
      </template>
    </span>
    <span class="ml-2" :class="props.disabled ? 'text-text-disabled' : 'text-text-regular'">
      <slot>{{ props.label }}</slot>
    </span>
  </label>
</template>

<style scoped>
input[type="checkbox"] {
  margin: 0;
}
</style>