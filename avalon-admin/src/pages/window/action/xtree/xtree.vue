<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {
    compile,
    ComponentInternalInstance, computed, createVNode,
    defineComponent,
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
    addModelApi,
    createModelApi,
    deleteModelApi, editModelApi,
    getModelAllApi,
    getModelDetailApi,
    deleteMultiModelApi,
} from "../../../../api/modelApi.ts";
import {getTemplate, XMLParserResult} from "../../../../xml/XMLParserResult.ts";
import {parserEx} from "../../../../xml/XMLParser.ts";
import MyButton from "../../../../components/button/my-button.vue";
import MyButtonGroup from "../../../../components/button-group/my-button-group.vue";
import {useServiceStore} from "../../../../global/store/serviceStore.ts";
import {exportExcel, getActionFormView, getActionTreeView, getActionXTreeView} from "../../../../api/commonApi.ts";
import MySearch from "../../../../components/search/my-search.vue";
import MyDialog from "../../../../components/dialog/my-dialog.vue";
import MyExportDialog from "../../../../components/dialog/my-export-dialog.vue";
import {goModelImport, replaceModelForm} from "../../../../util/routerUtils.ts";
import ShowField from "../../../../model/ShowField.ts";
import {TreeData} from "../../../../components/tree/tree-props.ts";
import Field from "../../../../model/Field.ts";
import {FieldTypeEnum} from "../../../../model/enum-type/FieldTypeEnum.ts";
import {useFieldStore} from "../../../../global/store/fieldStore.ts";
import {getChangeFieldRecordRow, getJoinFirstField, getJoinLastField, getServiceField, hasJoin} from "../../../../util/fieldUtils.ts";
import Form from "../../../../model/form/Form.ts";
import {useTemplateRef} from "@vue/runtime-dom";
import { objectCloneDeep } from "../../../../util/ObjectUtils.ts";
import { Plus } from "@element-plus/icons-vue";


const serviceStore = useServiceStore()

const {proxy} = getCurrentInstance() as ComponentInternalInstance;
const route = useRoute();

const moduleName = ref<string>(route.params.module as string)
const serviceName = ref<string>(route.params.service as string)
const rowClickHandler = inject('rowClick') as (id: number | undefined) => void;


const view = ref<ActionView | undefined>(undefined)
getActionXTreeView(serviceName.value).then(data => { // 加载xml
    if (data && data.length) {
        view.value = data[0]
        renderView();
    }
})

const renderView = async () => {
    if (view.value) {
        await parserXml(view.value.arch)
        await loadTreeData()
    }
}

let parserResult: XMLParserResult | null = null;
const parentField = ref<string>('')
const nameField = ref<string>('')
const childrenField = ref<string>('')

const parserXml = async (str: string) => {
    parserResult = await parserEx(str, serviceName.value)
    parentField.value = parserResult.xtree.parentField
    nameField.value = parserResult.xtree.nameField
    childrenField.value = parserResult.xtree.childrenField
    recordProps.value.label = nameField.value
    recordProps.value.children = childrenField.value
    leftTreeShow.value = true
}

const emit = defineEmits(['rowClick'])
const myTreeRef = useTemplateRef<any>('tree')

const record = ref<any>([])
const leftTreeShow = ref(false)
const recordProps = ref({
    label: '',
    children: '',
    isLeaf: (data:any, node:any):boolean =>  {
        return false
    }
})
const loadRecordData = async (node:any, resolve:any) => {
    if(node.level === 0){ // 根节点
        if(!parentField.value){
           // 等待parentField.value有值后再执行
           const waitForParentField = () => {
               if(parentField.value){
                   loadRootData().then(data => {
                       resolve(data)
                   })
               } else {
                   setTimeout(waitForParentField, 100)
               }
           }
           waitForParentField()
           return
        }
        const data = await loadRootData()
        resolve(data)
    }else{ // 子节点
        const data = await loadChildrenData(node.data)
        resolve(data)
    }
}
const recordField = ref<any[]>([])
const pageNum = ref<number>(1)
const selectionDynamic = ref<any>({})
const condition = ref<string>('')

const loadTreeData = async () => {
    if (!(serviceName.value && moduleName.value)) {
        return;
    }

    let serviceByNameAsync = await serviceStore.getServiceByNameAsync(serviceName.value);
    const data = await getModelAllApi(serviceByNameAsync.keyField + "," + parentField.value + "," + nameField.value,
        `('${parentField.value}',=,null)`,
        serviceName.value)
    data.map((x: any) => {
        x[childrenField.value] = []
    })
    record.value.splice(0, record.value.length);
    record.value.push(...data)

    recordField.value.splice(0, recordField.value.length);
    recordField.value.push(...data)
}

const expandChildHandler = (data: TreeData) => {

}

const createServiceClick = async () => {
    if (recordRowIsChange.value) { // 保存 可能是新增或修改
        saveClick()
    } else { // 重新增加
        if (row_id.value) { // 增加子级
            parentId.value = row_id.value
            row_id.value = null
        }
        for (let key in recordRowWithField.value) {
            if (recordRowWithField.value[key]) {
                recordRowWithField.value[key] = undefined
            }
        }
        const defaultValue = await createModelApi({}, serviceName.value)
        for (let key in defaultValue) {
            if (recordRowWithField.value[key] !== undefined) {
                recordRowWithField.value[key] = defaultValue[key]
            }
        }
    }
}


const deleteServiceClick = () => {
    deleteShow.value = true
}

const addNodeClick = async (node:any, data: any) => {
    parentId.value = data.id
    row_id.value = null
    const defaultValue = await createModelApi({}, serviceName.value)
    for (let key in defaultValue) {
        if (recordRowWithField.value[key] !== undefined) {
            recordRowWithField.value[key] = defaultValue[key]
        }
    }
}

const editNodeClick = (node:any,data: any) => {
    row_id.value = data.id
    loadDetailData(row_id.value).then(data => {
        for (let key in data) {
            recordRowWithField.value[key] = data[key]
        }
    })
}

const deleteNodeClick = (node:any,data: any) => {
    deleteData.value = data
    deleteShow.value = true
    deleteNode.value = node
}

const conditionChange = (search: string) => {
    condition.value = search;
    loadTreeData();
}


const deleteShow = ref(false)
const deleteData = ref<any>()
const deleteNode = ref<any>()

const hideClick = () => {
    deleteShow.value = false
    deleteData.value = undefined
}
const sureClick = async () => {
    if(deleteData.value) { // 删除节点
        deleteModelApi(deleteData.value.id, serviceName.value as string).then(data => {
        proxy?.$notify.success({
            title: "提示",
            message: "删除完成",
        });
        deleteShow.value = false
        deleteData.value = undefined
        const index = deleteNode.value.parent.childNodes.findIndex((x:any) => x.data.id === deleteNode.value.data.id)
        if(index !== -1){
            deleteNode.value.parent.childNodes.splice(index, 1)
        }
        })
    }else{ // 删除选中节点
        const ids = nodeSelects.value;
        deleteMultiModelApi(ids, serviceName.value as string).then(data => {
            proxy?.$notify.success({
                title: "提示",
                message: "删除完成",
            });
            nodeSelects.value = []
        })
    }
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
    const ids = nodeSelects.value;
    if (nodeSelects.value.length) {
        condition = `('${primaryKeyField.keyField}',in,${ids.join(",")})`
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

// 节点改变
const nodeSelects = ref<any>([])
const checkChange = (data: any, checked: boolean) => {
    if(checked){
        nodeSelects.value.push(data.id)
    }else{
        nodeSelects.value = nodeSelects.value.filter((x:any) => x !== data.id)
    }
}



// form 视图编辑
const xmlFormTemplate = ref<any>(null)
const template_component = shallowRef<any>(null)
let template_fields = ref<string[]>([]); // 全部字段 用于查询数据库
let self_service_fields = ref<string[]>([]); // 自身第一级字段
const serviceFieldStore = useFieldStore()
const row_id = ref()
const parentId = ref()
let form = ref<Form>({} as Form)

const loadFormView = async (service: string) => {
    return await getActionFormView(service).then(data => {
        if (data && data.length) {
            return data[0]
        }
        return null
    })

}
loadFormView(serviceName.value).then((dataView: any) => {
    if (dataView) {
        view.value = dataView
        renderFormView(dataView.arch).then(() => {
            loadDataWithLayout()
        })
    }
})
const renderFormView = async (arch: string) => {
    await parserFormXml(arch)
}

const parserFormXml = async (str: string) => {
    const serviceFields = await serviceFieldStore.getFieldByServiceNameAsync(serviceName.value)
    const primaryKeyField = await serviceStore.getServiceByNameAsync(serviceName.value)
    parserResult = await parserEx(str, serviceName.value)
    Object.assign(form.value, parserResult.form)
    xmlFormTemplate.value = getTemplate(parserResult);

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

const loadTreeView = async (service: string) => {
    return await getActionTreeView(service).then(data => {
        if (data && data.length) {
            return data[0]
        }
        return null
    })
}

const loadDetailData = async (id: number) => {
    return getModelDetailApi(id, template_fields.value.join(","),
        serviceName.value);
}
const detailRecordRow = ref<any>({}) // 原始信息
const recordRowWithField = ref<Record<string, any>>({}) // 用户修改信息


const loadDataWithLayout = async () => {
    if (!(serviceName.value && moduleName.value && self_service_fields.value.length)) {
        return;
    }
    const serviceFields = await serviceFieldStore.getFieldByServiceNameAsync(serviceName.value)
    if (row_id.value) {
        const data = await loadDetailData(row_id.value);
        if (data) {
            Object.assign(detailRecordRow.value, data)
            const serviceInstance = await serviceStore.getServiceByNameAsync(serviceName.value);
            for (let key in detailRecordRow.value) {
                const field = serviceFields.find(f => f.name === key) as Field
                if (field.type == FieldTypeEnum.One2manyField) {
                    recordRowWithField.value[key] = detailRecordRow.value[key]
                } else if (field.type == FieldTypeEnum.Many2oneField) {
                    if (serviceInstance.delegateField) {
                        const delegateField = JSON.parse(serviceInstance.delegateField)
                        let delegate = false;
                        for (let s in delegateField) {
                            if (delegateField[s] == field.name) {
                                delegate = true;
                                recordRowWithField.value[key] = {}
                                const delegateServiceFields = await serviceFieldStore.getFieldByServiceNameAsync(s)
                                for (const sKey in detailRecordRow.value[key]) {
                                    const dField = delegateServiceFields.find(f => f.name == sKey)
                                    recordRowWithField.value[key][sKey] = detailRecordRow.value[key][sKey]
                                }
                                break;
                            }
                        }
                        if (!delegate) {
                            recordRowWithField.value[key] = detailRecordRow.value[key]
                        }
                    } else {
                        recordRowWithField.value[key] = detailRecordRow.value[key]
                    }
                } else {
                    recordRowWithField.value[key] = detailRecordRow.value[key]
                }
                await createNewRecordRowDefault(recordRowWithField.value,serviceFields)
                detailRecordRow.value = objectCloneDeep(recordRowWithField.value)
            }
            template_component.value = createFormTemplateVNode();
        }
    } else {
        const defaultValue = await createNewRecordRow()
        await createNewRecordRowDefault(defaultValue,serviceFields)
        await createModelRecordRow(defaultValue)
        detailRecordRow.value = objectCloneDeep(recordRowWithField.value)
        template_component.value = createFormTemplateVNode();
    }
}

const createNewRecordRowDefault = async (defaultValue: any,serviceFields: Field[]) => {
    for (let key of self_service_fields.value) {
        if (hasJoin(key)) {
            const first = getJoinFirstField(key); // 只支持二级字段
            const firstField = await getServiceField(serviceName.value, first);
            const last = getJoinLastField(key);
            const lastField = await getServiceField(serviceName.value, key);
            if (!defaultValue[first]) {
                defaultValue[first] = {}
            }
            if (lastField && (lastField.type == FieldTypeEnum.One2manyField ||
                lastField.type == FieldTypeEnum.Many2manyField)) {
                defaultValue[first][last] = []
            } else if (lastField) {
                defaultValue[first][last] = undefined
            }
        } else {
            let field = serviceFields.find(f => f.name === key)
            if (field && (field.type == FieldTypeEnum.One2manyField ||
                field.type == FieldTypeEnum.Many2manyField)) {
                defaultValue[key] = []
            } else if (field) {
                defaultValue[key] = undefined
            }
        }
    }
}

const createFormTemplateVNode = () => {
    let component = defineComponent({
        setup() {
            const vNode = compile(xmlFormTemplate.value)
            return () => {
                return createVNode(vNode, {...recordRowWithField.value,recordRow:recordRowWithField.value, rules:{}})
            }
        }
    })
    return component;
}

const createNewRecordRow = async () => {
    const defaultRecordRow = await createModelApi({}, serviceName.value)
    return defaultRecordRow
}

const createModelRecordRow = async (defaultValue: any) => {
    recordRowWithField.value = defaultValue
}
const recordRowIsChange = computed(() => { // 字段是否有变量
    return JSON.stringify(recordRowWithField.value) !== JSON.stringify(detailRecordRow.value)
})

const saveClick = async () => {
    if (row_id.value) { // 保存
        update().then(() => {
            detailRecordRow.value = objectCloneDeep(recordRowWithField.value)
        })
    } else {
        await insert()
        detailRecordRow.value = objectCloneDeep(recordRowWithField.value)
    }
}


const insert = async () => {
    const serviceFields = await serviceFieldStore.getFieldByServiceNameAsync(serviceName.value)
    const recordRow = await getChangeFieldRecordRow(recordRowWithField.value, detailRecordRow.value, serviceFields)
    if (parentId.value) {
        if (parentField.value) { // 存在父级字段
            recordRow[parentField.value] = parentId.value
        }
    }


    const data = await addModelApi(recordRow, serviceName.value)
    let serviceByNameAsync = await serviceStore.getServiceByNameAsync(serviceName.value);
    recordRow[serviceByNameAsync.keyField] = data.id
    proxy?.$notify.success({
        title: "新增",
        message: "新增成功",
    });
    return recordRow
}

const update = async () => {
    const serviceFields = await serviceFieldStore.getFieldByServiceNameAsync(serviceName.value)
    const recordRow = await getChangeFieldRecordRow(recordRowWithField.value, detailRecordRow.value, serviceFields)
    
    if (Object.keys(recordRow).length === 0) {
        proxy?.$notify.success({
            title: "修改",
            message: "修改成功",
        });
        return;
    }
    recordRow.id = row_id.value

    await editModelApi(recordRow, serviceName.value).then(data => {
        proxy?.$notify.success({
            title: "修改",
            message: "修改成功",
        });
    })
    return recordRow
}

const loadRootData = async () => {
    let serviceByNameAsync = await serviceStore.getServiceByNameAsync(serviceName.value);

    const record = await getModelAllApi(serviceByNameAsync.keyField + "," + parentField.value + "," + nameField.value,
        `('${parentField.value}',=,null)`,
        serviceName.value)
    return record
}

const loadChildrenData = async (data: any) => {
    if (data) {
        let serviceByNameAsync = await serviceStore.getServiceByNameAsync(serviceName.value);
        const record = await getModelAllApi(serviceByNameAsync.keyField + "," + parentField.value + "," + nameField.value,
            `('${parentField.value}',=,${data.id})`,
            serviceName.value)
        return record
    }
    return []
}
</script>

<template>
    <div class="flex flex-col flex-wrap p-4 items-start h-full">
        <div class="h-[50px] flex items-start w-full">
            <div class="flex-1 flex items-center">
                <my-button-group size="small">
                    <my-button  type="primary" rounded @click="createServiceClick">
                        {{ !recordRowIsChange ? '新增' : '保存' }}
                    </my-button>
                    <my-button type="primary" rounded @click="importExcelClick">导入</my-button>
                    <my-button  v-if="nodeSelects.length" type="success" rounded @click="exportOpen">
                        导出
                    </my-button>
                    <my-button  v-if="nodeSelects.length" type="danger" rounded @click="deleteServiceClick">删除
                    </my-button>
                </my-button-group>
                
            </div>
            <div class="flex-1 px-4">
                <MySearch @conditionChange="conditionChange" :full-width="true" class="w-full"
                          :serviceName="serviceName"></MySearch>
            </div>
            <div class="flex-1 flex justify-end">

            </div>
        </div>
        <div class="flex-1 overflow-y-auto flex flex-col w-full">
            <div class="flex-1 overflow-y-auto flex gap-2">
                <div class="h-full min-w-[500px]">
                    <div class="h-full border border-border border-solid relative rounded overflow-hidden py-2">
                        <el-tree @check-change="checkChange" show-checkbox v-if="leftTreeShow" lazy :props="recordProps" :load="loadRecordData" >
                            <template #default="{ node, data }">
                                <div class="flex items-center w-full">
                                    <div class="flex-1">{{ data[nameField] }}</div>
                                    <div class="flex-1"></div>
                                    <div class="flex items-center justify-center gap-1 px-2">
                                        <el-icon @click.stop="addNodeClick(node,data)" ><Plus /></el-icon>
                                        <el-icon @click.stop="editNodeClick(node,data)" ><Edit /></el-icon>
                                        <el-icon @click.stop="deleteNodeClick(node,data)" ><Delete/></el-icon>
                                    </div>
                                </div>
                            </template>
                        </el-tree>
                    </div>
                </div>
                <div class="flex-1 border border-border border-solid rounded overflow-hidden">
                    <el-scrollbar class="h-full">
                        <component :is="template_component"/>
                    </el-scrollbar>
                </div>
            </div>
        </div>
    </div>
    <MyDialog :draggable="true" width="300px" v-model="deleteShow" @close="hideClick" @sure="sureClick" title="提示">
        确认删除吗?
    </MyDialog>
    <MyExportDialog v-model="exportShow" :service="serviceName" @close="exportClose" @sure="exportSure"></MyExportDialog>
</template>

<style scoped>

</style>