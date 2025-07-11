<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/25 15:48
 */

import { QuestionFilled } from '@element-plus/icons-vue'
import Field from "../../model/Field.ts";
import {ref} from "vue";
import {FieldTypeEnum} from "../../model/enum-type/FieldTypeEnum.ts";
import {getServiceField} from "../../util/fieldUtils.ts";


const props = defineProps<{
  service: string,
  field: string
}>()

const serviceField = ref<Field>()
getServiceField(props.service, props.field).then((field => {
  if (field) {
    serviceField.value = field
  }
}))


</script>

<template>
  <el-popover placement="bottom" trigger="hover" :width="300">
    <template #reference>
      <el-icon class="cursor-pointer" size="14">
        <QuestionFilled />
      </el-icon>
    </template>
    <template #default>
      <div class="">
        <ul class="list-disc pl-5">
          <li>
            <div class="flex gap-1 whitespace-nowrap items-center">
              <div>字段:</div>
              <div>{{ field }}</div>
            </div>
          </li>
          <li>
            <div class="flex gap-1 whitespace-nowrap items-center">
              <div>模型:</div>
              <div>{{ service }}</div>
            </div>
          </li>
          <li>
            <div class="flex gap-1 whitespace-nowrap items-center">
              <div>类型:</div>
              <div>{{ serviceField?.type }}</div>
            </div>
          </li>
          <li v-if="serviceField?.type == FieldTypeEnum.Many2oneField||
                         serviceField?.type == FieldTypeEnum.Many2manyField||
                            serviceField?.type == FieldTypeEnum.One2manyField ||
                            serviceField?.type == FieldTypeEnum.One2oneField">
            <div class="flex gap-1 whitespace-nowrap items-center">
              <div>关系:</div>
              <div>{{ serviceField?.relativeServiceName }}</div>
            </div>

          </li>
        </ul>
      </div>
    </template>
  </el-popover>
</template>

<style scoped>

</style>