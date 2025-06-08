<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {ref, watch} from "vue";
import {getActionSearchView} from "../../api/commonApi.ts";
import {parserEx} from "../../xml/XMLParser.ts";
import {XMLParserResult} from "../../xml/XMLParserResult.ts";
import {arrow, flip, offset, shift, useFloating} from '@floating-ui/vue';
import Field from "../../model/Field.ts";
import {useGlobalFieldDataStore} from "../../global/store/fieldStore.ts";
import {useGlobalServiceDataStore} from "../../global/store/serviceStore.ts";
import {FieldTypeEnum} from "../../model/enum-type/FieldTypeEnum.ts";
import {getJoinFirstField, hasJoin} from "../../util/fieldUtils.ts";
import MySearchConditionModel from "./my-search-condition-model.vue";

/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/21 20:21
 */
const props = defineProps<{
  serviceName: string,
  fullWidth?: Boolean,
  arrowShow?: Boolean,
}>()

const serviceStore = useGlobalServiceDataStore()

const fieldDataStore = useGlobalFieldDataStore();
const serviceDataStore = useGlobalServiceDataStore();
const emit = defineEmits(['conditionChange'])

const reference = ref<any>(null);
const floating = ref(null);
const floatingArrow = ref(null);
const {floatingStyles, middlewareData, placement} = useFloating(reference, floating, {
  placement: 'bottom',
  middleware: [offset(8), flip(), shift(), arrow({element: floatingArrow})],
});

const searchValue = ref<String>('')

const arch = ref<string>('')
let parserResult = ref<XMLParserResult | null>();
const loadSearchView = (service: string) => {
  getActionSearchView(service).then(data => {
    if (data.length) {
      parserEx(data[0].arch, service).then(result => {
        parserResult.value = result
        loadField(parserResult.value.fields)
      })
    } else {
      loadField([])
    }
  })
}
if (props.serviceName) {
  loadSearchView(props.serviceName)
}

const searchPopperRef = ref()
const showPopper = () => {
  if (searchPopperRef.value) {
    searchPopperRef.value.show()
  }
}
const hidePopper = () => {
  if (searchPopperRef.value) {
    searchPopperRef.value.hide()
  }
}

watch(() => searchValue.value, (newSearchValue) => {
  if (newSearchValue) {
    showPopper();
  } else {
    hidePopper();
    emit('conditionChange', '');
  }
})

const fields = ref<{ name: string, label: string, fieldMeta: Field }[]>([])
const loadField = async (fieldArr: any[]) => {
  const fieldTemp = await fieldDataStore.getFieldByServiceNameAsync(props.serviceName)
  const service = await serviceDataStore.getServiceByNameAsync(props.serviceName)
  fields.value.splice(0, fields.value.length);
  const nameField = fieldArr.find((x) => x.name == service.nameField)
  if (!nameField) {
    fieldArr.push({name: service.nameField})
  }
  for (let field of fieldArr) {
    let fieldName = field.name
    if (hasJoin(field.name)) {
      fieldName = getJoinFirstField(field.name)
    }
    const x = fieldTemp.find(x => x.name == fieldName);
    if (x) {
      fields.value.push({
        name: field.name,
        label: x.label,
        fieldMeta: x
      })
    }
  }
}
const searchChange = async (originName: string, field: Field, value: String) => {
  let searchCondition = ''
  let fieldName = originName;

  if (field.type === FieldTypeEnum.Many2oneField) { // 仅支持 string many2one Many2manyField字段
    const primaryKeyField = await serviceStore.getServiceByNameAsync(field.relativeServiceName)
    fieldName += `.${primaryKeyField.nameField}`
  } else if (field.type === FieldTypeEnum.Many2manyField) {
    const primaryKeyField = await serviceStore.getServiceByNameAsync(field.relativeServiceName)
    fieldName += `.${primaryKeyField.nameField}`
  }
  let conditionOp = 'like'
  if (field.type == FieldTypeEnum.BigDecimalField ||
      field.type == FieldTypeEnum.BigIntegerField ||
      field.type == FieldTypeEnum.DoubleField ||
      field.type == FieldTypeEnum.FloatField ||
      field.type == FieldTypeEnum.IntegerField) {
    conditionOp = '='
  }
  searchCondition = `('${fieldName}',${conditionOp},'${value}')`
  if (searchCondition) {
    emit('conditionChange', searchCondition);
  }
  hidePopper();
}

const searchConditionShow = ref(false)
const showSearchConditionModel = () => {
  searchConditionShow.value = true
}
const searchConditionStr = ref('')
const searchConditionSure = (conditionStr: string) => {
  searchConditionShow.value = false
  if (conditionStr) { // 不是字符串
    searchConditionStr.value = conditionStr
    emit('conditionChange', conditionStr);
  } else {
    searchConditionStr.value = ''
  }
}
const searchConditionClose = () => {
  searchConditionShow.value = false
}
const clearSearchConditionClick = () => {
  searchConditionStr.value = ''
  emit('conditionChange', '');
}
</script>

<template>
  <div class="relative h-[34px] w-full">
    <my-popover ref="searchPopperRef" placement="bottom" trigger="click" :arrow-show="false"
                popper-class="w-[570px] h-fit">
      <div ref="reference" class="w-[570px]">
        <div class="flex border border-solid border-border items-center gap-2 px-3 h-[34px]  rounded overflow-hidden">
          <MyIcon type="fas" icon="search" size="sm"></MyIcon>
          <div class="flex bg-background-component rounded-lg px-1" v-if="searchConditionStr">
            <div class="px-1">{{ searchConditionStr }}</div>
            <div class="cursor-pointer" @click="clearSearchConditionClick">x</div>
          </div>
          <input placeholder="搜索..." class="text inline-block flex-1 w-full" type="text"
                 v-model="searchValue">
          <MyIcon @click.stop="showSearchConditionModel" class="cursor-pointer" type="fas" icon="caret-down"
                  size="sm"></MyIcon>
        </div>
      </div>
      <template #content>
        <div :class="['bg-background shadow-sm hover:shadow-lg hover:shadow-primary/20 transition-shadow border border-border border-solid py-2',
         {'z-9999':true,popover:true,'w-full':fullWidth,'popover-p':!fullWidth,'popover-full-p':fullWidth}]">
          <div class="">
            <div v-for="(field, index) in fields" :key="index"
                 class="flex items-center px-6 gap-2 py-0.5 cursor-pointer hover:bg-gray-100"
                 @click="searchChange(field.name,field.fieldMeta,searchValue)">
              <div>搜索</div>
              <div class="font-bold">{{ field.label }}</div>
              <div>包含</div>
              <div class="text-primary">{{ searchValue }}</div>
            </div>
          </div>

        </div>
      </template>
    </my-popover>


    <my-search-condition-model :service-name="serviceName" :show="searchConditionShow"
                               @sureClick="searchConditionSure" @close-click="searchConditionClose"/>
  </div>

</template>

<style scoped>

</style>