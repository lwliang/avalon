<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import Field from "../../model/Field.ts";
import {FieldTypeEnum} from "../../model/enum-type/FieldTypeEnum.ts";
import {computed, ref, watch, nextTick} from "vue";
import {getSelectionValueByServiceAndField} from "../../cache/SelectionValueMemory.ts";
import {useServiceStore} from "../../global/store/serviceStore.ts";
import {ComputedRef} from "@vue/reactivity";
import {getFileUploadUrl, getVideoUploadUrl} from "../../api/env.ts";
import MyMany2manySelect from "../select/many2may-select/my-many2many-select.vue";
import FormField from "../../model/FormField.ts";
import {getDate, getDateTime, getTime} from "../../util/dateUtils.ts";
import MyDebug from "../debug/my-debug.vue";
import {useUserInfoStore} from "../../global/store/userInfoStore.ts";
import {useEventListener} from "@vueuse/core";
import MyVideo from "../video/my-video.vue";
import ShowField from "../../model/ShowField.ts";
import {getJoinFirstField, getJoinLastField, hasJoin} from "../../util/fieldUtils.ts";
import MyTableColSearch from "./my-table-col-search.vue";
import EditingCell from "./my-table.ts"
import MySelectionSelect from "../select/selection-select/my-selection-select.vue";
import MyMany2OneSelect from "../select/many2one-select/my-many2one-select.vue";
import {maskPassword} from "../../util/StringUtils.ts";
import MyPassword from "../password/my-password.vue";
import MyDatetime from "../datetime/my-datetime.vue";
import MyTime from "../time/my-time.vue";
import MyInput from "../input/my-input.vue";
import MyDate from "../date/my-date.vue";
import MyImageUpload from "../upload/my-image-upload.vue";
import {Filter, Delete, Plus} from '@element-plus/icons-vue';

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
const serviceStore = useServiceStore()
const tableRef = ref()
const multipleSelection = ref<any[]>([])

const loadData = async () => {
  if (!props.serviceName) {
    return;
  }

  for (let field of props.fields) {
    if (field.Field.type == FieldTypeEnum.SelectionField || field.Field.type == FieldTypeEnum.FieldSelectionField) {
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

watch(() => props.fields.length, (newValue) => {
  if (newValue) {
    loadData()
  }
}, {immediate: true})

const rowClick = (row: any, column: any, event: Event) => {
  // 如果是添加行，直接触发添加事件
  if (row._isAddRow) {
    return;
  }
  
  if (!props.editable) {
    if (column.index == -1) { // 是选择列
      return
    }
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
    return row[first] ? row[first][last] : '';
  }
  return row[field.originField]
}

const getPasswordValue = (field: ShowField, row: any) => {
  let password = '';
  if (hasJoin(field.originField)) {
    const first = getJoinFirstField(field.originField)
    const last = getJoinLastField(field.originField)
    password = row[first] ? row[first][last] : '';
  } else {
    password = row[field.originField]
  }
  return password ? maskPassword(password) : ''
}

const setValue = (field: ShowField, row: any, value: any) => {
  if (hasJoin(field.originField)) {
    const first = getJoinFirstField(field.originField)
    const last = getJoinLastField(field.originField)
    if (!row[first]) row[first] = {};
    row[first][last] = value;
  } else {
    row[field.originField] = value;
  }
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

// 选择相关
const handleSelectionChange = (selection: any[]) => {
  // 过滤掉添加行
  const realSelection = selection.filter(row => !row._isAddRow);
  multipleSelection.value = realSelection
  const ids = realSelection.map(row => row.id);
  emits('rowSelectChange', realSelection.length, ids)
}

const addRowClick = () => {
  emits('rowAddClick')
}

const fieldSearchClick = (fieldName: string, operate: string, value: any) => {
  emits('colFieldSearch', fieldName, operate, value);
}

// 编辑模式
const editingCell = ref<EditingCell>(new EditingCell(-1, -1));
const editingValue = ref<any>(null);

// 过滤掉主键字段
const visibleFields = computed(() => {
  return props.fields.filter(field => !field.Field.isPrimaryKey);
});

// 计算表格数据，如果showBtnRow为true，则在最后添加一行用于显示添加按钮
const tableData = computed(() => {
  if (props.showBtnRow) {
    return [...props.record, { _isAddRow: true }];
  }
  return props.record;
});

const editingCellChange = (rowIndex: number, colIndex: number, value: any, field: Field) => {
  // 如果是添加行，不进行编辑
  if (tableData.value[rowIndex]?._isAddRow) {
    return;
  }
  
  // 如果不是编辑模式，不阻止事件冒泡
  if (!props.editable) {
    return;
  }
  
  // 先保存当前编辑的值
  if (editingCell.value.rowIndex >= 0 && editingCell.value.colIndex >= 0) {
    const currentRowValue = props.record[editingCell.value.rowIndex]
    const currentFieldShow = visibleFields.value[editingCell.value.colIndex];
    const currentValue = editingValue.value;
    
    // 立即保存当前值
    setValue(currentFieldShow, currentRowValue, currentValue);
    if (props.keyField) {
      cellChangeEvent(currentRowValue[props.keyField], currentFieldShow.originField, currentValue);
    }
  }
  
  // 设置新的编辑状态
  editingCell.value.rowIndex = rowIndex;
  editingCell.value.colIndex = colIndex;
  editingValue.value = value;
}

const editingCellSave = () => {
  if (editingCell.value.rowIndex >= 0 && editingCell.value.colIndex >= 0) {
    const rowValue = props.record[editingCell.value.rowIndex]
    const fieldShow = visibleFields.value[editingCell.value.colIndex];
    const currentValue = editingValue.value;
    const currentRowIndex = editingCell.value.rowIndex;
    const currentColIndex = editingCell.value.colIndex;
    
    // 先清除编辑状态
    editingCell.value.rowIndex = -1;
    editingCell.value.colIndex = -1;
    editingValue.value = null;
    
    // 使用 nextTick 避免递归更新
    nextTick(() => {
      setValue(fieldShow, rowValue, currentValue);
      if (props.keyField) {
        cellChangeEvent(rowValue[props.keyField], fieldShow.originField, currentValue);
      }
    });
  }
}

const cellChangeEvent = (key: any, fieldName: string, value: any) => {
  emits('cellChange', key, fieldName, value);
}

// span-method 函数，用于合并添加行的单元格
const spanMethod = ({ row, column, rowIndex, columnIndex }: any) => {
  if (row._isAddRow) {
    // 如果是添加行，只在第一个数据列显示，其他列合并到第一列
    const selectionColumnCount = props.showSelectBtn ? 1 : 0;
    const dataColumnIndex = columnIndex - selectionColumnCount;
    
    if (dataColumnIndex === 0) {
      // 计算数据列的总数（不包括选择列和操作列）
      const totalDataColumns = visibleFields.value.length;
      return {
        rowspan: 1,
        colspan: totalDataColumns
      };
    } else if (dataColumnIndex > 0 && dataColumnIndex < visibleFields.value.length) {
      // 其他数据列隐藏
      return {
        rowspan: 0,
        colspan: 0
      };
    }
  }
  return {
    rowspan: 1,
    colspan: 1
  };
}

// 点击表格外部保存编辑
useEventListener(document, 'click', (e: MouseEvent) => {
  const el = tableRef.value?.$el
  if (!el) return

  const rect = el.getBoundingClientRect()
  const inside =
      e.clientX >= rect.left &&
      e.clientX <= rect.right &&
      e.clientY >= rect.top &&
      e.clientY <= rect.bottom

  if (!inside) {
    editingCellSave()
  }
})
</script>

<template>
    <el-table
        ref="tableRef"
        :data="tableData"
        height="100%"
        stripe
        border
        :show-header="true"
        :span-method="spanMethod"
        @row-click="rowClick"
        @selection-change="handleSelectionChange"
        class="w-full"
    >
      <!-- 选择列 -->
      <el-table-column
          v-if="showSelectBtn"
          type="selection"
          width="55"
          align="center"
          :index="-1"
          :selectable="(row: any) => !row._isAddRow"
      />

      <!-- 数据列 -->
      <el-table-column
          v-for="(field, colIndex) in visibleFields"
          :key="colIndex"
          :prop="field.originField"
          :label="field.Field.label"
          :min-width="120"
          :index="colIndex"
          show-overflow-tooltip
      >
        <template #header="{ column }">
          <div class="flex items-center">
            <span class="flex-1 font-bold">{{ field.Field.label }}</span>
            <template v-if="enableFilter && field.Field.canSearch">
              <el-popover
                  placement="top"
                  trigger="click"
                  width="300"
              >
                <template #reference>
                  <div class="h-full flex justify-center items-center ml-2">
                    <el-icon class="cursor-pointer">
                      <component :is="Filter"/>
                    </el-icon>
                  </div>
                </template>
                <template #default>
                  <my-table-col-search
                      @sureSearch="fieldSearchClick"
                      :service-name="serviceName"
                      :field="field.Field"
                  />
                </template>
              </el-popover>
            </template>
            <template v-if="userInfoStore.user.debug">
              <div class="px-2">
                <MyDebug :service="serviceName" :field="field.originField"/>
              </div>
            </template>
          </div>
        </template>

        <template #default="{ row, $index }">
          <!-- 添加行显示添加按钮 -->
          <template v-if="row._isAddRow">
            <div class="flex items-center h-full">
              <el-button type="primary" :icon="Plus" @click="addRowClick" size="small">
                添加行
              </el-button>
            </div>
          </template>
          <!-- 正常数据行 -->
          <template v-else>
            <div @click="editingCellChange($index, colIndex, getValue(field, row), field.Field)" class="w-full h-full">
            <!-- SelectionField -->
            <template v-if="field.Field.type == FieldTypeEnum.SelectionField">
              <template v-if="editable && editingCell.rowIndex == $index && editingCell.colIndex == colIndex">
                <my-selection-select
                    @click.stop="void 0"
                    :serviceName="serviceName"
                    :field="field.originField"
                    v-model="editingValue"
                />
              </template>
              <template v-else>
                {{ getSelectFormField(row, getValue(field, row), field.Field) }}
                <my-selection-select
                    :serviceName="serviceName"
                    :field="field.originField"
                    v-model="row[field.Field.name]"
                    :readonly="true"
                    :ref="field.Field.name+'_input'"
                />
              </template>
            </template>

            <!-- FieldSelectionField -->
            <template v-else-if="field.Field.type == FieldTypeEnum.FieldSelectionField">
              {{ getSelectionField(field, row) }}
            </template>

            <!-- Many2oneField -->
            <template v-else-if="field.Field.type == FieldTypeEnum.Many2oneField">
              <template v-if="editable && editingCell.rowIndex == $index && editingCell.colIndex == colIndex">
                <MyMany2OneSelect
                    v-model="editingValue"
                    :serviceName="field.Field.relativeServiceName"
                    :field="field.Field.name"
                />
              </template>
              <template v-else>
                {{
                  (() => {
                    const value = getValue(field, row);
                    const service = serviceStore.getServiceByName(field.Field.relativeServiceName);
                    return (value && service) ? value[service.nameField] : '';
                  })()
                }}
              </template>
            </template>

            <!-- ImageField -->
            <template v-else-if="field.Field.type == FieldTypeEnum.ImageField">
              <template v-if="editable && editingCell.rowIndex == $index && editingCell.colIndex == colIndex">
                <MyImageUpload v-model="editingValue" @blur="editingCellSave"/>
              </template>
              <template v-else>
                <el-image 
                  v-if="getValue(field,row)" 
                  :src="getImageUrl(getValue(field,row)) as string"
                  :preview-src-list="[getImageUrl(getValue(field,row)) as string]"
                  fit="cover"
                  style="width: 60px; height: 60px;"
                  :preview-teleported="true"
                  @click.stop="void 0"
                />
              </template>
            </template>

            <!-- VideoField -->
            <template v-else-if="field.Field.type == FieldTypeEnum.VideoField">
              <MyVideo width="94" height="94" :src="getVideoUrl(getValue(field,row))"/>
            </template>

            <!-- Many2manyField -->
            <template v-else-if="field.Field.type == FieldTypeEnum.Many2manyField">
              <template v-if="editable && editingCell.rowIndex == $index && editingCell.colIndex == colIndex">
                <MyMany2manySelect
                    v-model="editingValue"
                    :ref="field.Field.name+'_input'"
                    :serviceName="field.Field.relativeServiceName"
                    :field="field.Field.name"
                    :relativeForeignKeyName="field.Field.relativeForeignKeyName"
                />
              </template>
              <template v-else>
                {{ getMany2manyFormField(row, getValue(field, row), field.Field) }}
                <MyMany2manySelect
                    v-model="row[field.Field.name]"
                    :readonly="true"
                    :ref="field.Field.name+'_input'"
                    :serviceName="field.Field.relativeServiceName"
                    :field="field.Field.name"
                    :relativeForeignKeyName="field.Field.relativeForeignKeyName"
                />
              </template>
            </template>

            <!-- BooleanField -->
            <template v-else-if="field.Field.type == FieldTypeEnum.BooleanField">
              <template v-if="editable && editingCell.rowIndex == $index && editingCell.colIndex == colIndex">
                <el-checkbox v-model="editingValue" @change="editingCellSave"/>
              </template>
              <template v-else>
                <el-checkbox v-model="row[field.Field.name]" :disabled="true"/>
              </template>
            </template>

            <!-- DateTimeField -->
            <template v-else-if="field.Field.type == FieldTypeEnum.DateTimeField">
              <template v-if="editable && editingCell.rowIndex == $index && editingCell.colIndex == colIndex">
                <my-datetime v-model="editingValue" @blur="editingCellSave"/>
              </template>
              <template v-else>
                {{ getValue(field, row) ? getDateTime(getValue(field, row)) : '' }}
              </template>
            </template>

            <!-- DateField -->
            <template v-else-if="field.Field.type == FieldTypeEnum.DateField">
              <template v-if="editable && editingCell.rowIndex == $index && editingCell.colIndex == colIndex">
                <my-date v-model="editingValue" @blur="editingCellSave"/>
              </template>
              <template v-else>
                {{ getValue(field, row) ? getDate(getValue(field, row)) : '' }}
              </template>
            </template>

            <!-- TimeField -->
            <template v-else-if="field.Field.type == FieldTypeEnum.TimeField">
              <template v-if="editable && editingCell.rowIndex == $index && editingCell.colIndex == colIndex">
                <my-time v-model="editingValue" @blur="editingCellSave"/>
              </template>
              <template v-else>
                {{ getValue(field, row) }}
              </template>
            </template>

            <!-- PasswordField -->
            <template v-else-if="field.Field.type == FieldTypeEnum.PasswordField">
              <template v-if="editable && editingCell.rowIndex == $index && editingCell.colIndex == colIndex">
                <my-password v-model="editingValue" @blur="editingCellSave"/>
              </template>
              <template v-else>
                {{ getPasswordValue(field, row) }}
              </template>
            </template>

            <!-- 其他字段类型 -->
            <template v-else>
              <template v-if="editable && editingCell.rowIndex == $index && editingCell.colIndex == colIndex">
                <my-input v-model="editingValue" @blur="editingCellSave"/>
              </template>
              <template v-else>
                <div class="w-full h-full">
                  {{ getValue(field, row) || '&nbsp;' }}
                </div>
              </template>
            </template>
          </div>
          </template>
        </template>
      </el-table-column>

      <!-- 操作列 -->
      <el-table-column
          v-if="showDeleteBtn"
          label="操作"
          width="80"
          align="center"
          fixed="right"
      >
        <template #default="{ row }">
          <!-- 添加行不显示删除按钮 -->
          <template v-if="!row._isAddRow">
            <el-button
                type="danger"
                :icon="Delete"
                size="small"
                circle
                @click.stop="rowDeleteClick(row)"
            />
          </template>
        </template>
      </el-table-column>
    </el-table>
</template>