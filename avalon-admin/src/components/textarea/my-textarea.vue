<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import FormField from "../../model/FormField.ts";
import {watch} from "vue";
import {borderStyleType} from "../icon/types.ts";

const props = defineProps({
  required: Boolean,
  readonly: Boolean,
  border: {
    type: borderStyleType,
    default: 'round'
  },
  pt: {
    type: Number,
    default: 0
  }
})

const emit = defineEmits(["inputMessage"])

const formField = defineModel({
  type: FormField,
  required: true
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
  if (props.required) { // 必填
    if (formField.value && !formField.value.value) {
      formField.value.isValidate = false
      return false;
    }
  }

  setValidate(!!formField.value?.value)
  return formField.value?.isValidate
}

const enterHandler = () => {
  emit('inputMessage', formField.value.value)
}

defineExpose({validate})

</script>

<template>
  <div class="w-full relative border border-solid border-border rounded p-2 mt-2">
        <textarea
            :style="{'min-height': '80px','padding-top':pt+'px'}"
            :class="['form-input-control','bg-transparent', 'w-full', {'form-input-control-error': !formField.isValidate,
            }]"
            v-if="formField"
            type="checkbox"
            v-model="formField.value" :readonly="readonly"
            @keydown.enter.stop="enterHandler">
        </textarea>
  </div>


</template>

<style scoped>

</style>