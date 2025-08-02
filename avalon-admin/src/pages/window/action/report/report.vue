<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {compile, ComponentInternalInstance, createVNode, defineComponent, getCurrentInstance, provide, ref, shallowRef, watch} from "vue";
import {useRoute} from "vue-router";
import {createModelApi, getModelPageApi} from "../../../../api/modelApi.ts";
import ActionView from "../../../../model/view/ActionView.ts";
import {getTemplate, XMLParserResult} from "../../../../xml/XMLParserResult.ts";
import {parserEx} from "../../../../xml/XMLParser.ts";
import {useFieldStore} from "../../../../global/store/fieldStore.ts";
import {useServiceStore} from "../../../../global/store/serviceStore.ts";
import {getPageSize} from "../../../../api/env.ts";
import Field from "../../../../model/Field.ts";
import {getActionReportView} from "../../../../api/commonApi.ts";
import Service from "../../../../model/Service.ts";
import { getAllFieldRecordRow } from "../../../../util/fieldUtils.ts";

const route = useRoute();
const serviceFieldStore = useFieldStore()
const serviceStore = useServiceStore()

const moduleName = ref<string>(route.params.module as string)
const serviceName = ref<string>(route.params.service as string)
const service = ref<Service | undefined>(undefined)
serviceStore.getServiceByNameAsync(serviceName.value).then(data => {
  service.value = data
})

const view = ref<ActionView | undefined>(undefined)

const componentRegistry = ref<Map<string, any>>(new Map())

// 提供注册方法
provide('registerReportComponent', async (id: string, instance: any) => {
  componentRegistry.value.set(id, instance)
  if(instance) {
    const serviceFields = await serviceFieldStore.getFieldByServiceNameAsync(serviceName.value)
    const param = await getAllFieldRecordRow(recordRow.value, serviceFields)
    instance.loadData(param)
    
  }
})

getActionReportView(serviceName.value).then(data => {
  if (data && data.length) {
    view.value = data[0]
    renderView();
  }
})

const xmlTemplate = ref<any>(null)
let table_fields_component = shallowRef<any>(null)

const renderView = () => {
  if (view.value) {
    parserXml(view.value.arch)
  }
}

let template_fields = ref<string[]>([]);
let template_fields_meta = ref<Field[]>([])
let template_fields_component = shallowRef<any>(null)
let filter_fields_template = ref<any>(null)

let parserResult: XMLParserResult | null = null;

const parserXml = async (str: string) => {
  parserResult = await parserEx(str, serviceName.value)
  xmlTemplate.value = getTemplate(parserResult)
  filter_fields_template.value = parserResult.report.filterTemplate
  template_fields_component.value = createReportFilterTemplateVNode()
  template_fields.value.splice(0, template_fields.value.length)
  template_fields.value.push(...parserResult.fields.map(field => field.name))
  const serviceFields = await serviceFieldStore.getFieldByServiceNameAsync(serviceName.value)
  template_fields_meta.value.splice(0, template_fields_meta.value.length)
  template_fields.value.forEach(field => {
    recordRow.value[field] = null
    const fieldMeta = serviceFields.find(fieldMeta => fieldMeta.name == field)
    if(fieldMeta){
      template_fields_meta.value.push(fieldMeta)
    }
  })
  const data = await createModelApi({}, serviceName.value)
  for(const obj in data){
    recordRow.value[obj] = data[obj]
  }
  table_fields_component.value = createReportTableTemplateVNode()
  
  // 解析报表字段
  await parseReportFields()
}

// 解析报表字段
const parseReportFields = async () => {

}

const reportRefs = ref<any[]>([])

const {proxy} = getCurrentInstance() as ComponentInternalInstance;

const record = ref<any>([])
const pageNum = ref<number>(1)
const condition = ref<string>('')
const begin = ref<number>(0)
const end = ref<number>(0)
const total = ref<number>(0)

// 过滤器字段
const recordRow = ref<any>({

})

const createReportFilterTemplateVNode = () => {
  let component = defineComponent({
    setup() {
      const vNode = compile(filter_fields_template.value)
      return () => {
        return createVNode(vNode, {
          formModel:'report',
          ...recordRow.value,
          recordRow: recordRow.value,
          btnQueryClick:btnQueryClick,
          rules: {
          }
        })
      }
    }
  })
  return component;
}

const createReportTableTemplateVNode = () => {
  let component = defineComponent({
    setup() {
      const vNode = compile(xmlTemplate.value)
      return () => {
        return createVNode(vNode, {
          serviceName: serviceName.value,
          formModel:'report',
          ...recordRow.value,
          recordRow: recordRow.value,
          btnQueryClick:btnQueryClick,
          rules: {
          }
        })
      }
    }
  })
  return component;
}


const btnQueryClick = async () => {
  const serviceFields = await serviceFieldStore.getFieldByServiceNameAsync(serviceName.value)
  const param = await getAllFieldRecordRow(recordRow.value, serviceFields)
  componentRegistry.value.forEach(ref => {
    ref.loadData(param)
  })
}



</script>

<template>
  <div class="h-full p-4 overflow-hidden flex flex-col box-border">
    <!-- 过滤器区域 -->
    <div class="w-full mb-4">
      <div class="bg-white p-4 rounded-lg shadow-sm border">
        <div class="text-lg font-semibold mb-4">{{ service?.label }}</div>
        <div>
            <component :is="template_fields_component" />
        </div>
      </div>
    </div>

    
    <!-- 报表表格区域 -->
    <div class="flex-1 overflow-hidden">
      <el-scrollbar>
        <component :is="table_fields_component" />
      </el-scrollbar>
    </div>
  </div>
</template>

<style scoped>

</style>
