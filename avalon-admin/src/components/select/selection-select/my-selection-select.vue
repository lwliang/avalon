<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import './my-selection-select.css'
import {computed, ref, watch} from "vue";
import {InputExpose} from "../../../global/input/InputExpose.ts";
import {getSelectionValueByServiceAndField} from "../../../cache/SelectionValueMemory.ts";
import {onMounted} from "@vue/runtime-dom";
import {MySelectionSelectProps} from "./my-selection-select.ts";
import {isObjectEmpty} from "../../../util/ObjectUtils.ts";
import { ArrowDown } from '@element-plus/icons-vue'
import MyTag from "../../tag/my-tag.vue";

const props = defineProps<MySelectionSelectProps>()

const emit = defineEmits(['rightBtnClick', 'blur'])

const formField = defineModel<string>()

const labelValue = ref<string>('')
const labelFields = ref<Array<{id: string, name: string}>>([]) // 多选时的标签值

watch(() => formField.value, () => {
  formatDisplay()
})

/**
 * 格式化显示
 */
const formatDisplay = () => {
  if (formField.value) {
    if (props.isMulti) { // 多选
      const values = formField.value.split(',')
      labelFields.value.splice(0, labelFields.value.length)
      for (const value of values) {
        if (options.value[value]) {
          labelFields.value.push({id: value, name: options.value[value]})
        }
      }
    } else { // 单选
      labelValue.value = options.value[formField.value] || ''
    }
  }
}

const options = ref<Record<string, string>>({})

const computedOptions = computed(() => {
  const result: any = {}
  for (const key in options.value) {
    if (labelFields.value.findIndex(field => field.id === key) < 0) {
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

const validate = (): boolean => {
  if (!props.required) {
    return true;
  }
  return !!formField.value
}

const optionSelectClick = (key: string) => {
  if (props.isMulti) { // 多选
    labelFields.value.push({id: key, name: options.value[key]})
    labelValue.value = '';
    formField.value = labelFields.value.map(field => field.id).join(',')
  } else { // 单选
    labelValue.value = options.value[key]
    formField.value = key
  }
}

const deleteTagClick = (value: any) => {
  const index = labelFields.value.findIndex(field => field.id === value)
  if (index >= 0) {
    labelFields.value.splice(index, 1)
  }
  formField.value = labelFields.value.map(field => field.id).join(',')
}

const tagClick = (value: any) => {
}

const inputBlurClick = () => {
  emit('blur')
}

defineExpose<InputExpose>({validate})

</script>

<template>
  <div class="flex relative w-full">
    <div class="inline-flex w-full relative items-center flex-wrap gap-1">
      <!-- readonly 模式显示标签 -->
      <template v-if="readonly">
        <template v-if="isMulti">
          <template v-for="(fieldValue, index) in labelFields" :key="index">
            <MyTag 
              :closable="false" 
              :round="false"
              :label="fieldValue.name"
              :value="fieldValue.id" 
              @click="tagClick"
            />
          </template>
        </template>
        <template v-else>
          <template v-if="labelValue">
            <MyTag 
              :closable="false" 
              :round="false"
              :label="labelValue"
              :value="labelValue" 
              @click="tagClick"
            />
          </template>
        </template>
      </template>
      
      <!-- 非 readonly 模式显示选择器 -->
      <el-select
        v-else
        v-model="formField"
        placeholder="请选择"
        clearable
        :multiple="isMulti"
        @change="inputBlurClick"
        @blur="inputBlurClick"
        style="width: 100%">
        <el-option
          v-for="(value, key) in computedOptions"
          :key="key"
          :label="value"
          :value="key"
        />
        <template #empty>
          <div class="px-4">
            无选择
          </div>
        </template>
      </el-select>
    </div>
  </div>
</template>

<style scoped>

</style>