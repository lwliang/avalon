<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {ref, watch, computed} from "vue";
import {InputExpose} from "../../../global/input/InputExpose.ts";
import {onMounted} from "@vue/runtime-dom";
import {getModelPageApi} from "../../../api/modelApi.ts";
import {useFieldStore} from "../../../global/store/fieldStore.ts";
import {useServiceStore} from "../../../global/store/serviceStore.ts";
import Service from "../../../model/Service.ts";
import {useDebounceFn} from '@vueuse/core'
import MyTag from "../../tag/my-tag.vue";
import {MyMany2ManySelectProps} from "./types.ts";
import { objectCloneDeep } from "../../../util/ObjectUtils.ts";
import Snowflake from "../../../model/Snowflake.ts";

const serviceFieldStore = useFieldStore()
const serviceStore = useServiceStore()

const props = defineProps<MyMany2ManySelectProps>()
const service = ref<Service>()
const emit = defineEmits(['rightBtnClick', 'blur'])

// 格式 {id:1,name:"显示"} name的字段不一定是name
const formField = defineModel<any[]>({
  type: Array,
  default: () => []
})

const formFieldSelected = computed({
  get() {
    return formField.value.map(item => item[props.relativeForeignKeyName][relativeServiceId.value])
  },
  set(value: any[]) {
    // 根据选中的值更新 formField
    if (Array.isArray(value)) {
      formField.value = value.map(id => {
        // 从 options 中找到对应的完整对象
        const option = options.value.find(opt => opt[relativeServiceId.value] === id)
        if (option) {
          return {
            id:Symbol(Snowflake.getNextId()),
            [props.relativeForeignKeyName]: {
              id: option[relativeServiceId.value],
            [relativeServiceName.value]: option[relativeServiceName.value], // 确保包含 name 属性
          }}
        }
        return {
          id:Symbol(Snowflake.getNextId()),
          [props.relativeForeignKeyName]: {
            id: id,
            [relativeServiceName.value]: ''
          }
        }
      })
    }
  }
})

const relativeServiceName = ref(props.relativeServiceName || '')
const relativeServiceId = ref(props.relativeServiceId || '')
const options = ref<Record<string, any>[]>([])
let init = true

// 从 props 中获取服务信息
if (props.serviceName) {
  if(!relativeServiceName.value) {
    serviceStore.getServiceByNameAsync(props.serviceName).then(data => {
      relativeServiceName.value = data.nameField;
      relativeServiceId.value = data.keyField;
    })
  }
}

watch(() => formField.value, () => {
  setValidate(true)
})

const initOptions = () => {
  const selected = objectCloneDeep(formField.value.map(item => item[props.relativeForeignKeyName]))
  for(let i = 0; i < selected.length; i++) {
    const item = selected[i]
    const option = options.value.find(opt => opt[relativeServiceId.value] === item[relativeServiceId.value])
    if (!option) {
      options.value.push(item)
    }
  }
}

// 计算可选项，排除已选择的项
const computedOptions = computed(() => {
  return options.value
})

onMounted(() => {
  if (props.serviceName) {
    service.value = serviceStore.getServiceByName(props.serviceName);
  }
  if (formField.value && formField.value.length > 0) {
    initOptions()
    if (init) {
      init = false
    }
  } else {
    loadServiceOption()
  }
})

const loadServiceOption = (name?: string) => {
  if (props.serviceName && service.value) {
    let condition = "";
    if (formField.value && formField.value.length > 0) {
      const values = formField.value.map(item => item.id)
      if (values.length) {
        condition = `('${relativeServiceId.value}',notIn , ${values.join()})`
      }
    }
    if (condition) {
      condition = `('${service.value.nameField}',like,${name ? "'" + name + "'" : "''"})&${condition}`
    } else {
      condition = `('${service.value.nameField}',like,${name ? "'" + name + "'" : "''"})`
    }
    getModelPageApi(`${service.value.keyField},${service.value.nameField}`,
        condition,
        props.serviceName,
        1).then(pageInfo => {
      options.value.splice(0, options.value.length)
      options.value.push(...pageInfo.data)
      if (init) {
        init = false
      }
    })
  }
}

const setValidate = (valid: boolean) => {
  // 验证逻辑保持不变，但不再设置 isValidate 属性
}

const validate = (): boolean => {
  if (!props.required) {
    return true;
  }
  return formField.value && formField.value.length > 0
}

const optionSelectClick = (key: Record<string, any>) => {
  const newItem = {
    id: key[relativeServiceId.value],
    name: key[relativeServiceName.value]
  }
  formField.value.push(newItem)
}

const deleteTagClick = (tagValue: any) => {
  const tagIndex = formField.value.findIndex((x: any) => x.id === tagValue.id)
  if (tagIndex >= 0) {
    formField.value.splice(tagIndex, 1)
  }
}

const tagClick = (tagValue: any) => {
  console.log("tagClick", tagValue)
}

const inputBlurClick = () => {
  emit('blur')
}

const handleSearch = useDebounceFn((value: string) => {
  loadServiceOption(value)
}, 500)

defineExpose<InputExpose>({validate})

</script>

<template>
  <div class="flex relative w-full">
    <div class="inline-flex w-full relative items-center flex-wrap gap-1">
      <!-- readonly 模式显示标签 -->
      <template v-if="readonly">
        <template v-for="(fieldValue,index) in formField" :key="index">
          <MyTag 
            :closable="false" 
            :round="false"
            :label="fieldValue[props.relativeForeignKeyName][relativeServiceName]"
            :value="fieldValue" 
            @click="tagClick"
          />
        </template>
      </template>
      
      <!-- 非 readonly 模式显示选择器 -->
      <el-select
        v-else
        v-model="formFieldSelected"
        placeholder="请选择"
        clearable
        multiple
        filterable
        remote
        :remote-method="handleSearch"
        @change="inputBlurClick"
        @blur="inputBlurClick"
        style="width: 100%">
        <el-option
          v-for="(value, index) in computedOptions"
          :key="index"
          :label="value[relativeServiceName]"
          :value="value[relativeServiceId]"
        />
        <template #empty>
          <div class="px-4">
            无匹配数据
          </div>
        </template>
      </el-select>
    </div>
  </div>
</template>

<style scoped>

</style>