<script setup lang="ts">
import Field from "../../model/Field.ts";
import FormField from "../../model/FormField.ts";
import {onMounted, ref, watch} from "vue";
import MyInnerPopover from "../popover/my-inner-popover.vue";

const props = defineProps<{
  field?: Field
}>()

const formField = defineModel({
  type: FormField
})

const labelInput = ref(new FormField(null))
const options = ref([{
  value: '=',
  label: '=',
}, {
  value: '>',
  label: '>',
}, {
  value: '<',
  label: '<',
}, {
  value: '>=',
  label: '>=',
}, {
  value: '<=',
  label: '<=',
}, {
  value: '!=',
  label: '!=',
}, {
  value: 'like',
  label: '包含',
}, {
  value: 'notLike',
  label: '不包含',
},
  // {
  //   value: 'between',
  //   label: '介于',
  // }, {
  //   value: 'in',
  //   label: '在',
  // }, {
  //   value: 'notIn',
  //   label: '不在',
  // }
])

const itemClick = (op: any) => {
  if (formField.value) {
    formField.value.value = op;
  }
  labelInput.value.value = op.label;
}

onMounted(() => {
  if (formField && formField.value && formField.value.value) {
    const value = formField.value.value.value
    const option = options.value.find(x => x.value == value)
    if (option) {
      labelInput.value.value = option.label;
    }
  }
})
</script>

<template>
  <MyInnerPopover placement="bottom" trigger="click" full-width ref="popperSelect">
    <template v-slot:default>
      <div class="inline-flex w-full relative">
        <my-input placeholder="查询条件" v-model="labelInput" icon="caret-down"
                  iconStyle="fas" icon-color="#FFF" icon-tag="icon"/>
      </div>

    </template>
    <template v-slot:option>
      <div>
        <div class="w-full cursor-pointer option-item px-4" v-for="(op,index) in options" :key="index"
             @click="itemClick(op)">
          {{ op.label }}
        </div>
      </div>
    </template>
  </MyInnerPopover>
</template>

<style scoped>
.option-item:hover {
  @apply bg-primary text-white;
}
</style>