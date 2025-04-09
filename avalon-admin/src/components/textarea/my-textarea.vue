<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import FormField from "../../model/FormField.ts";
import {watch} from "vue";
import {borderStyleType} from "../icon/my-icon.ts";

const props = defineProps({
  htmlId: String,
  htmlName: String,
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
  <div class="w-full relative ">
        <textarea
            :style="{'min-height': '80px','padding-top':pt+'px'}"
            :class="['form-input-control','bg-transparent', 'w-full', {'form-input-control-error': !formField.isValidate,
            'border':border == 'round', 'border-b':border == 'bottom'}]"
            v-if="formField"
            type="checkbox"
            v-model="formField.value" :id="htmlId" :readonly="readonly"
            @keydown.enter.stop="enterHandler"
            :name="htmlName">
        </textarea>
  </div>


</template>

<style scoped>

</style>