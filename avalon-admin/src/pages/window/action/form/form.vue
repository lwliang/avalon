/**
* @author lwlianghehe@gmail.com
* @date 2024/11/22
*/

<script setup lang="ts">
import {
    compile,
    ComponentInternalInstance,
    computed,
    createVNode,
    defineComponent,
    getCurrentInstance, provide,
    ref,
    shallowRef
} from "vue";
import {useRoute} from "vue-router";
import ActionView from "../../../../model/view/ActionView.ts";
import {addModelApi, createModelApi, editModelApi, getModelDetailApi, invokeMethod} from "../../../../api/modelApi.ts";
import {getTemplate, XMLParserResult} from "../../../../xml/XMLParserResult.ts";
import {parserEx} from "../../../../xml/XMLParser.ts";
import {useGlobalFieldDataStore} from "../../../../global/store/fieldStore.ts";
import {useGlobalServiceDataStore} from "../../../../global/store/serviceStore.ts";
import FormField from "../../../../model/FormField.ts";
import MyButton from "../../../../components/button/my-button.vue";
import {FieldTypeEnum} from "../../../../model/enum-type/FieldTypeEnum.ts";
import Field from "../../../../model/Field.ts";
import {goModelWindow, replaceModelForm} from "../../../../util/routerUtils.ts";
import {getActionFormView, getActionTreeView} from "../../../../api/commonApi.ts";
import MyServiceLog from "../../../../components/service-log/my-service-log.vue";
import {getJoinFirstField, getJoinLastField, getServiceField, hasJoin} from "../../../../util/fieldUtils.ts";
import {getModuleIcon} from "../../../../api/moduleApi.ts";
import {refreshPage} from "../../../../util/commonUtils.ts";
import Service from "../../../../model/Service.ts";
import Form from "../../../../model/form/Form.ts";
import ServiceInvokeParam from "../../../../model/ServiceInvokeParam.ts";

const {proxy} = getCurrentInstance() as ComponentInternalInstance;
const route = useRoute();
const serviceFieldStore = useGlobalFieldDataStore()
const serviceStore = useGlobalServiceDataStore()

const moduleName = ref<string>(route.params.module as string)
const row_id = ref<number | undefined>(parseInt(route.query.id as string))
const serviceName = ref<string>(route.params.service as string)
const form_container = ref()

const servicePropInstance = ref<Service>()
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
const recordRowWithField = ref<Record<string, FormField | any>>({})
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
            const serviceInstance = await serviceStore.getServiceByNameAsync(serviceName.value);
            for (let key in recordRow.value) {
                const field = serviceFields.find(f => f.name === key) as Field
                if (field.type == FieldTypeEnum.One2manyField) {
                    recordRowWithField.value[key] = new FormField(recordRow.value[key], field)
                } else if (field.type == FieldTypeEnum.Many2oneField) {
                    if (serviceInstance.delegateField) {
                        const delegateField = JSON.parse(serviceInstance.delegateField)
                        let delegate = false;
                        for (let s in delegateField) {
                            if (delegateField[s] == field.name) {
                                delegate = true;
                                recordRowWithField.value[key] = {}
                                const delegateServiceFields = await serviceFieldStore.getFieldByServiceNameAsync(s)
                                for (const sKey in recordRow.value[key]) {
                                    const dField = delegateServiceFields.find(f => f.name == sKey)
                                    recordRowWithField.value[key][sKey] = new FormField(recordRow.value[key][sKey], dField)
                                }
                                break;
                            }
                        }
                        if (!delegate) {
                            recordRowWithField.value[key] = new FormField(recordRow.value[key], field)
                        }
                    } else {
                        recordRowWithField.value[key] = new FormField(recordRow.value[key], field)
                    }
                } else {
                    recordRowWithField.value[key] = new FormField(recordRow.value[key], field)
                }

            }
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
            recordRowWithField.value[key] = new FormField(defaultValue[key], field)
        }
    }
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


const createFormTemplateVNode = () => {
    let component = defineComponent({
        setup() {
            const vNode = compile(xmlTemplate.value)
            return () => {
                return createVNode(vNode, {...recordRowWithField.value})
            }
        }
    })
    return component;
}

const createHeaderTemplateVNode = () => {
    if (!headerTemplate.value) return
    const vNode = compile(headerTemplate.value)
    const btnClickHandler = async (actionType: string, action: string) => {
        console.log('btnClick', actionType, action)

        let param = null;
        if (row_id.value) {
            param = {
                serviceName: serviceName.value,
                method: action,
                param: {"id": row_id.value}
            }
        } else {
            param = {
                serviceName: serviceName.value,
                method: action,
                param: {}
            }
        }
        if (serviceName.value) {
            const result = await invokeMethod(serviceName.value, param);
            if (!result) { // 没有返回值
                proxy?.$notify.success("提示", "操作成功");
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
        return createVNode(vNode, {...recordRowWithField.value, btnClickHandler})
    }
}
// 新增，初始化对象
const createClick = async () => {
    row_id.value = undefined;

    for (let fieldKey in recordRowWithField.value) {
        const field = recordRowWithField.value[fieldKey].Field;
        if (field && (field.type == FieldTypeEnum.One2manyField ||
            field.type == FieldTypeEnum.Many2manyField)) {
            recordRowWithField.value[fieldKey].reset([])
        } else if (field) {
            recordRowWithField.value[fieldKey].reset(null)
        }
    }
}

const backClick = () => {
    goModelWindow(moduleName.value, serviceName.value, {})
}

const saveClick = async () => {
    if (row_id.value) { // 保存
        update().then(() => {
            loadDetailData(row_id.value as number).then(data => {
                Object.assign(recordRow.value, data)
                for (let key in recordRow.value) {
                    recordRowWithField.value[key].reset(recordRow.value[key])
                }
            })
        })
    } else {
        insert().then((data: any) => {
            replaceModelForm(moduleName.value, serviceName.value, data.id)
        })
    }
}

const insert = async () => {
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

    return await addModelApi(recordRow, serviceName.value).then(data => {
        proxy?.$notify.success("新增", "新增成功");
        row_id.value = data.id
        return data
    })
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

    return editModelApi(recordRow, serviceName.value).then(data => {
        proxy?.$notify.success("修改", "修改成功");
    })
}

const scrollValue = ref<{ scrollTop: number, scrollLeft: number }>({scrollLeft: 0, scrollTop: 0})
provide('formScrollEvent', scrollValue)
const onScrollClick = (e: any) => {
    console.log("onScrollClick", e.target.scrollTop)
    Object.assign(scrollValue.value, {scrollTop: e.target.scrollTop, scrollLeft: e.target.scrollLeft})
}
</script>

<template>
    <div class="p-4 w-full overflow-hidden h-full box-border">
        <div>
            <MyButton class="mr-2" type="success" is-link rounded @click="backClick" icon="chevron-left"
                      icon-style="fas">返回
            </MyButton>
            <MyButton type="primary" rounded @click="createClick" v-if="form.create">新增</MyButton>
            <MyButton type="success" rounded @click="saveClick" class="ml-2"
                      v-if="recordRowIsChange && (form.edit || form.create)">
                保存
            </MyButton>
            <component :is="header_component"/>
        </div>

        <div class="w-full flex h-full box-border">
            <div class="w-full overflow-x-auto" style="flex: 2" @scroll="onScrollClick">
                <component :is="template_component"/>
            </div>
            <div class="flex-1 h-full box-border hidden 2xl:block">
                <div class="w-full overflow-auto h-full">
                    <MyServiceLog :service="serviceName" :service-id="row_id"></MyServiceLog>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>

</style>