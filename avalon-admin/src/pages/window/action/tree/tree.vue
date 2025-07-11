<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {
  compile,
  ComponentInternalInstance, computed,
  createVNode,
  getCurrentInstance,
  inject,
  provide,
  ref,
  shallowRef,
  watch
} from "vue";
import {useRoute} from "vue-router";
import ActionView from "../../../../model/view/ActionView.ts";
import {
  createModelApi,
  deleteMultiModelApi,
  getModelAllApi,
  getModelPageApi,
  invokeMethod,
  saveMultiModelApi
} from "../../../../api/modelApi.ts";
import {getTemplate, XMLParserResult} from "../../../../xml/XMLParserResult.ts";
import {parserEx} from "../../../../xml/XMLParser.ts";
import {useFieldStore} from "../../../../global/store/fieldStore.ts";
import {useServiceStore} from "../../../../global/store/serviceStore.ts";
import Field from "../../../../model/Field.ts";
import {getSelectionValueByServiceAndField} from "../../../../cache/SelectionValueMemory.ts";
import {FieldTypeEnum} from "../../../../model/enum-type/FieldTypeEnum.ts";
import {exportExcel, getActionTreeView} from "../../../../api/commonApi.ts";
import {getPageSize} from "../../../../api/env.ts";
import MyTable from "../../../../components/table/my-table.vue";
import FormField from "../../../../model/FormField.ts";
import MySearch from "../../../../components/search/my-search.vue";
import MyDialog from "../../../../components/dialog/my-dialog.vue";
import MyExportDialog from "../../../../components/dialog/my-export-dialog.vue";
import MyFormModel from "../../../../components/model/form-dialog/my-form-dialog.vue";
import {goModelImport} from "../../../../util/routerUtils.ts";
import {getModelKeyValue, getServiceField, hasJoin, isModelKeyValue} from "../../../../util/fieldUtils.ts";
import ShowField from "../../../../model/ShowField.ts";
import TreeXml from "../../../../xml/TreeXml.ts";
import {EditableType} from "../../../../xml/xmlType.ts";
import {objectCloneDeep} from "../../../../util/ObjectUtils.ts";
import {uploadFile} from "../../../../api/fileUploadApi.ts";
import {getActionFormView} from "../../../../api/commonApi.ts";
import {addModelApi} from "../../../../api/modelApi.ts";

const {proxy} = getCurrentInstance() as ComponentInternalInstance;
const serviceFieldStore = useFieldStore()
const serviceStore = useServiceStore()

const route = useRoute();

const moduleName = ref<string>(route.params.module as string)
const serviceName = ref<string>(route.params.service as string)
const rowClickHandler = inject('rowClick') as (id: number | undefined) => void;
const serviceKeyField = ref<string>('')

serviceStore.getServiceByNameAsync(serviceName.value).then(data => {
  serviceKeyField.value = data.keyField;
})

const view = ref<ActionView | undefined>(undefined)
getActionTreeView(serviceName.value).then(data => { // 加载xml
  if (data && data.length) {
    view.value = data[0]
    renderView();
  }
})

const xmlTemplate = ref<any>(null)

const renderView = async () => {
  if (view.value) {
    await parserXml(view.value.arch)
    loadData()
  }
}

let template_fields = ref<string[]>([]);
let template_full_fields = ref<string[]>([]);
let services_fields = ref<ShowField[]>([]);
const headerTemplate = ref<any>(null)
const header_component = shallowRef<any>(null)

let parserResult: XMLParserResult | null = null;

let tree: TreeXml | undefined = undefined;
const editable = ref<EditableType | undefined>(undefined);

const parserXml = async (str: string) => {
  const primaryKeyField = await serviceStore.getServiceByNameAsync(serviceName.value)
  const serviceFields = await serviceFieldStore.getFieldByServiceNameAsync(serviceName.value)
  parserResult = await parserEx(str, serviceName.value)
  xmlTemplate.value = getTemplate(parserResult)
  tree = parserResult.tree
  editable.value = tree.editable

  if (parserResult.header && parserResult.header.template) {
    headerTemplate.value = parserResult.header.template
  }
  template_fields.value.splice(0, template_fields.value.length)
  template_fields.value.push(...parserResult.fields.map(x => x.name))
  if (!template_fields.value.includes(primaryKeyField.keyField)) {
    template_fields.value.push(primaryKeyField.keyField)
  }

  template_full_fields.value.splice(0, template_full_fields.value.length)
  template_full_fields.value.push(...parserResult.fullFields.map(x => x.name))
  if (!template_full_fields.value.includes(primaryKeyField.keyField)) {
    template_full_fields.value.push(primaryKeyField.keyField)
  }

  services_fields.value.splice(0, services_fields.value.length)
  for (const key of template_fields.value) {
    let field;
    if (!hasJoin(key)) {
      field = serviceFields.find(f => f.name === key)
    } else {
      field = await getServiceField(serviceName.value, key);
    }
    if (field) {
      services_fields.value.push({Field: field, originField: key, serviceName: serviceName.value})
    }
  }

  createHeaderTemplateVNode();
}

const createHeaderTemplateVNode = () => {
  if (!headerTemplate.value) return
  const vNode = compile(headerTemplate.value)
  const btnClickHandler = async (actionType: string, action: string) => {
    console.log('btnClick', actionType, action)

    let param: any = {
      serviceName: serviceName.value,
      method: action,
      param: {}
    };
    if (rowSelectCount.value) {
      param.param.ids = rowSelectIds.value
    }
    if (serviceName.value) {
      const result = await invokeMethod(serviceName.value, param);
      if (!result) { // 没有返回值
        proxy?.$notify.success({
          title: "提示",
          message: "操作成功",
        });
      } else {
        if (result.type && result.type == 'ir.actions.client') { // 判断前端动作
          const service = proxy?.$registry.getAll('actions').get(result.tag) as any
          if (service) {
            service.execute(result.param);
          }
        }

      }
    }
  }
  header_component.value = () => {
    return createVNode(vNode, {btnClickHandler})
  }
}

const emit = defineEmits(['rowClick'])

const record = ref<any>([])
const recordOrigin = ref<any>([])
const recordChange = ref<any>([])
const pageNum = ref<number>(1)
const selectionDynamic = ref<any>({})
const condition = ref<string>('')
const begin = ref<number>(0)
const end = ref<number>(0)
const total = ref<number>(0)
const loadData = async () => {
  if (!(serviceName.value && moduleName.value && template_fields.value.length)) {
    return;
  }
  let pageInfo = await getModelPageApi(template_full_fields.value.join(","),
      condition.value,
      serviceName.value,
      pageNum.value)

  if (pageInfo.data) {
    record.value.splice(0, record.value.length);
    record.value = pageInfo.data
    recordOrigin.value.splice(0, recordOrigin.value.length)
    recordChange.value.splice(0, recordChange.value.length)
    recordOrigin.value.push(...objectCloneDeep(pageInfo.data))
  }
  begin.value = (pageInfo.pageCur - 1) * getPageSize() + 1
  end.value = begin.value + pageInfo.data.length - 1
  total.value = pageInfo.total

  for (let field of services_fields.value) {
    if (field.Field.type == FieldTypeEnum.SelectionField) { // 得到字段对应的selection的值
      selectionDynamic.value[field.Field.name] = await getSelectionValueByServiceAndField(serviceName.value, field.Field.name)
    }
  }
}


const rowClick = (row: any) => {
  serviceStore.getServiceByNameAsync(serviceName.value).then(service => {
    rowClickHandler(row[service.keyField])
  })
}

const rowTargetShow = ref(false)
const rowTargetTitle = ref('')
const rowTargetId = ref()
const selectRow = ref()
const subRowSureClick = (row: any) => {
  rowTargetShow.value = false
  if (selectRow.value) {
    Object.assign(selectRow.value, row)
  } 
  
  addModelApi(selectRow.value, serviceName.value).then(data => {
    loadData()
  })
}

const subRowCloseClick = () => {
  rowTargetShow.value = false
}
const getFieldRecordRow = async (recordRowField: Record<string, FormField | any>) => {
  const recordRow = {} as any;
  for (let fieldKey in recordRowField) {
    if (recordRowField[fieldKey] instanceof FormField) {
      if (recordRowField[fieldKey].isChanged()) {
        recordRow[fieldKey] = await recordRowField[fieldKey].getRawValue()
      }
    } else {
      if (!recordRow[fieldKey]) {
        recordRow[fieldKey] = {}
      }
      for (let x in recordRowField[fieldKey]) {
        recordRow[fieldKey][x] = await recordRowField[fieldKey][x].getRawValue()
      }
    }
  }
  return recordRow
}

const createServiceClick = async () => {
  if (!editable.value) { // 不在tree中编辑
    const formView = await getActionFormView(serviceName.value)
    if (formView && formView.length && formView[0].target == 'new') { // 弹出Form窗口
      rowTargetShow.value = true
      rowTargetId.value = undefined
      const defaultValue = await createModelApi({}, serviceName.value)
      if (serviceKeyField.value) {
        defaultValue[serviceKeyField.value] = getModelKeyValue()
      }
      rowTargetTitle.value = formView[0].label
      selectRow.value = defaultValue
    } else {
      rowClickHandler(undefined)
    }
  } else {
    const defaultValue = await createModelApi({}, serviceName.value)
    if (serviceKeyField.value) {
      defaultValue[serviceKeyField.value] = getModelKeyValue()
    }
    if (editable.value == 'bottom') {
      record.value.push(defaultValue)
    } else {
      record.value.unshift(defaultValue)
    }
  }
}

const saveServiceClick = async () => {
  for (const row of recordChange.value) { // 检查需要二次上传的图片
    for (let fieldName in row) {
      if (row[fieldName] instanceof File) { // 文件
        const urlObj = await uploadFile(row[fieldName])
        row[fieldName] = urlObj.url
      }
      const serviceFields = await serviceFieldStore.getFieldByServiceNameAsync(serviceName.value)
      const field = serviceFields.find(x => x.name == fieldName)
      if (field && field.type == FieldTypeEnum.Many2oneField) { // many2one
        const primaryKeyField = await serviceStore.getServiceByNameAsync(field.relativeServiceName)
        row[fieldName] = row[fieldName][primaryKeyField.keyField]
      }
      if (fieldName == serviceKeyField.value && isModelKeyValue(row[fieldName])) { // 去除前端新增主键
        row[fieldName] = undefined
      }
    }
  }
  saveMultiModelApi(recordChange.value, serviceName.value).then(result => {
    loadData()
  })
}

const deleteServiceClick = () => {
  deleteShow.value = true
}

const getMany2manyFormField = (obj: any, value: any, field: Field) => {
  obj[field.name + '_many'] = new FormField(value, field)
  return ''
}
const getFormField = (obj: any, value: any, field: Field) => {
  obj[field.name + '_field'] = new FormField(value, field)
  return ''
}

const conditionChange = (search: string) => {
  condition.value = search;
  pageNum.value = 1

  loadData();
}

const handlePageChange = (dir: string) => {
  if (dir == 'right') { // 下一页
    pageNum.value = pageNum.value + 1;
  } else {
    pageNum.value = pageNum.value - 1;
  }
  loadData()
}
const rowSelectCount = ref(0)
const rowSelectIds = ref<any[]>([])
const rowSelectChangeHandler = (selectCount: number, ids: any[]) => {
  rowSelectCount.value = selectCount

  rowSelectIds.value.splice(0, rowSelectIds.value.length)
  if (selectCount) {
    rowSelectIds.value.push(...ids)
  }
}

const colFieldSearchHandler = (fieldName: string, operate: string, value: any) => {
  condition.value = `('${fieldName}',${operate}, '${value}')`
  pageNum.value = 1
  loadData()
}

const deleteShow = ref(false)

const hideClick = () => {
  deleteShow.value = false
  rowSelectIds.value.splice(0, rowSelectIds.value.length)
}
const sureClick = () => {
  deleteMultiModelApi(rowSelectIds.value, serviceName.value as string).then(data => {
    proxy?.$notify.success({
      title: "提示",
      message: "删除完成",
    });
    deleteShow.value = false

    for (let deleteIdsKey of rowSelectIds.value) {
      const index = record.value.findIndex((x: any) => x.id == deleteIdsKey)
      if (index >= 0) {
        record.value.splice(index, 1)
      }
    }

  })
}

const exportShow = ref(false)
const exportClose = () => {
  exportShow.value = false
}
const exportOpen = () => {
  exportShow.value = true
}
const exportSure = async (fields: string) => {
  const primaryKeyField = await serviceStore.getServiceByNameAsync(serviceName.value)
  let condition = "";
  if (rowSelectIds.value.length) {
    condition = `('${primaryKeyField.keyField}',in,${rowSelectIds.value.join(",")})`
  }
  exportExcel(serviceName.value, fields, condition, "").then(data => {
    proxy?.$notify.success({
      title: "提示",
      message: "导出成功",
    });
  })
}

const importExcelClick = () => {
  goModelImport(moduleName.value, serviceName.value, {})
}

const isRecordModify = computed(() => {
  return !!recordChange.value.length
})

const cellFieldHandler = async (key: any, fieldName: string, value: any) => {
  let obj = recordChange.value.find((x: any) => x[serviceKeyField.value] == key)
  let originObj = recordOrigin.value.find((x: any) => x[serviceKeyField.value] == key)
  if (obj) {
    if (!originObj || originObj[fieldName] != value) {
      obj[fieldName] = value
    }
  } else {
    if (!originObj || originObj[fieldName] != value) {
      obj = {}
      obj[fieldName] = value
      obj[serviceKeyField.value] = key
      recordChange.value.push(obj)
    }
  }
}
</script>

<template>
  <div class="flex flex-col flex-wrap p-4 items-start h-full">
    <div class="h-[50px] flex items-start w-full ">
      <div class="flex-1 h-[34px] flex items-center">
        <el-button-group>
          <el-button type="primary" size="small" @click="createServiceClick">新增</el-button>
          <el-button type="success" size="small" @click="saveServiceClick" v-if="isRecordModify">保存</el-button>
          <el-button type="primary" size="small" @click="importExcelClick">导入</el-button>
          <el-button type="danger" size="small" @click="deleteServiceClick" v-if="rowSelectCount">删除</el-button>
          <el-button type="success" size="small" @click="exportOpen" v-if="rowSelectCount">导出</el-button>
          <component :is="header_component"/>
        </el-button-group>
      </div>
      <div class="flex-1 px-4">
        <MySearch @conditionChange="conditionChange" :full-width="true" class="w-full"
                  :serviceName="serviceName"></MySearch>
      </div>
      <div class="flex-1 flex justify-end h-[34px] flex items-center">
        <MyPagination v-model:total="total" v-model:begin="begin" v-model:end="end"
                      @pageChange="handlePageChange"></MyPagination>
      </div>
    </div>
    <div class="w-full flex-1 flex flex-col h-full overflow-y-auto">
        <MyTable :editable="!!editable" height="100%" :record="record" :fields="services_fields"
                 :service-name="serviceName"
                 :key-field="serviceKeyField"
                 :showSelectBtn="true"
                 :enableFilter="true"
                 @rowClick="rowClick"
                 @rowSelectChange="rowSelectChangeHandler"
                 @colFieldSearch="colFieldSearchHandler"
                 @cellChange="cellFieldHandler"/>
    </div>
  </div>
  <MyDialog :show="deleteShow" @close="hideClick" @sure="sureClick" title="提示">
    确认删除吗?
  </MyDialog>
  <MyExportDialog v-model="exportShow" :service="serviceName" @close="exportClose" @sure="exportSure"></MyExportDialog>

  <MyFormModel v-if="rowTargetShow" v-model="rowTargetShow"
                 :title="rowTargetTitle"
                 :service="serviceName"
                 :row-id="rowTargetId"
                 :old-record-row="selectRow"
                 @close="subRowCloseClick"
                 @sure="subRowSureClick"></MyFormModel>
</template>

<style scoped>

</style>