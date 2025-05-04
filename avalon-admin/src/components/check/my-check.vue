<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import FormField from "../../model/FormField.ts";
import {ref, watch} from "vue";

const props = defineProps({
  htmlId: String,
  htmlName: String,
  required: Boolean,
  readonly: Boolean,
  indeterminate: {
    type: Boolean,
    default: false,
  }
})


const emit = defineEmits(['change'])

const formField = defineModel({
  type: FormField,
  required: true
})

watch(() => formField.value?.value, () => {
  emit('change', formField.value?.value)
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

const indeterminateClick = () => {
  formField.value.value = true
}

defineExpose({validate})

</script>

<template>
  <div class="inline-flex relative">
    <div v-if="indeterminate" class="check-indeterminate" @click="indeterminateClick">
      <div class="check-indeterminate-inner"></div>
    </div>
    <input v-else
           :class="[' cursor-pointer','form-input-control', 'w-5', {'form-input-control-error': !formField.isValidate}]"
           v-if="formField && !indeterminate"
           type="checkbox"
           v-model="formField.value" :id="htmlId" :disabled="readonly"
           :name="htmlName">
  </div>


</template>

<style scoped>
.check-indeterminate {
  width: 20px;
  @apply flex items-center justify-center;
  @apply cursor-pointer;
  @apply relative;

}

.check-indeterminate-inner {
  display: inline-block;
  background-color: var(--color-primary);
  border-color: var(--color-primary);
  width: 13px;
  height: 13px;
  border-radius: 2px;
}

.check-indeterminate-inner:before {
  content: "";
  position: absolute;
  display: block;
  background-color: var(--color-surface);
  height: 2px;
  transform: scale(.5);
  left: 0;
  right: 0;
  top: 5px;
}
</style>