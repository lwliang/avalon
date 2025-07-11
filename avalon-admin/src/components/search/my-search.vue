<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {ref, watch} from "vue";
import {getActionSearchView} from "../../api/commonApi.ts";
import {parserEx} from "../../xml/XMLParser.ts";
import {XMLParserResult} from "../../xml/XMLParserResult.ts";
import Field from "../../model/Field.ts";
import {useFieldStore} from "../../global/store/fieldStore.ts";
import {useServiceStore} from "../../global/store/serviceStore.ts";
import {FieldTypeEnum} from "../../model/enum-type/FieldTypeEnum.ts";
import {getJoinFirstField, hasJoin} from "../../util/fieldUtils.ts";
import MySearchConditionModel from "./my-search-condition-model.vue";
import MyIcon from "../icon/my-icon.vue";

/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/21 20:21
 */
const props = defineProps<{
  serviceName: string,
  fullWidth?: Boolean,
  arrowShow?: Boolean,
}>()

const serviceStore = useServiceStore()
const fieldDataStore = useFieldStore();
const emit = defineEmits(['conditionChange'])

const searchValue = ref<String>('')
const searchPopperRef = ref()
const selectedIndex = ref<number>(-1)

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

watch(() => searchValue.value, (newSearchValue) => {
  if (newSearchValue) {
    selectedIndex.value = -1; // 重置选中索引
  } else {
    selectedIndex.value = -1;
    emit('conditionChange', '');
  }
})

// 键盘导航处理
const handleKeydown = (event: KeyboardEvent) => {
  if (!searchValue.value || fields.value.length === 0) return;
  
  switch (event.key) {
    case 'ArrowDown':
      event.preventDefault();
      selectedIndex.value = Math.min(selectedIndex.value + 1, fields.value.length - 1);
      break;
    case 'ArrowUp':
      event.preventDefault();
      selectedIndex.value = Math.max(selectedIndex.value - 1, -1);
      break;
    case 'Enter':
      event.preventDefault();
      if (selectedIndex.value >= 0 && selectedIndex.value < fields.value.length) {
        const selectedField = fields.value[selectedIndex.value];
        searchChange(selectedField.name, selectedField.fieldMeta, searchValue.value);
        // 选择后清空输入内容，自动隐藏popover
        searchValue.value = '';
        selectedIndex.value = -1;
      }
      break;
    case 'Escape':
      selectedIndex.value = -1;
      searchValue.value = '';
      break;
  }
};

// 鼠标悬停处理
const handleMouseEnter = (index: number) => {
  selectedIndex.value = index;
};

// 鼠标离开处理
const handleMouseLeave = () => {
  selectedIndex.value = -1;
};

const fields = ref<{ name: string, label: string, fieldMeta: Field }[]>([])
const loadField = async (fieldArr: any[]) => {
  const fieldTemp = await fieldDataStore.getFieldByServiceNameAsync(props.serviceName)
  const service = await serviceStore.getServiceByNameAsync(props.serviceName)
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
    <el-popover 
      ref="searchPopperRef" 
      placement="bottom" 
      trigger="manual"
      :show-arrow="false"
      popper-class="search-popover"
      :visible="!!searchValue && fields.length > 0"
    >
      <template #reference>
        <el-input
          v-model="searchValue"
          placeholder="搜索..."
          size="default"
          class="w-[570px]"
          clearable
          @keydown="handleKeydown"
        >
          <template #prefix>
            <MyIcon type="fas" icon="search" size="sm" />
          </template>
          
          <template #prepend v-if="searchConditionStr">
            <div class="flex items-center bg-gray-100 px-2 rounded">
              <span class="text-sm text-gray-600">{{ searchConditionStr }}</span>
              <MyIcon class="ml-1 cursor-pointer" type="fas" icon="xmark" size="sm" @click="clearSearchConditionClick"/>
            </div>
          </template>
          
          <template #suffix>
            <MyIcon 
              @click.stop="showSearchConditionModel" 
              class="cursor-pointer" 
              type="fas" 
              icon="caret-down"
              size="sm"
            />
          </template>
        </el-input>
      </template>
      
      <template #default>
        <div class="bg-background shadow-sm hover:shadow-lg hover:shadow-primary/20 transition-shadow border border-border border-solid py-2">
          <div>
            <div v-for="(field, index) in fields" :key="index"
                 :class="[
                   'flex items-center px-6 gap-2 py-0.5 cursor-pointer transition-colors',
                   selectedIndex === index ? 'bg-blue-100 text-blue-800' : 'hover:bg-gray-100'
                 ]"
                 @click="() => {
                   searchChange(field.name, field.fieldMeta, searchValue);
                   searchValue = '';
                   selectedIndex = -1;
                 }"
                 @mouseenter="handleMouseEnter(index)"
                 @mouseleave="handleMouseLeave">
              <div>搜索</div>
              <div class="font-bold">{{ field.label }}</div>
              <div>包含</div>
              <div class="text-primary">{{ searchValue }}</div>
            </div>
          </div>
        </div>
      </template>
    </el-popover>


    <my-search-condition-model :service-name="serviceName" v-model="searchConditionShow"
                               @sureClick="searchConditionSure" @close-click="searchConditionClose"/>
  </div>

</template>

<style scoped>
/* 自定义搜索popover样式 */
:global(.search-popover) {
  width: 570px !important;
  height: fit-content;
  padding: 0 !important;
}

:global(.search-popover .el-popover__content) {
  padding: 0 !important;
  width: 100% !important;
}

</style>