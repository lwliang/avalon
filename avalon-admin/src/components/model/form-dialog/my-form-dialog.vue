<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import './my-form-dialog.css';
import MyDialog from "../../dialog/my-dialog.vue";
import {
  compile,
  ComponentInternalInstance,
  createVNode,
  defineComponent,
  getCurrentInstance,
  ref,
  shallowRef,
  watch
} from "vue";
import {useFieldStore} from "../../../global/store/fieldStore.ts";
import {useServiceStore} from "../../../global/store/serviceStore.ts";
import ActionView from "../../../model/view/ActionView.ts";
import {getModelDetailApi} from "../../../api/modelApi.ts";
import {parserEx} from "../../../xml/XMLParser.ts";
import Field from "../../../model/Field.ts";
import {getTemplate, XMLParserResult} from "../../../xml/XMLParserResult.ts";
import {FieldTypeEnum} from "../../../model/enum-type/FieldTypeEnum.ts";
import {cloneDeep} from "lodash";
import {getActionFormView, getActionTreeView, getOnChangeFields, onChangeValue} from "../../../api/commonApi.ts";
import Service from "../../../model/Service.ts";
import {
  getJoinFirstField,
  getJoinLastField,
  getServiceField,
  hasJoin
} from "../../../util/fieldUtils.ts";
import {isObjectEmpty} from "../../../util/ObjectUtils.ts";
import {ElNotification} from "element-plus";

const emit = defineEmits(['close', 'sure'])
defineOptions({
  name: 'MyFormModel',
})

const props = defineProps<{
  title: string,
  service: string,
  oldRecordRow?: any,
  rowId: Symbol | number | undefined
}>()

const serviceFieldStore = useFieldStore()
const serviceStore = useServiceStore()

const primaryService = ref<Service>()
serviceStore.getServiceByNameAsync(props.service).then(data => {
  primaryService.value = data
})
const view = ref<ActionView | undefined>(undefined)

const show = defineModel<boolean>({required: true})

const loadView = async (service: string) => {
  return await getActionFormView(service).then(data => {
    if (data && data.length) {
      return data[0]
    }
    return null
  })
}
loadView(props.service).then((dataView: any) => {
  if (dataView) {
    view.value = dataView
    renderView(dataView.arch).then(() => {
      loadDataWithLayout()
    })
  }
})

const recordRow = ref<any>({})
const originalRecordRow = ref<any>({}) // 保存原始数据用于比较变化

const loadDataWithLayout = async () => {
  if (!(props.service && template_fields.value.length)) {
    return;
  }
  
  if (props.rowId && !(typeof props.rowId == 'symbol')) {
    // 编辑模式：加载现有数据
    const data = await getModelDetailApi(props.rowId as number, template_fields.value.join(","), props.service)
    if (data) {
      Object.assign(recordRow.value, data)
      originalRecordRow.value = cloneDeep(data) // 保存原始数据
      if (props.oldRecordRow) {
        Object.assign(recordRow.value, props.oldRecordRow)
      }
      template_component.value = createFormTemplateVNode();
    }
  } else {
    // 新增模式：创建默认字段
    await createNewRecordRow();
    if (props.oldRecordRow) {
      Object.assign(recordRow.value, props.oldRecordRow)
    }
    template_component.value = createFormTemplateVNode();
  }
}

// 创建新记录行
const createNewRecordRow = async () => {
  const serviceFields = await serviceFieldStore.getFieldByServiceNameAsync(props.service)
  
  for (let key of template_fields.value) {
    if (hasJoin(key)) {
      const first = getJoinFirstField(key); // 只支持二级字段
      const firstField = await getServiceField(props.service, first);
      const last = getJoinLastField(key);
      const lastField = await getServiceField(props.service, key);
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
  originalRecordRow.value = cloneDeep(recordRow.value)
}

const createFormTemplateVNode = () => {
  const component = defineComponent({
    setup() {
      const vNode = compile(xmlTemplate.value)
      return () => {
        return createVNode(vNode, {...recordRow.value, recordRow:recordRow.value,rules:{}})
      }
    }
  })

  return component;
}

const xmlTemplate = ref<any>(null)
const template_component = shallowRef<any>(null)
let template_fields = ref<string[]>([]);

let parserResult: XMLParserResult | null = null;

const renderView = async (arch: string) => {
  await parserXml(arch)
}

const parserXml = async (str: string) => {
  parserResult = await parserEx(str, props.service)
  const tempService = await serviceStore.getServiceByNameAsync(props.service)
  const serviceFields = await serviceFieldStore.getFieldByServiceNameAsync(props.service)
  xmlTemplate.value = getTemplate(parserResult);
  template_fields.value.splice(0, template_fields.value.length)
  template_fields.value.push(...parserResult.fullFields.map(x => x.name))
  if (parserResult.one2ManyFields && parserResult.one2ManyFields.length) {
    for (let manyField of parserResult.one2ManyFields) {
      let find;
      if (!hasJoin(manyField)) { // 当前表字段
        find = serviceFields.find(x => x.name == manyField) as Field;
      } else {
        find = await getServiceField(props.service, manyField)
      }
      if (!find) {
        continue;
      }
      const viewData = await loadTreeView(find.relativeServiceName);
      const tempFields = serviceFieldStore.getFieldByServiceName(find.relativeServiceName)
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
  if (!template_fields.value.includes(tempService.keyField)) {
    template_fields.value.push(tempService.keyField)
  }
}

const loadTreeView = async (service: string) => {
  return await getActionTreeView(service).then(data => {
    if (data && data.length) {
      return data[0]
    }
    return null
  })
}

const closeClick = () => {
  emit('close')
}

const sureClick = async () => {
  const row: any = {}
  
  // 获取所有已更改的字段
  for (let key in recordRow.value) {
    if (JSON.stringify(recordRow.value[key]) !== JSON.stringify(originalRecordRow.value[key])) {
      row[key] = recordRow.value[key]
    }
  }
  
  // 添加主键（编辑模式）
  if (props.rowId && primaryService.value) {
    row[primaryService.value.keyField] = props.rowId
  }
  
  emit('sure', row)
}
/**
 * 加载表单字段监听列表
 * @param serviceName
 */
const onChangeFields = ref<String[]>([])
const loadOnChangeField = async (serviceName: string) => {
  return getOnChangeFields(serviceName)
}
loadOnChangeField(props.service).then(data => {
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
    const changeRecordRow = await onChangeValue(props.service, changeFields, recordRow.value, originalRecordRow.value)
    if (changeRecordRow.value) { // 返回值
      for (const fieldKey in changeRecordRow.value) {
        recordRow.value[fieldKey] = changeRecordRow.value[fieldKey]
      }
    }
    if (changeRecordRow.warnings && changeRecordRow.warnings.length > 0) { // 有异常
      for (const warning of changeRecordRow.warnings) {
        ElNotification({
          title: warning.title,
          message: warning.message,
          type: "warning",
        })
      }
    }
  }
}, {
  deep: true
})
</script>

<template>
  <my-dialog
    :title="title"
    v-model="show"
    :close-on-click-modal="false"
    :draggable="true"
    @close="closeClick"
    @sure="sureClick"
    body-class="min-h-[400px] max-h-[900px]"
  >
    <component :is="template_component"/>
  </my-dialog>
</template>

<style scoped>

</style>