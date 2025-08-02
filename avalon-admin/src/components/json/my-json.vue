<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2025/07/02 10:26
 */
import {Vue3JsonEditor} from 'vue3-json-editor'
import {ref, watch} from "vue";

const modelValue = defineModel<any>({required: true})
const emit = defineEmits<{
  (e: 'clear'): void
  (e: 'change', value: any): void
  (e: 'blur', event: FocusEvent): void
  (e: 'focus', event: FocusEvent): void
  (e: 'suffixIconClick'): void
  (e: 'keyup', event: KeyboardEvent): void
}>()

const onJsonChange = (value: any) => {
  modelValue.value = JSON.stringify(value)
  emit('change', value)
}

const value = ref()

watch(() => modelValue.value, (newValue) => {
  if (newValue) {
    try {
      value.value = typeof newValue === 'string' ? JSON.parse(newValue) : newValue
    } catch (e) {
      value.value = newValue
    }
  } else {
    value.value = null
  }
}, {immediate: true})

watch(() => value.value, (newValue) => {
  if (newValue) {
    modelValue.value = JSON.stringify(newValue)
  } else {
    modelValue.value = ""
  }
})

/** 校验逻辑 */
function validate(): boolean {
  return true
}

defineExpose({validate})
</script>

<template>
  <Vue3JsonEditor
      v-model="value"
      :expandedOnStart="true"
      @json-change="onJsonChange"
  />
</template>

<style scoped>

</style>