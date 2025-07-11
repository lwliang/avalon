<script setup lang="ts">
import MyTable from "../table/my-table.vue";
import MySearch from "../search/my-search.vue";
import {ref} from "vue";
import {getActionTreeView} from "../../api/commonApi.ts";
import ActionView from "../../model/view/ActionView.ts";
import {getTemplate, XMLParserResult} from "../../xml/XMLParserResult.ts";
import {parserEx} from "../../xml/XMLParser.ts";
import {useFieldStore} from "../../global/store/fieldStore.ts";
import {useServiceStore} from "../../global/store/serviceStore.ts";
import {getModelPageApi} from "../../api/modelApi.ts";
import {getPageSize} from "../../api/env.ts";
import {FieldTypeEnum} from "../../model/enum-type/FieldTypeEnum.ts";
import {getSelectionValueByServiceAndField} from "../../cache/SelectionValueMemory.ts";
import ShowField from "../../model/ShowField.ts";

/**
 * @author lwlianghehe@gmail.com
 * @date 2024/12/17 10:28
 */
const props = defineProps<{
  service: string,
}>()

const emit = defineEmits(['rowSelectChange'])

const serviceFieldStore = useFieldStore()
const serviceStore = useServiceStore()

const record = ref<any[]>([])
const tableHeight = ref('600px')

const conditionChange = (search: string) => {
  condition.value = search;
  loadServiceRecord()
}

const view = ref<ActionView | undefined>(undefined)
getActionTreeView(props.service).then(data => {
  if (data && data.length) {
    view.value = data[0]
    renderView();
  }
})
const xmlTemplate = ref<any>(null)

const renderView = () => {
  if (view.value) {
    parserXml(view.value.arch).then(() => {
      loadServiceRecord()
    })
  }
}


let parserResult: XMLParserResult | null = null;
let template_fields = ref<string[]>([]);
let template_full_fields = ref<string[]>([]);
let services_fields = ref<ShowField[]>([]);

const parserXml = async (str: string) => {
  parserResult = await parserEx(str, props.service)
  xmlTemplate.value = getTemplate(parserResult);
  const keyService = await serviceStore.getServiceByNameAsync(props.service)
  template_fields.value.splice(0, template_fields.value.length)
  template_fields.value.push(...parserResult.fields.map(x => x.name))
  if (!template_fields.value.includes(keyService.keyField)) {
    template_fields.value.push(keyService.keyField)
  }

  template_full_fields.value.splice(0, template_full_fields.value.length)
  template_full_fields.value.push(...parserResult.fullFields.map(x => x.name))
  if (!template_full_fields.value.includes(keyService.keyField)) {
    template_full_fields.value.push(keyService.keyField)
  }

  services_fields.value.splice(0, services_fields.value.length)
  const serviceFields = await serviceFieldStore.getFieldByServiceNameAsync(props.service)
  for (const key of template_full_fields.value) {
    const field = serviceFields.find(f => f.name === key)
    if (field) {
      services_fields.value.push({Field: field, originField: key, serviceName: props.service})
    }
  }
}

const pageNum = ref<number>(1)
const selectionDynamic = ref<any>({})
const condition = ref<string>('')
const begin = ref<number>(0)
const end = ref<number>(0)
const total = ref<number>(0)

const loadServiceRecord = async () => {
  if (!(props.service && template_fields.value.length)) {
    return;
  }
  const recordTemp: any = []
  await getModelPageApi(template_full_fields.value.join(","),
      condition.value,
      props.service,
      pageNum.value).then(pageInfo => {
    if (pageInfo.data) {
      recordTemp.push(...pageInfo.data)
    }
    begin.value = (pageInfo.pageCur - 1) * getPageSize() + 1
    end.value = begin.value + pageInfo.data.length - 1
    total.value = pageInfo.total
  })
  for (let field of services_fields.value) {
    if (field.Field.type == FieldTypeEnum.SelectionField) { // 得到字段对应的selection的值
      selectionDynamic.value[field.Field.name] = await getSelectionValueByServiceAndField(props.service, field.Field.name)
    }
  }
  record.value.splice(0, record.value.length);
  record.value.push(...recordTemp)
}
const handlePageChange = (dir: string) => {
  if (dir == 'right') { // 下一页
    pageNum.value = pageNum.value + 1;
  } else {
    pageNum.value = pageNum.value - 1;
  }
  loadServiceRecord()
}

const selectIds = ref<any[]>([])

const rowSelectChangeEvent = (selectedSum: number, ids: any[]) => {
  selectIds.value.splice(0, selectIds.value.length);
  if (selectedSum) {
    selectIds.value.push(...ids)
  }
  emit('rowSelectChange', selectIds.value)
}
</script>

<template>
  <div class="h-full flex flex-col">
    <el-row>
      <el-col :span="4"></el-col>
      <el-col :span="16">
        <MySearch @conditionChange="conditionChange" :full-width="true" class="w-full"
                  :serviceName="service"/>
      </el-col>
      <el-col :span="4">
        <div class="flex justify-end pr-2 items-center">
          <MyPagination v-model:total="total" v-model:begin="begin" v-model:end="end"
                        @pageChange="handlePageChange"></MyPagination>
        </div>
      </el-col>
    </el-row>
    <el-scrollbar class="py-4 flex-1 ">
      <MyTable height="100%" :record="record" :fields="services_fields" :service-name="service"
               :showDeleteBtn="false" :showSelectBtn="true" @row-select-change="rowSelectChangeEvent">
      </MyTable>
    </el-scrollbar>
  </div>
</template>

<style scoped>

</style>