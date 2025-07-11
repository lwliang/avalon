<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {
  compile,
  ComponentInternalInstance,
  computed,
  createVNode,
  defineComponent,
  getCurrentInstance, provide,
  ref,
  shallowRef,
  watch
} from "vue";
import {useRoute} from "vue-router";
import ActionView from "../../../../model/view/ActionView.ts";
import {addModelApi, createModelApi, editModelApi, getModelDetailApi, invokeMethod} from "../../../../api/modelApi.ts";
import {getTemplate, XMLParserResult} from "../../../../xml/XMLParserResult.ts";
import {parserEx} from "../../../../xml/XMLParser.ts";
import {useFieldStore} from "../../../../global/store/fieldStore.ts";
import {useServiceStore} from "../../../../global/store/serviceStore.ts";
import MyButton from "../../../../components/button/my-button.vue";
import {FieldTypeEnum} from "../../../../model/enum-type/FieldTypeEnum.ts";
import Field from "../../../../model/Field.ts";
import {goModelWindow, replaceModelForm} from "../../../../util/routerUtils.ts";
import {getActionFormView, getActionTreeView, getOnChangeFields, onChangeValue} from "../../../../api/commonApi.ts";
import MyServiceLog from "../../../../components/service-log/my-service-log.vue";
import {getJoinFirstField, getJoinLastField, getServiceField, hasJoin} from "../../../../util/fieldUtils.ts";
import {getModuleIcon} from "../../../../api/moduleApi.ts";
import {refreshPage} from "../../../../util/commonUtils.ts";
import Service from "../../../../model/Service.ts";
import Form from "../../../../model/form/Form.ts";
import ServiceInvokeParam from "../../../../model/ServiceInvokeParam.ts";
import {isObjectEmpty} from "../../../../util/ObjectUtils.ts";
import {getChangeFieldRecordRow, getAllFieldRecordRow} from "../../../../util/fieldUtils.ts";
import {objectCloneDeep} from "../../../../util/ObjectUtils.ts";

const route = useRoute();
const serviceFieldStore = useFieldStore()
const serviceStore = useServiceStore()

const moduleName = ref<string>(route.params.module as string)
const row_id = ref<number | undefined>(parseInt(route.query.id as string))
const serviceName = ref<string>(route.params.service as string)
const form_container = ref()

const servicePropInstance = ref<Service>()
const {proxy} = getCurrentInstance() as ComponentInternalInstance;
serviceStore.getServiceByNameAsync(serviceName.value).then(data => {
  servicePropInstance.value = data
})

const view = ref<ActionView | undefined>(undefined)
defineOptions({
  name: 'Form',
})

const loadView = async (service: string) => {
  return await getActionFormView(service).then(data => {
    if (data && data.length) {
      return data[0]
    }
    return null
  })

}
loadView(serviceName.value).then((dataView: any) => {
  if (dataView) {
    view.value = dataView
    renderView(dataView.arch).then(() => {
      loadDataWithLayout()
    })
  }
})

const loadTreeView = async (service: string) => {
  return await getActionTreeView(service).then(data => {
    if (data && data.length) {
      return data[0]
    }
    return null
  })
}

const xmlTemplate = ref<any>(null)
const headerTemplate = ref<any>(null)
const template_component = shallowRef<any>(null)
const header_component = shallowRef<any>(null)

const renderView = async (arch: string) => {
  await parserXml(arch)
}

let template_fields = ref<string[]>([]); // 全部字段 用于查询数据库
let self_service_fields = ref<string[]>([]); // 自身第一级字段

let parserResult: XMLParserResult | null = null;
let form = ref<Form>({} as Form)

const parserXml = async (str: string) => {
  const serviceFields = await serviceFieldStore.getFieldByServiceNameAsync(serviceName.value)
  const primaryKeyField = await serviceStore.getServiceByNameAsync(serviceName.value)
  parserResult = await parserEx(str, serviceName.value)
  Object.assign(form.value, parserResult.form)
  xmlTemplate.value = getTemplate(parserResult);
  if (parserResult.header && parserResult.header.template) {
    headerTemplate.value = parserResult.header.template
  }
  template_fields.value.splice(0, template_fields.value.length)
  template_fields.value.push(...parserResult.fullFields.map(x => x.name))

  self_service_fields.value.splice(0, self_service_fields.value.length)
  self_service_fields.value.push(...parserResult.fields.map(x => x.name))

  if (parserResult.one2ManyFields && parserResult.one2ManyFields.length) {
    for (let manyField of parserResult.one2ManyFields) {
      let find;
      if (!hasJoin(manyField)) { // 当前表字段
        find = serviceFields.find(x => x.name == manyField) as Field;
      } else {
        find = await getServiceField(serviceName.value, manyField)
      }
      if (!find) {
        continue;
      }
      const viewData = await loadTreeView(find.relativeServiceName);
      const relativeService = await serviceStore.getServiceByNameAsync(find.relativeServiceName)
      const parserResult2 = await parserEx(viewData.arch, find.relativeServiceName)

      for (let tempField of parserResult2.fullFields) {
        template_fields.value.push(`${manyField}.${tempField.name}`)
      }
      if (!template_fields.value.includes(`${manyField}.${relativeService.keyField}`)) {
        template_fields.value.push(`${manyField}.${relativeService.keyField}`)
      }
    }
  }
  if (!template_fields.value.includes(primaryKeyField.nameField)) {
    template_fields.value.push(primaryKeyField.nameField)
  }
}

const recordRow = ref<any>({})
const originalRecordRow = ref<any>({}) // 保存原始数据用于比较变化
const recordRowIsChange = computed(() => { // 字段是否有变化
  return JSON.stringify(recordRow.value) !== JSON.stringify(originalRecordRow.value)
})

const loadDetailData = async (id: number) => {
  return getModelDetailApi(id, template_fields.value.join(","),
      serviceName.value);
}

const loadDataWithLayout = async () => {
  if (!(serviceName.value && moduleName.value && self_service_fields.value.length)) {
    return;
  }
  const serviceFields = await serviceFieldStore.getFieldByServiceNameAsync(serviceName.value)
  if (row_id.value) {
    const data = await loadDetailData(row_id.value);
    if (data) {
      Object.assign(recordRow.value, data)
      originalRecordRow.value = objectCloneDeep(data)
      template_component.value = createFormTemplateVNode();
      createHeaderTemplateVNode();
    }
  } else {
    await createNewRecordRow(serviceFields);
    const defaultValue = await createModelApi({}, serviceName.value)
    await createModelRecordRow(defaultValue, serviceFields)
    template_component.value = createFormTemplateVNode();
    createHeaderTemplateVNode();
  }
}

const createModelRecordRow = async (defaultValue: any, serviceFields: Field[]) => {
  for (let key in defaultValue) {
    let field = serviceFields.find(f => f.name === key)
    if (field) {
      recordRow.value[key] = defaultValue[key]
    }
  }
  // 保存原始数据
  originalRecordRow.value = JSON.parse(JSON.stringify(recordRow.value))
}

const createNewRecordRow = async (serviceFields: Field[]) => {
  for (let key of self_service_fields.value) {
    if (hasJoin(key)) {
      const first = getJoinFirstField(key); // 只支持二级字段
      const firstField = await getServiceField(serviceName.value, first);
      const last = getJoinLastField(key);
      const lastField = await getServiceField(serviceName.value, key);
      if (!recordRow.value[first]) {
        recordRow.value[first] = {}
      }
      if (lastField && (lastField.type == FieldTypeEnum.One2manyField ||
          lastField.type == FieldTypeEnum.Many2manyField)) {
        recordRow.value[first][last] = []
      } else if (lastField) {
        recordRow.value[first][last] = undefined
      }
    } else {
      let field = serviceFields.find(f => f.name === key)
      if (field && (field.type == FieldTypeEnum.One2manyField ||
          field.type == FieldTypeEnum.Many2manyField)) {
        recordRow.value[key] = []
      } else if (field) {
        recordRow.value[key] = undefined
      }
    }
  }
  // 保存原始数据
  Object.assign(originalRecordRow.value, recordRow.value)
}

const createFormTemplateVNode = () => {
  let component = defineComponent({
    setup() {
      const vNode = compile(xmlTemplate.value)
      return () => {
        return createVNode(vNode, {
          ...recordRow.value,
          recordRow: recordRow.value,
          rules: {
            'a':[{required: true, message: 'a', trigger: 'blur'}]
          }
        })
      }
    }
  })
  return component;
}

const createHeaderTemplateVNode = () => {
  if (!headerTemplate.value) return
  const vNode = compile(headerTemplate.value)
  const btnClickHandler = async (btnEvent: any) => {
    const action = btnEvent.action || btnEvent.currentTarget.getAttribute('action')
    let param = null;
    if (row_id.value) {
      param = {
        serviceName: serviceName.value,
        method: action,
        param: recordRow.value
      }
    } else {
      param = {
        serviceName: serviceName.value,
        method: action,
        param: recordRow.value
      }
    }
    if (serviceName.value) {
      const result = await invokeMethod(serviceName.value, param as any);
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
    return createVNode(vNode, {
      ...recordRow.value,
      recordRow: recordRow.value, 
      btnClickHandler,
    })
  }
}

// 新增，初始化对象
const createClick = async () => {
  row_id.value = undefined;
  recordRow.value = {}
  originalRecordRow.value = {}
  
  const serviceFields = await serviceFieldStore.getFieldByServiceNameAsync(serviceName.value)
  await createNewRecordRow(serviceFields);
  const defaultValue = await createModelApi({}, serviceName.value)
  await createModelRecordRow(defaultValue, serviceFields)
}

const backClick = () => {
  goModelWindow(moduleName.value, serviceName.value, {})
}

const saveClick = async () => {
  if (row_id.value) { // 保存
    update().then(() => {
      loadDetailData(row_id.value as number).then(data => {
        Object.assign(recordRow.value, data)
        Object.assign(originalRecordRow.value, data) // 更新原始数据
      })
    })
  } else {
    insert().then((data: any) => {
      replaceModelForm(moduleName.value, serviceName.value, data.id)
    })
  }
}

const insert = async () => {
  const serviceFields = await serviceFieldStore.getFieldByServiceNameAsync(serviceName.value)
  const changedData = await getChangeFieldRecordRow(recordRow.value, {}, serviceFields)

  return await addModelApi(changedData, serviceName.value).then(data => {
    proxy?.$notify.success({
      title: "新增",
      message: "新增成功",
    });
    row_id.value = data.id
    return data
  })
}



const update = async () => {
  const serviceFields = await serviceFieldStore.getFieldByServiceNameAsync(serviceName.value)
  const changedData = await getChangeFieldRecordRow(recordRow.value, originalRecordRow.value, serviceFields)
  
  if (Object.keys(changedData).length === 0) {
    proxy?.$notify.success({
      title: "修改",
      message: "修改成功",
    });
    return;
  }
  changedData["id"] = row_id.value

  return editModelApi(changedData, serviceName.value).then(data => {
    proxy?.$notify.success({
      title: "修改",
      message: "修改成功",
    });
  })
}

const scrollValue = ref<{ scrollTop: number, scrollLeft: number }>({scrollLeft: 0, scrollTop: 0})
provide('formScrollEvent', scrollValue)
const onScrollClick = (e: any) => {
  console.log("onScrollClick", e.target.scrollTop)
  Object.assign(scrollValue.value, {scrollTop: e.target.scrollTop, scrollLeft: e.target.scrollLeft})
}

/**
 * 加载表单字段监听列表
 * @param serviceName
 */
const onChangeFields = ref<String[]>([])
const loadOnChangeField = async (serviceName: string) => {
  return getOnChangeFields(serviceName)
}
loadOnChangeField(serviceName.value).then(data => {
  onChangeFields.value.push(...data)
})

watch(recordRow.value, async () => {
  let changeFields: any = {}
  for (let fieldKey in recordRow.value) {
    if (JSON.stringify(recordRow.value[fieldKey]) !== JSON.stringify(originalRecordRow.value[fieldKey])) {
      if (onChangeFields.value.includes(fieldKey)) {
        changeFields[fieldKey] = true
      }
    }
  }
  if (!isObjectEmpty(changeFields)) {
    const changeRecordRow = await onChangeValue(serviceName.value, changeFields, recordRow.value, originalRecordRow.value)
    if (changeRecordRow.value) { // 返回值
      for (const fieldKey in changeRecordRow.value) {
        recordRow.value[fieldKey] = changeRecordRow.value[fieldKey]
      }
    }
    if (changeRecordRow.warnings && changeRecordRow.warnings.length > 0) { // 有异常
      for (const warning of changeRecordRow.warnings) {
        proxy?.$notify.error({
          title: warning.title,
          message: warning.message,
        });
      }
    }
  }
}, {
  deep: true
})
</script>

<template>
  <div class="p-4 flex flex-col w-full overflow-hidden h-full box-border">
    <div class="h-[50px] flex items-start border-b border-t-0 border-l-0 border-r-0 border-border border-solid ">
      <div class="h-[50px] flex items-center">
        <el-button-group>
          <el-button type="success"  @click="backClick" icon="Back"  
          size="small" circle/>
          <el-button type="primary" round @click="createClick" v-if="form.create" size="small">新增</el-button>
          <el-button type="success" round @click="saveClick" class="ml-2" size="small"
                    v-if="recordRowIsChange && (form.edit || form.create)">
            保存
          </el-button>
          <component :is="header_component"/>
        </el-button-group>
      </div>
    </div>
    
      <div class="w-full flex flex-1 h-full box-border overflow-hidden">
          <el-scrollbar class="h-full flex-1">
            <div class="w-full box-border" style="flex: 2"
                @scroll="onScrollClick">
              <component :is="template_component"/>
            </div>
          </el-scrollbar>
      </div>
  
  </div>
</template>

<style scoped>
/* 确保滚动条视图容器占满高度 */
:deep(.el-scrollbar__view) {
  height: 100%;
  @apply flex;
}
</style>