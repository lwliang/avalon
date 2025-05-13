<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/13 9:28
 */

import MyIcon from "../icon/my-icon.vue";
import MyInnerPopover from "../popover/my-inner-popover.vue";
import MyTable from "../table/my-table.vue";
import FormField from "../../model/FormField.ts";
import MyInput from "../input/my-input.vue";
import {ref} from "vue";
import MyFieldTree from "../tree/my-field-tree.vue";
import Field from "../../model/Field.ts";
import {useGlobalFieldDataStore} from "../../global/store/fieldStore.ts";

const props = defineProps({
  serviceName: String,
})

const emits = defineEmits(['selectField'])

const formField = defineModel({
  type: FormField,
  required: true,
})

const labelInput = ref<FormField>(new FormField(''))

const popperShow = () => {

}

const fields = ref<Field[]>([])
if (props.serviceName) {
  useGlobalFieldDataStore().getFieldByServiceNameAsync(props.serviceName as string).then((data) => {
    fields.value.push(...data)
  })
}


const leftFieldSelect = (field: any) => {
  labelInput.value.value = field.label
  formField.value.value = field

  emits('selectField', field)
}
</script>

<template>
  <MyInnerPopover placement="bottom" trigger="click" full-width ref="popperSelect" @popperShow="popperShow">
    <template v-slot:default>
      <div class="inline-flex w-full relative">
        <my-input v-model="labelInput" icon="caret-down"
                  iconStyle="fas" icon-color="#FFF" icon-tag="icon"/>
      </div>

    </template>
    <template v-slot:option>
      <div class="px-2">
        <MyFieldTree :show-plus-button="false" :nodes="fields" @fieldSelect="leftFieldSelect"/>
      </div>
    </template>
  </MyInnerPopover>
</template>

<style scoped>

</style>