<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import './my-dialog.css'
import MyIcon from "../icon/my-icon.vue";
import MyButton from "../button/my-button.vue";
import {ComponentInternalInstance, computed, getCurrentInstance, ref, watch} from "vue";
import Field from "../../model/Field.ts";
import {getServiceFieldApi} from "../../api/modelApi.ts";
import MyFieldTree from "../tree/my-field-tree.vue";
import {useDebounceFn} from "@vueuse/core";
import MyDialog from "./my-dialog.vue";
import {ElNotification} from "element-plus";
import { FieldTypeEnum } from '../../model/enum-type/FieldTypeEnum.ts';

const {proxy} = getCurrentInstance() as ComponentInternalInstance;

const props = defineProps({
  zIndex: {
    type: Number,
    default: 2000
  },
  title: {
    type: String,
    default: "导出数据"
  },
  show: {
    type: Boolean,
    default: false
  },
  service: {
    type: String
  }
})

const emit = defineEmits(['close', 'sure'])

const leftSearch = ref('')
const rightSearch = ref('')


const closeClick = () => {
  emit('close')
}

const sureClick = () => {
  if (!selectFields.value.length) {
    ElNotification({
      title: "提示",
      message: "请选择要导出的字段",
      type: "error",
    })
    return
  }
  const fieldStr = selectFields.value.map(x => x.name).join(",")
  emit('sure', fieldStr)
}
const fields = ref<Field[]>([])
const fieldProps = {
  label: 'label',
  children: 'children',
  isLeaf: (data:any, node:any):boolean =>  {
    return data.type !== FieldTypeEnum.One2manyField &&
    data.type !== FieldTypeEnum.One2oneField &&
    data.type !== FieldTypeEnum.Many2manyField &&
    data.type !== FieldTypeEnum.Many2oneField
  }  ,
}
const loadField = (node:any, resolve:any, reject:any):void => {
  if (!(node.level === 0)) { // 根节点
    if(node.data.relativeServiceName){ // 如果节点有相对服务名，则加载相对服务名
      getServiceFieldApi(node.data.relativeServiceName).then(data => {
      node.data.children = data
      resolve(data)
    })
    } 
  }else {
      getServiceFieldApi(props.service as string).then(data => {
        node.data.children = data
        resolve(data)
      })
    }
}



const selectFields = ref<{ label: string, name: string, id: string }[]>([])
const selectFieldProps = {
  label: 'label',
  children: 'children',
  isLeaf: ():boolean =>  {
    return false
  }  ,
}
const selectTreeRef = ref<any>(null)
const filterSelectNode = (value: string, data: any) => {
  if (!value) return true
  return data.label.includes(value)
}
const pushSelectField = (selectField: { label: string, name: string, id: string }) => {
  selectFields.value.push(selectField);
}

const leftFieldSelect = (node:any, data:any) => {
  if(node.level === 1){
    pushSelectField({
      label: data.label,
      name: data.name,
      id: data.id
    })
  } else {
    leftFieldSelect(node.parent, {
      label:  node.parent.data.label + "/" + data.label,
      name: node.parent.data.name + "." + data.name,
      id: node.parent.data.id
    })
  }
}

const filterNode = (value: string, data: any) => {
  if (!value) return true
  return data.label.includes(value)
}
const treeRef = ref<any>(null)
watch(()=>leftSearch.value, (val) => {
  treeRef.value.filter(val)
})
watch(()=>rightSearch.value, (val) => {
  selectTreeRef.value.filter(val)
})

const deleteSelectField = (field: { label: string, name: string, id: string }) => {
  let index = selectFields.value.findIndex(x => x.id == field.id);
  if (index >= 0) {
    selectFields.value.splice(index, 1)
  }
}


</script>

<template>
  <my-dialog :close-on-click-modal="false" :title="title" :show="show" @sure="sureClick" @close="closeClick" dialog-style="width:978px;height:694px">
    <div class="flex h-[600px]">
      <div class="flex-1 flex flex-col h-full overflow-hidden">
        <div class="pb-2 flex-shrink-0">可用字段</div>
        <MyInput class="flex-shrink-0" v-model="leftSearch" placeholder="搜索"></MyInput>
        <div class="h-4 flex-shrink-0"></div>
        <div class="flex-1 overflow-y-auto border border-border border-solid relative rounded">
          <el-scrollbar class="flex-1">
            <el-tree ref="treeRef" lazy :props="fieldProps" :load="loadField" :filter-node-method="filterNode">
              <template #default="{ node, data }">
                <div class="flex items-center w-full h-full pr-4">
                  <span>{{ node.label }}</span>
                  <div class="flex-1"></div>
                  <el-icon @click.stop="leftFieldSelect(node, data)"><Plus /></el-icon>
                </div>
              </template>
            </el-tree>
            <!-- <MyFieldTree node-class="px-1" :nodes="fields" @fieldSelect="leftFieldSelect"
                         :condition="leftSearch"/> -->
          </el-scrollbar>
        </div>
      </div>
      <div class="w-4">

      </div>
      <div class="flex-1 flex flex-col h-full ">
        <div class="pb-2">要导出的字段</div>
        <MyInput v-model="rightSearch" placeholder="搜索"></MyInput>
        <div class="h-4 flex-shrink-0"></div>
   
        <div class="flex-1 h-full">
          <div class="flex-1 h-full border border-border border-solid relative rounded overflow-hidden">
            <el-tree ref="selectTreeRef" draggable node-key="id" :data="selectFields" :props="selectFieldProps" 
             :filter-node-method="filterSelectNode">
              <template #default="{ node, data }">
                <div class="flex items-center w-full h-full pr-4">
                  <div class=" cursor-pointer pr-2">
                    <MyIcon icon="sort" type="fas"></MyIcon>
                  </div>
                  <div class="flex-1 select-none">{{ data.label }}</div>
                  <div class=" cursor-pointer" @click="deleteSelectField(data)">
                    <MyIcon icon="trash-can" type="fas"></MyIcon>
                  </div>
                </div>
              </template>
            </el-tree>
          </div>
        </div>
      </div>
    </div>
    <template #footer>
      <MyButton type="info" rounded @click="closeClick">取消</MyButton>
      <MyButton class="ml-3" type="primary" rounded @click="sureClick">导出</MyButton>
    </template>
  </my-dialog>
</template>

<style scoped>

</style>