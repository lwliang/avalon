<script setup lang="ts">
import Field from "../../model/Field.ts";
import MyConditionOperate from "../search/my-condition-operate.vue";
import MyButton from "../button/my-button.vue";
import {ref} from "vue";
import MyConditionValue from "../search/my-condition-value.vue";
import {useTemplateRef} from "@vue/runtime-dom";
import {FieldTypeEnum} from "../../model/enum-type/FieldTypeEnum.ts";
import { FilterOperator } from "../../model/FilterCondition.ts";

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/15 20:06
 */
const props = defineProps<{
  serviceName: string,
  field: Field,
  operate?: string,
  value?: any
}>()

const emits = defineEmits<{
  sureSearch: [fieldName: string, operate: string, value: any]
}>()


const getDefaultOperate = (): FilterOperator => {
  switch (props.field.type) {
    case FieldTypeEnum.BooleanField:
    case FieldTypeEnum.BigDecimalField:
    case FieldTypeEnum.BigIntegerField:
    case FieldTypeEnum.DateField:
    case FieldTypeEnum.DateTimeField:
    case FieldTypeEnum.DoubleField:
    case FieldTypeEnum.FloatField:
    case FieldTypeEnum.IntegerField:
    case FieldTypeEnum.TimeField:
      return '='
    default:
      return 'like'
  }
}

const outsideRef = useTemplateRef<HTMLDivElement>('outside')
const operateValue = ref<FilterOperator>(props.operate as FilterOperator || getDefaultOperate())
const searchValue = ref<string>(props.value || '')

const searchClick = () => {
  if (outsideRef.value) {
    const event = new MouseEvent('click', {bubbles: true});
    outsideRef.value.dispatchEvent(event);
  }
  emits('sureSearch', props.field.name, operateValue.value, searchValue.value)
}
</script>

<template>
  <div class="w-full rounded px-2 py-2 flex flex-col gap-2" @click.stop="void 0">
    <my-condition-operate :field="field" v-model="operateValue" @click.stop="void 0" :teleported="false"/>
    <my-condition-value :service-name="serviceName" :field="field" v-model="searchValue"
                        :operate="operateValue" @click.stop="void 0"/>
    <div class="flex items-center justify-center">
      <my-button @click="searchClick" rounded>查询</my-button>
    </div>
  </div>
  <div ref="outside"></div>
</template>

<style scoped>

</style>