<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import './my-selection-select.css'
import {ref, watch, computed} from "vue";
import FormField from "../../../model/FormField.ts";
import {InputExpose} from "../../../global/input/InputExpose.ts";
import {getSelectionValueByServiceAndField} from "../../../cache/SelectionValueMemory.ts";
import {onMounted} from "@vue/runtime-dom";
import MyIcon from "../../icon/my-icon.vue";
import {borderStyleType} from "../../icon/types.ts";
import MyPopover from "../../popover/my-popover.vue";
import {MySelectionSelectProps} from "./my-selection-select.ts";
import {isObjectEmpty} from "../../../util/ObjectUtils.ts";

const props = defineProps<MySelectionSelectProps>()

const emit = defineEmits(['rightBtnClick', 'blur'])

const formField = defineModel({
  type: FormField,
})
const labelValue = new FormField('');
labelValue.Field = formField.value?.Field
const labelField = ref<FormField>(labelValue)
const labelFields = ref<FormField[]>([]) // 多选时，labelField的值

watch(() => formField.value?.value, () => {
  setValidate(true)
  formatDisplay()
})

const popoverRef = ref()

/**
 * 格式化显示
 */
const formatDisplay = () => {
  if (labelField.value && labelField.value.value != options.value[formField.value?.value]) {
    if (formField.value?.Field && formField.value.Field.isMulti) { // 多选
      if (formField.value?.value) {
        const values = formField.value?.value.split(',')
        labelFields.value.splice(0, labelFields.value.length)
        for (const value of values) {
          labelFields.value.push(new FormField({id: value, name: options.value[value]}, formField.value?.Field))
        }
      }
    } else { // 单选
      labelField.value.value = options.value[formField.value?.value]
    }
  }
}

const options = ref<Record<string, string>>({})

const computedOptions = computed(() => {
  const result: any = {}
  for (const key in options.value) {
    if (labelFields.value.findIndex(field => field.value.id === key) < 0) {
      result[key] = options.value[key]
    }
  }
  return result
})

onMounted(() => {
  loadSelectionOptions();
})

const loadSelectionOptions = () => {
  if (props.serviceName && props.field) {
    getSelectionValueByServiceAndField(props.serviceName, props.field).then(data => {
      Object.assign(options.value, data)
      if (formField.value) {
        formatDisplay()
      }
    })
  }
}

const setValidate = (valid: boolean) => {
  if (formField.value) {
    formField.value.isValidate = valid
  }
}

const validate = (): boolean => {
  if (!props.required) {
    setValidate(true)
    return true;
  }
  setValidate(!!formField.value?.value)
  return formField.value ? formField.value.isValidate : false
}


const optionSelectClick = (key: string) => {
  if (formField.value?.Field?.isMulti) { // 多选
    labelFields.value.push(new FormField({id: key, name: options.value[key]}, formField.value?.Field))
    labelField.value.value = null;
    if (formField.value) {
      formField.value.value = labelFields.value.map(field => field.value.id).join(',')
    }
  } else { // 单选
    if (labelField.value) {
      labelField.value.value = options.value[key]
    }
    if (formField.value) {
      formField.value.value = key
    }
    if (popoverRef.value) {
      popoverRef.value.hide()
    }
  }
}

const deleteTagClick = (value: any) => {
  const index = labelFields.value.findIndex(field => field.value.id === value)
  if (index >= 0) {
    labelFields.value.splice(index, 1)
  }
  if (formField.value) {
    formField.value.value = labelFields.value.map(field => field.value.id).join(',')
  }
}

const tagClick = (value: any) => {
}


const itemSelectClick = (key: number) => {
  optionSelectClick(String(key))
}

const inputBlurClick = () => {
  emit('blur')
}

defineExpose<InputExpose>({validate})

</script>

<template>
  <div class="flex relative">
    <div class="inline-flex w-full relative items-center flex-wrap gap-1">
      <template v-for="(fieldValue,index) in labelFields" :key="index">
        <MyTag :closable="!readonly" :round="false"
               :label="fieldValue.value.name"
               :value="fieldValue.value.id" @close="deleteTagClick" @click="tagClick"/>
      </template>
      <template v-if="readonly && labelField.value">
        <MyTag :closable="!readonly" :round="false"
               :label="labelField.value"
               :value="labelValue.value" @close="deleteTagClick" @click="tagClick"/>
      </template>
      <MyPopover ref="popoverRef" v-if="!readonly" placement="bottom" trigger="click"
                 default-class="w-full flex-1 min-w-[120px]">
        <template #default>
          <div class="inline-flex relative w-full">
            <my-input class="w-full" v-model="labelField" readonly suffix-icon-type="fas" suffix-icon="caret-down"
                      @blur="inputBlurClick"/>
          </div>
        </template>
        <template #content>
          <div class="flex flex-col min-w-[250px]">
            <template v-if="computedOptions && !isObjectEmpty(computedOptions)">
              <div class="cursor-pointer hover:bg-background-component px-4" v-for="(value,key) in computedOptions"
                   :key="key"
                   @click="itemSelectClick(key)">
                {{ value }}
              </div>
            </template>
            <div v-else class="px-4">
              无选择
            </div>
          </div>

        </template>
      </MyPopover>
    </div>
  </div>


</template>

<style scoped>

</style>