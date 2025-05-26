<script setup lang="ts">
import Field from "../../model/Field.ts";
import FormField from "../../model/FormField.ts";
import {onMounted, ref, watch} from "vue";
import MyPopover from "../popover/my-popover.vue";

const props = defineProps<{
  field?: Field
}>()

const emit = defineEmits<{
  (e: 'click', event: MouseEvent): void
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

const itemClick = (op: any, e: MouseEvent) => {
  if (formField.value) {
    formField.value.value = op;
  }
  labelInput.value.value = op.label;

  if (popperSelect.value) {
    popperSelect.value.hide();
  }
  emit('click', e)
}

const popperSelect = ref()
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
  <MyPopover placement="bottom" trigger="click" ref="popperSelect" popper-class="py-1" :teleported="false">
    <template v-slot:default>
      <div class="inline-flex w-full relative">
        <my-input placeholder="查询条件" v-model="labelInput" icon="caret-down"
                  iconStyle="fas" icon-color="#FFF" icon-tag="icon"/>
      </div>

    </template>
    <template v-slot:content>
      <div class="w-[180px]">
        <div class="w-full cursor-pointer option-item px-4" v-for="(op,index) in options" :key="index"
             @click="itemClick(op, $event)">
          {{ op.label }}
        </div>
      </div>
    </template>
  </MyPopover>
</template>

<style scoped>
.option-item:hover {
  @apply bg-fill text-primary;
}
</style>