<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import './my-password.css'
import FormField from "../../model/FormField.ts";
import {ref, watch} from "vue";
import {borderStyleType} from "../icon/types.ts";

const props = defineProps({
  required: Boolean,
})

const emit = defineEmits<{
  (e: 'clear'): void
  (e: 'change', value: any): void
  (e: 'blur'): void
  (e: 'keyup', event: KeyboardEvent): void
}>()

const formField = defineModel({
  type: FormField,
})

watch(() => formField.value?.value, () => {
  setValidate(true)
})

const setValidate = (valid: boolean) => {
  if (formField.value) {
    formField.value.isValidate = valid
  }
}

const validate = () => {
  if (!props.required) {
    setValidate(true)
    return true;
  }
  setValidate(!!formField.value?.value)
  return formField.value?.isValidate
}

const eyeOpen = ref(false)
const type = ref('password')

const suffixIconClick = () => {
  eyeOpen.value = !eyeOpen.value
  type.value = eyeOpen.value ? 'text' : 'password'
}

defineExpose({validate})
</script>

<template>
  <my-input @suffixIconClick="suffixIconClick" v-model="formField" :type="type" @keyup="emit('keyup',$event)"
            @change="emit('change',$event)"
            @blur="emit('blur')"
            @clear="emit('clear')"
            :suffixIcon="eyeOpen ? 'eye' :'eye-slash'"/>
</template>

<style scoped>

</style>