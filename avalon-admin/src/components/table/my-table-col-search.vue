<script setup lang="ts">
import Field from "../../model/Field.ts";
import MyConditionOperate from "../search/my-condition-operate.vue";
import MyButton from "../button/my-button.vue";
import {ref} from "vue";
import FormField from "../../model/FormField.ts";
import MyConditionValue from "../search/my-condition-value.vue";
import {useTemplateRef} from "@vue/runtime-dom";
import {FieldTypeEnum} from "../../model/enum-type/FieldTypeEnum.ts";

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


const getDefaultOperate = () => {
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
const operateValue = ref(new FormField({value: props.operate || getDefaultOperate()}))
const searchValue = ref(new FormField(props.value))

const searchClick = () => {
  if (outsideRef.value) {
    const event = new MouseEvent('click', {bubbles: true});
    outsideRef.value.dispatchEvent(event);
  }
  emits('sureSearch', props.field.name, operateValue.value.value.value, searchValue.value.value ? searchValue.value.value : '')
}
</script>

<template>
  <div class="w-[200px] rounded px-2 py-2 flex flex-col gap-2" @click.stop="void 0">
    <my-condition-operate :field="field" v-model="operateValue" @click.stop="void 0"/>
    <my-condition-value :service-name="serviceName" :field="field" v-model="searchValue"
                        :operate="operateValue.value.value" @click.stop="void 0"/>
    <div class="flex items-center justify-center">
      <my-button @click="searchClick" rounded>查询</my-button>
    </div>
  </div>
  <div ref="outside"></div>
</template>

<style scoped>

</style>