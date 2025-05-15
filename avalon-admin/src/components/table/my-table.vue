<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import Field from "../../model/Field.ts";
import {FieldTypeEnum} from "../../model/enum-type/FieldTypeEnum.ts";
import {computed, ref, watch} from "vue";
import {getSelectionValueByServiceAndField} from "../../cache/SelectionValueMemory.ts";
import {useGlobalServiceDataStore} from "../../global/store/serviceStore.ts";
import {ComputedRef} from "@vue/reactivity";
import MyIcon from "../icon/my-icon.vue";
import MyButton from "../button/my-button.vue";
import {getFileUploadUrl, getVideoUploadUrl} from "../../api/env.ts";
import MyImage from "../image/my-image.vue";
import MyMany2manySelect from "../select/many2may-select/my-many2many-select.vue";
import FormField from "../../model/FormField.ts";
import MyCheck from "../check/my-check.vue";
import {getDateTime} from "../../util/dateUtils.ts";
import MyDebug from "../debug/my-debug.vue";
import {useUserInfoStore} from "../../global/store/userInfoStore.ts";
import {useDebounceFn} from "@vueuse/core";
import MyVideo from "../video/my-video.vue";
import ShowField from "../../model/ShowField.ts";
import {getJoinFirstField, getJoinLastField, hasJoin} from "../../util/fieldUtils.ts";
import MyTableColSearch from "./my-table-col-search.vue";
import MyPopover from "../popover/my-popover.vue";

const emits = defineEmits(['rowClick', 'rowDeleteClick', 'rowSelectChange', 'rowAddClick', 'colFieldSearch']);

const props = defineProps<{
  record: any[],
  serviceName: string,
  fields: ShowField[],
  height?: string,
  showDeleteBtn?: boolean,
  showSelectBtn?: boolean,
  showBtnRow?: boolean,
}>()
const selectionDynamic = ref<any>({})
const userInfoStore = useUserInfoStore()
const serviceStore = useGlobalServiceDataStore()
const rowColSpan = ref(props.fields.length)

const loadData = async () => {
  if (!props.serviceName) {
    return;
  }

  for (let field of props.fields) {
    if (field.Field.type == FieldTypeEnum.SelectionField || field.Field.type == FieldTypeEnum.FieldSelectionField) { // 得到字段对应的selection的值
      selectionDynamic.value[field.Field.name] = await getSelectionValueByServiceAndField(props.serviceName, field.Field.name)
    }
  }
}


const getSelectionField = (field: ShowField, row: any): ComputedRef => {
  return computed(() => {
    if (hasJoin(field.originField)) {
      const last = getJoinLastField(field.originField)
      if (last in selectionDynamic.value) {
        return selectionDynamic.value[last][getValue(field, row)]
      }
      return ''
    } else {
      if (field.Field.name in selectionDynamic.value) {
        return selectionDynamic.value[field.Field.name][row[field.Field.name]]
      }
      return ''
    }
  })
}

watch(() => props.fields.length, (newValue) => { // 增加selection字段获取
  if (newValue) {
    loadData()
  }
  rowColSpan.value = props.fields.length;
}, {immediate: true})

const rowClick = (row: any) => {
  emits('rowClick', row)
}

const rowDeleteClick = (row: any) => {
  emits('rowDeleteClick', row)
}

const getValue = (field: ShowField, row: any) => {
  if (hasJoin(field.originField)) {
    const first = getJoinFirstField(field.originField)
    const last = getJoinLastField(field.originField)
    return row[first][last];
  }
  return row[field.originField]
}

const getImageUrl = (file: any) => {
  if (file instanceof File) {
    return URL.createObjectURL(file)
  }

  return getFileUploadUrl(file)
}
const getVideoUrl = (file: any) => {
  if (file instanceof File) {
    return URL.createObjectURL(file)
  }

  return getVideoUploadUrl(file)
}

const getMany2manyFormField = (obj: any, value: any, field: Field) => {
  obj[field.name + '_many'] = new FormField(value, field)
  return ''
}
const getFormField = (obj: any, value: any, field: Field) => {
  obj[field.name + '_field'] = new FormField(value, field)
  return ''
}

const allSelect = ref(new FormField(false))
const web_select = 'web_select';
let selectChange = (value: any) => {
  let selectedSum = 0
  for (let row of props.record) {
    if (row[web_select].value) {
      selectedSum++;
    }
  }
  if (selectedSum == props.record.length && selectedSum != 0) {
    allSelect.value.value = true;
  } else {
    allSelect.value.value = false;
  }
  const ids = props.record.filter(x => x[web_select].value).map(y => y.id);
  emits('rowSelectChange', selectedSum, ids)
}

selectChange = useDebounceFn(selectChange, 100)
watch(() => props.record.length, (length) => {
  if (props.showSelectBtn) {
    for (let row of props.record) {
      row[web_select] = new FormField(false)
    }
    selectChange([]);
  }
}, {immediate: true, deep: true})

watch(() => allSelect.value.value, (all) => {
  if (all) { // 全选
    for (let row of props.record) {
      row[web_select].value = true;
    }
  } else { // 全关
    for (let row of props.record) {
      row[web_select].value = false;
    }
  }
})

const addRowClick = () => {
  emits('rowAddClick')
}

const fieldSearchClick = (conditionString: string) => {
  emits('colFieldSearch', conditionString);
}

</script>

<template>
  <div class="w-full overflow-auto" :style="{'max-height': height || 'auto'}">
    <table class="data-table w-[1000px]">
      <thead @click.stop="void(0)" class="sticky top-0" style="left: auto;bottom: auto;right: auto;z-index: 10;">
      <tr class="border-b">
        <th v-if="showSelectBtn" class="w-[28px]">
          <MyCheck v-model="allSelect"/>
        </th>
        <template v-for="field in fields" :key="field.Field.id">
          <th v-if="!field.Field.isPrimaryKey" class="whitespace-nowrap">
            <div class="flex">
              <span class="flex-1">{{ field.Field.label }}</span>
              <template v-if="field.Field.canSearch">
                <MyPopover ref="popper" placement="top" trigger="click">
                  <template #default>
                    <MyIcon class="cursor-pointer" type="fas" icon="filter"/>
                  </template>
                  <template #option>
                    <my-table-col-search @sureSearch="fieldSearchClick" :service-name="serviceName"
                                         :field="field.Field"/>
                  </template>
                </MyPopover>
              </template>

              <template v-if="userInfoStore.user.debug">
                <div class="px-2">
                  <MyDebug class="cursor-pointer" :service="serviceName" :field="field.originField"/>
                </div>
              </template>
            </div>

          </th>
        </template>

        <th class="w-[24px]" v-if="showDeleteBtn">
          <MyIcon icon="sliders" type="fas"/>
        </th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="row in record" :key="row.id" class="border-b cursor-pointer" @click="rowClick(row)">
        <td v-if="showSelectBtn" class="w-[28px]" @click.stop="void(0)">
          <MyCheck v-model="row[web_select]" @change="selectChange"/>
        </td>
        <template v-for="field in fields" :key="field.Field.id">
          <td v-if="!field.Field.isPrimaryKey">
            <template v-if="field.Field.type == FieldTypeEnum.SelectionField">
              {{ getSelectionField(field, row) }}
            </template>
            <template v-else-if="field.Field.type == FieldTypeEnum.FieldSelectionField">
              {{ getSelectionField(field, row) }}
            </template>
            <template v-else-if="field.Field.type == FieldTypeEnum.Many2oneField">
              {{
                getValue(field, row) ? getValue(field, row)[serviceStore.getServiceByName(field.Field.relativeServiceName).nameField] : ''
              }}
            </template>
            <template v-else-if="field.Field.type == FieldTypeEnum.ImageField">
              <MyImage width="50" height="50" :src="getImageUrl(getValue(field,row))"></MyImage>
            </template>
            <template v-else-if="field.Field.type == FieldTypeEnum.VideoField">
              <MyVideo width="94" height="94" :src="getVideoUrl(getValue(field,row))"></MyVideo>
            </template>
            <template v-else-if="field.Field.type == FieldTypeEnum.Many2manyField">
              {{ getMany2manyFormField(row, getValue(field, row), field.Field) }}
              <MyMany2manySelect v-model="row[field.Field.name+'_many']" :readonly="true"
                                 :ref="field.Field.name+'_input'"
                                 :service="field.Field.relativeServiceName"
                                 :field="field.Field.name"
                                 :htmlId="field.Field.name"
                                 :htmlName="field.Field.name"/>
            </template>
            <template v-else-if="field.Field.type == FieldTypeEnum.BooleanField">
              {{ getFormField(row, getValue(field, row), field.Field) }}
              <MyCheck :ref="field.Field.name+'_input'" v-model="row[field.Field.name+'_field']"
                       :readonly="true"
                       :field="field.Field.name"
                       :htmlId="field.Field.name"
                       :htmlName="field.Field.name"></MyCheck>
            </template>
            <template v-else-if="field.Field.type == FieldTypeEnum.DateTimeField">
              {{ getValue(field, row) ? getDateTime(getValue(field, row)) : '' }}
            </template>
            <template v-else>
              {{ getValue(field, row) }}
            </template>
          </td>
        </template>

        <td class="w-[24px]" @click.stop="()=>{}" v-if="showDeleteBtn">
          <MyButton @click="rowDeleteClick(row)" icon="trash-can" icon-style="fas" is-link type="primary"
                    icon-color="#212529"></MyButton>
        </td>
      </tr>
      <tr v-if="showBtnRow" @click.stop="void(0)">
        <td v-if="showSelectBtn">
        </td>
        <td :colspan="rowColSpan">
          <my-button type="primary" is-link @click="addRowClick">添加行</my-button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<style scoped>

</style>