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
    deleteMultiModelApi, editModelApi,
    getModelAllApi,
    getModelDetailApi,
    getModelPageApi
} from "../../../../api/modelApi.ts";
import {getTemplate, XMLParserResult} from "../../../../xml/XMLParserResult.ts";
import {parserEx} from "../../../../xml/XMLParser.ts";
import MyButton from "../../../../components/button/my-button.vue";
import {useGlobalServiceDataStore} from "../../../../global/store/serviceStore.ts";
import {exportExcel, getActionFormView, getActionTreeView, getActionXTreeView} from "../../../../api/commonApi.ts";
import FormField from "../../../../model/FormField.ts";
import MySearch from "../../../../components/search/my-search.vue";
import MyDialog from "../../../../components/dialog/my-dialog.vue";
import MyExportDialog from "../../../../components/dialog/my-export-dialog.vue";
import {goModelImport, replaceModelForm} from "../../../../util/routerUtils.ts";
import ShowField from "../../../../model/ShowField.ts";
import {TreeData} from "../../../../components/tree/tree-props.ts";
import Field from "../../../../model/Field.ts";
import {FieldTypeEnum} from "../../../../model/enum-type/FieldTypeEnum.ts";
import {useGlobalFieldDataStore} from "../../../../global/store/fieldStore.ts";
import {getJoinFirstField, getJoinLastField, getServiceField, hasJoin} from "../../../../util/fieldUtils.ts";
import Form from "../../../../model/form/Form.ts";
import {useTemplateRef} from "@vue/runtime-dom";


const serviceStore = useGlobalServiceDataStore()

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
}

const emit = defineEmits(['rowClick'])
const myTreeRef = useTemplateRef<any>('tree')

const record = ref<any>([])
const recordField = ref(new FormField([]))
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

    recordField.value.value.splice(0, recordField.value.value.length);
    recordField.value.value.push(...data)
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
                recordRowWithField.value[key].reset(undefined)
            }
        }
        const defaultValue = await createModelApi({}, serviceName.value)
        for (let key in defaultValue) {
            if (recordRowWithField.value[key]) {
                recordRowWithField.value[key].reset(defaultValue[key])
            }
        }
    }
}


const deleteServiceClick = () => {
    deleteShow.value = true
}


const conditionChange = (search: string) => {
    condition.value = search;
    loadTreeData();
}


const deleteShow = ref(false)

const hideClick = () => {
    deleteShow.value = false
    nodeSelects.value.splice(0, nodeSelects.value.length)
}
const sureClick = async () => {
    let serviceByNameAsync = await serviceStore.getServiceByNameAsync(serviceName.value);
    const ids = nodeSelects.value.map(x => x[serviceByNameAsync.keyField])
    deleteMultiModelApi(ids, serviceName.value as string).then(data => {
        nodeSelects.value.splice(0, nodeSelects.value.length)
        if (myTreeRef.value) { // 清除所有的选择
            myTreeRef.value.clearCheckedNodes()
        }

        proxy?.$notify.success('提示', '删除完成');
        deleteShow.value = false
        loadTreeData()
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
    const ids = nodeSelects.value.map(x => x[primaryKeyField.keyField])
    if (nodeSelects.value.length) {
        condition = `('${primaryKeyField.keyField}',in,${ids.join(",")})`
    }
    exportExcel(serviceName.value, fields, condition, "").then(data => {
        proxy?.$notify.success("提示", "导出成功");
    })
}

const importExcelClick = () => {
    goModelImport(moduleName.value, serviceName.value, {})
}

// 节点改变
const nodeClickSelected = ref<TreeData>({} as TreeData)
const nodeClickValue = async (data: TreeData) => {
    const primaryKeyField = await serviceStore.getServiceByNameAsync(serviceName.value)
    row_id.value = data[primaryKeyField.keyField]
    nodeClickSelected.value = data
    const detailRecord = await loadDetailData(row_id.value)
    for (let key in detailRecord) {
        if (recordRowWithField.value[key]) {
            recordRowWithField.value[key].reset(detailRecord[key])
        }
    }
}
const onNodeMove = async (move: { from: TreeData, to: TreeData }) => {
    let value: any = {}
    const primaryKeyField = await serviceStore.getServiceByNameAsync(serviceName.value)
    value[primaryKeyField.keyField] = move.from[primaryKeyField.keyField]

    if (move.to) { // 成为子级
        value[parentField.value] = move.to[primaryKeyField.keyField]
    } else { //成为父级
        value[parentField.value] = null
    }

    await editModelApi(value, serviceName.value)
    console.log("onNodeMove", move)
}

const nodeSelects = ref<TreeData[]>([])
const nodeSelectChange = async (data: TreeData[]) => {
    if (myTreeRef.value) {
        const nodes = myTreeRef.value.getCheckedNodes()
        nodeSelects.value.splice(0, nodeSelects.value.length)
        nodeSelects.value.push(...nodes)
    }
}


// form 视图编辑
const xmlFormTemplate = ref<any>(null)
const template_component = shallowRef<any>(null)
let template_fields = ref<string[]>([]); // 全部字段 用于查询数据库
let self_service_fields = ref<string[]>([]); // 自身第一级字段
const serviceFieldStore = useGlobalFieldDataStore()
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
const detailRecordRow = ref<any>({})
const recordRowWithField = ref<Record<string, FormField | any>>({})


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
                    recordRowWithField.value[key] = new FormField(detailRecordRow.value[key], field)
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
                                    recordRowWithField.value[key][sKey] = new FormField(detailRecordRow.value[key][sKey], dField)
                                }
                                break;
                            }
                        }
                        if (!delegate) {
                            recordRowWithField.value[key] = new FormField(detailRecordRow.value[key], field)
                        }
                    } else {
                        recordRowWithField.value[key] = new FormField(detailRecordRow.value[key], field)
                    }
                } else {
                    recordRowWithField.value[key] = new FormField(detailRecordRow.value[key], field)
                }

            }
            template_component.value = createFormTemplateVNode();
        }
    } else {
        await createNewRecordRow(serviceFields);
        const defaultValue = await createModelApi({}, serviceName.value)
        await createModelRecordRow(defaultValue, serviceFields)
        template_component.value = createFormTemplateVNode();
    }
}

const createFormTemplateVNode = () => {
    let component = defineComponent({
        setup() {
            const vNode = compile(xmlFormTemplate.value)
            return () => {
                return createVNode(vNode, {...recordRowWithField.value})
            }
        }
    })
    return component;
}

const createNewRecordRow = async (serviceFields: Field[]) => {
    for (let key of self_service_fields.value) {
        if (hasJoin(key)) {
            const first = getJoinFirstField(key); // 只支持二级字段
            const firstField = await getServiceField(serviceName.value, first);
            const last = getJoinLastField(key);
            const lastField = await getServiceField(serviceName.value, key);
            if (!recordRowWithField.value[first]) {
                recordRowWithField.value[first] = {}
            }
            if (lastField && (lastField.type == FieldTypeEnum.One2manyField ||
                lastField.type == FieldTypeEnum.Many2manyField)) {
                recordRowWithField.value[first][last] = new FormField([], lastField)
            } else if (lastField) {
                recordRowWithField.value[first][last] = new FormField(undefined, lastField)
            }
        } else {
            let field = serviceFields.find(f => f.name === key)
            if (field && (field.type == FieldTypeEnum.One2manyField ||
                field.type == FieldTypeEnum.Many2manyField)) {
                recordRowWithField.value[key] = new FormField([], field)
            } else if (field) {
                recordRowWithField.value[key] = new FormField(undefined, field)
            }
        }
    }
}

const createModelRecordRow = async (defaultValue: any, serviceFields: Field[]) => {
    for (let key in defaultValue) {
        let field = serviceFields.find(f => f.name === key)
        if (field) {
            recordRowWithField.value[key] = new FormField(defaultValue[key], field)
        }
    }
}
const recordRowIsChange = computed(() => { // 字段是否有变量
    for (let key in recordRowWithField.value) {
        if (recordRowWithField.value[key] instanceof FormField) {
            if (recordRowWithField.value[key].isChanged()) {
                return true
            }
        } else { // 对象
            for (const value in recordRowWithField.value[key]) {
                if (recordRowWithField.value[key][value].isChanged()) {
                    return true
                }
            }
        }
    }
    return false
})

const saveClick = async () => {
    if (row_id.value) { // 保存
        update().then(() => {
            loadDetailData(row_id.value as number).then(data => {
                for (let key in recordRowWithField.value) {
                    recordRowWithField.value[key].reset(recordRowWithField.value[key].value)
                }
                Object.assign(nodeClickSelected.value, data)
            })
        })
    } else {
        const recordKey = await insert()
        recordKey[childrenField.value] = []
        if (parentId.value) {
            if (nodeClickSelected.value) {
                nodeClickSelected.value[childrenField.value].push(recordKey)
            }
        } else {
            recordField.value.value.push(recordKey)
        }

        let serviceByNameAsync = await serviceStore.getServiceByNameAsync(serviceName.value);
        detailRecordRow.value[serviceByNameAsync.keyField] = recordKey.id
        for (let key in recordRowWithField.value) {
            recordRowWithField.value[key].reset(recordRowWithField.value[key].value)
        }
    }
}


const insert = async () => {
    const recordRow = {} as any;
    if (parentId.value) {
        if (parentField.value) { // 存在父级字段
            recordRow[parentField.value] = parentId.value
        }
    }
    for (let fieldKey in recordRowWithField.value) {
        if (recordRowWithField.value[fieldKey] instanceof FormField) {
            if (recordRowWithField.value[fieldKey].isChanged()) {
                recordRow[fieldKey] = await recordRowWithField.value[fieldKey].getRawValue()
            }
        } else {
            if (!recordRow[fieldKey]) {
                recordRow[fieldKey] = {}
            }
            for (let x in recordRowWithField.value[fieldKey]) {
                recordRow[fieldKey][x] = await recordRowWithField.value[fieldKey][x].getRawValue()
            }
        }
    }

    const data = await addModelApi(recordRow, serviceName.value)
    let serviceByNameAsync = await serviceStore.getServiceByNameAsync(serviceName.value);
    recordRow[serviceByNameAsync.keyField] = data.id
    proxy?.$notify.success("新增", "新增成功");
    return recordRow
}

const update = async () => {
    const recordRow = {} as any;
    for (let fieldKey in recordRowWithField.value) {
        if (recordRowWithField.value[fieldKey] instanceof FormField) {
            if (recordRowWithField.value[fieldKey].isChanged()) {
                recordRow[fieldKey] = await recordRowWithField.value[fieldKey].getRawValue()
            }
        } else {
            if (!recordRow[fieldKey]) {
                recordRow[fieldKey] = {}
            }
            for (let x in recordRowWithField.value[fieldKey]) {
                recordRow[fieldKey][x] = await recordRowWithField.value[fieldKey][x].getRawValue()
            }
        }
    }
    if (Object.keys(recordRow).length === 0) {
        proxy?.$notify.success("修改", "修改成功");
        return;
    }
    recordRow["id"] = row_id.value

    await editModelApi(recordRow, serviceName.value).then(data => {
        proxy?.$notify.success("修改", "修改成功");
    })
    return recordRow
}

const loadChildrenData = async (data: TreeData) => {
    if (data) {
        let serviceByNameAsync = await serviceStore.getServiceByNameAsync(serviceName.value);
        const record = await getModelAllApi(serviceByNameAsync.keyField + "," + parentField.value + "," + nameField.value,
            `('${parentField.value}',=,${data.id})`,
            serviceName.value)
        record.map((x: any) => {
            if (childrenField.value)
                x[childrenField.value] = []
        })
        return record
    }
    return null
}
</script>

<template>
    <div class="flex flex-col flex-wrap p-4 items-start h-full">
        <div class="pb-4 flex items-start w-full">
            <div class="flex-1">
                <my-button class="mr-0.5" type="primary" rounded @click="createServiceClick">
                    {{ !recordRowIsChange ? '新增' : '保存' }}
                </my-button>
                <my-button class="mr-0.5" type="primary" rounded @click="importExcelClick">导入</my-button>
                <my-button class="mr-0.5" v-if="nodeSelects.length" type="success" rounded @click="exportOpen">
                    导出
                </my-button>
                <my-button class="mr-0.5" v-if="nodeSelects.length" type="danger" rounded @click="deleteServiceClick">删除
                </my-button>
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
                    <my-tree ref="tree" v-model="recordField" :parentField="parentField"
                             :children-field="childrenField"
                             :nameField="nameField"
                             :showSelect="true"
                             @nodeMove="onNodeMove"
                             :border="true"
                             :lazy="true"
                             :load="loadChildrenData"
                             @expandChild="expandChildHandler"
                             @node-select="nodeSelectChange"
                             @nodeClick="nodeClickValue"/>
                </div>
                <div class="flex-1 border">
                    <component :is="template_component"/>
                </div>
            </div>
        </div>
    </div>
    <MyDialog :show="deleteShow" @close="hideClick" @sure="sureClick" title="提示">
        确认删除吗?
    </MyDialog>
    <MyExportDialog :show="exportShow" :service="serviceName" @close="exportClose" @sure="exportSure"></MyExportDialog>
</template>

<style scoped>

</style>