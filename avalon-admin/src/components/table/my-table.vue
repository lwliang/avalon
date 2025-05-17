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
import {getDate, getDateTime, getTime} from "../../util/dateUtils.ts";
import MyDebug from "../debug/my-debug.vue";
import {useUserInfoStore} from "../../global/store/userInfoStore.ts";
import {useDebounceFn, useEventListener} from "@vueuse/core";
import MyVideo from "../video/my-video.vue";
import ShowField from "../../model/ShowField.ts";
import {getJoinFirstField, getJoinLastField, hasJoin} from "../../util/fieldUtils.ts";
import MyTableColSearch from "./my-table-col-search.vue";
import MyPopover from "../popover/my-popover.vue";
import EditingCell from "./my-table.ts"
import MySelectionSelect from "../select/selection-select/my-selection-select.vue";
import {useTemplateRef} from "@vue/runtime-dom";
import MyMany2OneSelect from "../select/many2one-select/my-many2one-select.vue";
import {maskPassword} from "../../util/StringUtils.ts";
import MyPassword from "../password/my-password.vue";
import MyDatetime from "../datetime/my-datetime.vue";
import MyTime from "../time/my-time.vue";


const emits = defineEmits(['rowClick', 'rowDeleteClick', 'rowSelectChange', 'rowAddClick', 'colFieldSearch', 'cellChange']);

const props = defineProps<{
  record: any[],
  serviceName: string,
  fields: ShowField[],
  height?: string,
  showDeleteBtn?: boolean,
  showSelectBtn?: boolean,
  showBtnRow?: boolean,
  editable?: boolean,
  keyField?: string,
  enableFilter?: boolean
}>()
const selectionDynamic = ref<any>({})
const userInfoStore = useUserInfoStore()
const serviceStore = useGlobalServiceDataStore()
const rowColSpan = ref(props.fields.length)
const myTableRef = useTemplateRef('myTable')


useEventListener(document, 'click', (e: MouseEvent) => {
  const el = myTableRef.value
  if (!el) return

  // 获取元素的矩形区域
  const rect = el.getBoundingClientRect()
  // 判断点击点是否在矩形内
  const inside =
      e.clientX >= rect.left &&
      e.clientX <= rect.right &&
      e.clientY >= rect.top &&
      e.clientY <= rect.bottom

  if (!inside) {
    editingCellSave()
  }
})

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
  if (props.editable) {

  } else {
    emits('rowClick', row)
  }
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

const getPasswordValue = (field: ShowField, row: any) => {
  let password = '';
  if (hasJoin(field.originField)) {
    const first = getJoinFirstField(field.originField)
    const last = getJoinLastField(field.originField)
    password = row[first][last];
  }
  password = row[field.originField]

  return password ? maskPassword(password) : ''
}

const setValue = (field: ShowField, row: any, value: any) => {
  if (hasJoin(field.originField)) {
    const first = getJoinFirstField(field.originField)
    const last = getJoinLastField(field.originField)
    row[first][last] = value;
  }
  row[field.originField] = value;
}

const getImageUrl = (file: any) => {
  if (file instanceof File) {
    return URL.createObjectURL(file)
  }
  if (file) {
    return getFileUploadUrl(file)
  }
  return undefined
}
const getVideoUrl = (file: any) => {
  if (file instanceof File) {
    return URL.createObjectURL(file)
  }
  if (file) {
    return getVideoUploadUrl(file)
  }

  return undefined
}

const getMany2manyFormField = (obj: any, value: any, field: Field) => {
  obj[field.name + '_many'] = new FormField(value, field)
  return ''
}
const getSelectFormField = (obj: any, value: any, field: Field) => {
  obj[field.name + '_select'] = new FormField(value, field)
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

const fieldSearchClick = (fieldName: string, operate: string, value: any) => {
  emits('colFieldSearch', fieldName, operate, value);
}


// 编辑模式
const editingCell = ref<EditingCell>(new EditingCell(-1, -1));
const editingValue = ref<FormField>(new FormField(null));
const editingCellChange = (rowIndex: number, colIndex: number, value: any, field: Field) => {
  if (editingCell.value.rowIndex >= 0 && editingCell.value.colIndex >= 0) { // 进行保存
    const rowValue = props.record[editingCell.value.rowIndex]
    const fieldShow = props.fields[editingCell.value.colIndex];
    setValue(fieldShow, rowValue, editingValue.value.value);
    if (props.keyField) {
      cellChangeEvent(rowValue[props.keyField], fieldShow.originField, editingValue.value.value);
    }
  }
  editingCell.value.rowIndex = rowIndex;
  editingCell.value.colIndex = colIndex;
  editingValue.value.value = value;
  editingValue.value.Field = field;
}
const editingCellSave = () => {
  if (editingCell.value.rowIndex >= 0 && editingCell.value.colIndex >= 0) { // 进行保存
    const rowValue = props.record[editingCell.value.rowIndex]
    const fieldShow = props.fields[editingCell.value.colIndex];
    setValue(fieldShow, rowValue, editingValue.value.value);
    if (props.keyField) {
      cellChangeEvent(rowValue[props.keyField], fieldShow.originField, editingValue.value.value);
    }

    editingCell.value.rowIndex = -1;
    editingCell.value.colIndex = -1;
    editingValue.value.value = null;
  }
}

const cellChangeEvent = (key: any, fieldName: string, value: any) => {
  emits('cellChange', key, fieldName, value);
}

</script>

<template>
  <div ref="myTable" class="w-full overflow-auto h-full" :style="{'max-height': height || 'auto'}">
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
              <template v-if="enableFilter && field.Field.canSearch">
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
      <tr v-for="(row, rowIndex) in record" :key="row.id" class="border-b cursor-pointer" @click="rowClick(row)">
        <td v-if="showSelectBtn" class="w-[28px]" @click.stop="void(0)">
          <MyCheck v-model="row[web_select]" @change="selectChange"/>
        </td>
        <template v-for="(field, colIndex) in fields" :key="field.Field.id">
          <td v-if="!field.Field.isPrimaryKey"
              @click="editingCellChange(rowIndex,colIndex,getValue(field,row),field.Field)">
            <template v-if="field.Field.type == FieldTypeEnum.SelectionField">
              <template v-if="editable && editingCell.rowIndex == rowIndex && editingCell.colIndex == colIndex">
                <my-selection-select @click.stop="void 0" :service="serviceName" :field="field.originField"
                                     v-model="editingValue"/>
              </template>
              <template v-else>
                {{ getSelectFormField(row, getValue(field, row), field.Field) }}
                <my-selection-select :service="serviceName" :field="field.originField"
                                     v-model="row[field.Field.name+'_select']" :readonly="true"
                                     :ref="field.Field.name+'_input'"/>
              </template>
            </template>
            <template v-else-if="field.Field.type == FieldTypeEnum.FieldSelectionField">
              {{ getSelectionField(field, row) }}
            </template>
            <template v-else-if="field.Field.type == FieldTypeEnum.Many2oneField">
              <template v-if="editable && editingCell.rowIndex == rowIndex && editingCell.colIndex == colIndex">
                <MyMany2OneSelect v-model="editingValue"
                                  :service="field.Field.relativeServiceName"
                                  :field="field.Field.name"
                                  :htmlId="field.Field.name"
                                  :htmlName="field.Field.name"/>
              </template>
              <template v-else>
                {{
                  getValue(field, row) ? getValue(field, row)[serviceStore.getServiceByName(field.Field.relativeServiceName).nameField] : ''
                }}
              </template>
            </template>
            <template v-else-if="field.Field.type == FieldTypeEnum.ImageField">
              <template v-if="editable && editingCell.rowIndex == rowIndex && editingCell.colIndex == colIndex">
                <MyImageUpload v-model="editingValue" @blur="editingCellSave"></MyImageUpload>
              </template>
              <template v-else>
                <MyImage width="50" height="50" :src="getImageUrl(getValue(field,row))"></MyImage>
              </template>
            </template>
            <template v-else-if="field.Field.type == FieldTypeEnum.VideoField">
              <MyVideo width="94" height="94" :src="getVideoUrl(getValue(field,row))"></MyVideo>
            </template>
            <template v-else-if="field.Field.type == FieldTypeEnum.Many2manyField">
              <template v-if="editable && editingCell.rowIndex == rowIndex && editingCell.colIndex == colIndex">
                <MyMany2manySelect v-model="editingValue"
                                   :ref="field.Field.name+'_input'"
                                   :service="field.Field.relativeServiceName"
                                   :field="field.Field.name"
                                   :htmlId="field.Field.name"
                                   :htmlName="field.Field.name"/>
              </template>
              <template v-else>
                {{ getMany2manyFormField(row, getValue(field, row), field.Field) }}
                <MyMany2manySelect v-model="row[field.Field.name+'_many']" :readonly="true"
                                   :ref="field.Field.name+'_input'"
                                   :service="field.Field.relativeServiceName"
                                   :field="field.Field.name"
                                   :htmlId="field.Field.name"
                                   :htmlName="field.Field.name"/>
              </template>
            </template>
            <template v-else-if="field.Field.type == FieldTypeEnum.BooleanField">
              <template v-if="editable && editingCell.rowIndex == rowIndex && editingCell.colIndex == colIndex">
                <my-check v-model="editingValue" @blur="editingCellSave"/>
              </template>
              <template v-else>
                <template v-if="getValue(field, row)">
                  <MyIcon icon="square-check" type="fas" color="#3a7afe"/>
                </template>
                <template v-else>
                  <MyIcon icon="square-xmark" type="fas" color="#183153"/>
                </template>
              </template>
            </template>
            <template v-else-if="field.Field.type == FieldTypeEnum.DateTimeField">
              <template v-if="editable && editingCell.rowIndex == rowIndex && editingCell.colIndex == colIndex">
                <my-datetime v-model="editingValue" @blur="editingCellSave"/>
              </template>
              <template v-else>
                {{ getValue(field, row) ? getDateTime(getValue(field, row)) : '' }}
              </template>
            </template>
            <template v-else-if="field.Field.type == FieldTypeEnum.DateField">
              <template v-if="editable && editingCell.rowIndex == rowIndex && editingCell.colIndex == colIndex">
                <my-date v-model="editingValue" @blur="editingCellSave"/>
              </template>
              <template v-else>
                {{ getValue(field, row) ? getDate(getValue(field, row)) : '' }}
              </template>
            </template>
            <template v-else-if="field.Field.type == FieldTypeEnum.TimeField">
              <template v-if="editable && editingCell.rowIndex == rowIndex && editingCell.colIndex == colIndex">
                <my-time v-model="editingValue" @blur="editingCellSave"/>
              </template>
              <template v-else>
                {{ getValue(field, row) }}
              </template>
            </template>
            <template v-else-if="field.Field.type == FieldTypeEnum.PasswordField">
              <template v-if="editable && editingCell.rowIndex == rowIndex && editingCell.colIndex == colIndex">
                <my-password v-model="editingValue" @blur="editingCellSave"/>
              </template>
              <template v-else>
                {{ getPasswordValue(field, row) }}
              </template>

            </template>
            <template v-else>
              <template v-if="editable && editingCell.rowIndex == rowIndex && editingCell.colIndex == colIndex">
                <my-input v-model="editingValue" @blur="editingCellSave"/>
              </template>
              <template v-else>
                {{ getValue(field, row) }}
              </template>
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