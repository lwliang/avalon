<script setup lang="ts">
import {ref} from "vue";
import FormField from "../../model/FormField.ts";
import MyConditionLine from "./my-condition-line.vue";
import MyIcon from "../icon/my-icon.vue";
import {getFileUploadUrl} from "../../api/env.ts";
import MyDropdown from "../dropdown/my-dropdown.vue";
import MyAvatar from "../avatar/my-avatar.vue";
import MyDropdownItem from "../dropdown/my-dropdown-item.vue";
import {useTemplateRef} from "@vue/runtime-dom";

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/13 14:21
 */
const props = defineProps({
  serviceName: String
})

const fieldForms = ref<FormField[]>([
  new FormField(null)
])

const conditionLinesRef = useTemplateRef<any>('conditionLines')

const addClick = (index: number) => {
  fieldForms.value.splice(index, 0, new FormField(null))
}

const minusClick = (index: number) => {
  fieldForms.value.splice(index, 1)
}

const conditionForm = ref<'or' | 'and'>('or')
const andClick = () => {
  conditionForm.value = 'and'
}

const orClick = () => {
  conditionForm.value = 'or'
}

const getConditionString = () => {
  if (!conditionLinesRef.value) {
    return null
  }

  let result: string | null = null
  for (const conditionLineRef of conditionLinesRef.value) {
    const conStr = conditionLineRef.getConditionString()
    if (!conStr) continue
    if (result) {
      result = `(${conStr}${conditionForm.value == 'or' ? '|' : '&'}${result})`
    } else {
      result = conStr
    }
  }
  return result
}

defineExpose({getConditionString})
</script>

<template>
  <div class="px-4">
    <div class="flex items-center pb-2">
      <div>
        匹配
      </div>
      <div>
        <my-dropdown>
          <template #default>
            <div class="hover:bg-gray-200 px-2 py-0.5 rounded"><span>{{
                conditionForm == 'or' ? '任意' : '所有'
              }}</span></div>
          </template>
          <template #dropdown>
            <my-dropdown-item label="所有" @itemClick="andClick"></my-dropdown-item>
            <my-dropdown-item label="任意" @itemClick="orClick"></my-dropdown-item>
          </template>
        </my-dropdown>
      </div>
      <div>
        以下规则
      </div>
    </div>
    <div class="flex items-center py-1" v-for="(formField,index) in fieldForms" :key="index">
      <my-condition-line class="flex-1" :serviceName="serviceName" ref="conditionLines"/>
      <div class="flex gap-2 px-4">
        <MyIcon @click="addClick(index)" class="cursor-pointer" icon="plus" type="fas" color="var(--overlay-color)"/>
        <MyIcon @click="minusClick(index)" class="cursor-pointer" icon="minus" type="fas" color="var(--overlay-color)"/>
      </div>
    </div>

  </div>
</template>

<style scoped>

</style>