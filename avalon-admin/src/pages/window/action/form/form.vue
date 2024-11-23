/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

<script setup lang="ts">
import {
    ComponentInternalInstance,
    getCurrentInstance,
    ref,
    watch,
    inject,
    compile,
    createVNode,
    defineComponent, shallowRef
} from "vue";
import {LocationQueryValue, useRoute} from "vue-router";
import ActionView from "../../../../model/view/ActionView.ts";
import {
    addModelApi,
    editModelApi,
    getModelAllApi,
    getModelDetailApi,
    getModelPageApi
} from "../../../../api/modelApi.ts";
import {getTemplate, XMLParserResult} from "../../../../xml/XMLParserResult.ts";
import {parserEx} from "../../../../xml/XMLParser.ts";
import {useGlobalFieldDataStore} from "../../../../global/store/fieldStore.ts";
import {useGlobalServiceDataStore} from "../../../../global/store/serviceStore.ts";
import FormField from "../../../../model/FormField.ts";
import MyButton from "../../../../components/button/my-button.vue";
import {FieldTypeEnum} from "../../../../model/enum-type/FieldTypeEnum.ts";
import Field from "../../../../model/Field.ts";
import {goModelForm} from "../../../../util/routerUtils.ts";
import {getActionFormView, getActionTreeView, getActionView} from "../../../../api/commonApi.ts";
import MyServiceLog from "../../../../components/service-log/my-service-log.vue";

const {proxy} = getCurrentInstance() as ComponentInternalInstance;
const route = useRoute();
const serviceFieldStore = useGlobalFieldDataStore()
const serviceStore = useGlobalServiceDataStore()

const moduleName = ref<string>(route.params.module as string)
const row_id = ref<number | undefined>(parseInt(route.query.id as string))
const serviceName = ref<string>(route.params.service as string)
const form_container = ref()

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
const template_component = shallowRef<any>(null)

const renderView = async (arch: string) => {
    await parserXml(arch)
}

let template_fields = ref<string[]>([]);

let parserResult: XMLParserResult | null = null;

const parserXml = async (str: string) => {
    const serviceFields = await serviceFieldStore.getFieldByServiceNameAsync(serviceName.value)
    const primaryKeyField = await serviceStore.getServiceByNameAsync(serviceName.value)
    parserResult = await parserEx(str, serviceName.value)
    xmlTemplate.value = getTemplate(parserResult);
    template_fields.value.splice(0, template_fields.value.length)
    template_fields.value.push(...parserResult.fullFields.map(x => x.name))
    if (parserResult.one2ManyFields && parserResult.one2ManyFields.length) {
        for (let manyField of parserResult.one2ManyFields) {
            const find = serviceFields.find(x => x.name == manyField) as Field;
            const viewData = await loadTreeView(find.relativeServiceName);
            const tempKeyField = serviceFieldStore.getPrimaryKeyFieldByServiceName(find.relativeServiceName)
            const parserResult2 = await parserEx(viewData.arch, find.relativeServiceName)
            for (let tempField of parserResult2.fullFields) {
                template_fields.value.push(`${manyField}.${tempField.name}`)
            }
            if (!template_fields.value.includes(`${manyField}.${tempKeyField.name}`)) {
                template_fields.value.push(`${manyField}.${tempKeyField.name}`)
            }
        }
    }
    if (!template_fields.value.includes(primaryKeyField.nameField)) {
        template_fields.value.push(primaryKeyField.nameField)
    }
}

const recordRow = ref<any>({})
const recordRowWithField = ref<Record<string, FormField>>({})
const loadDetailData = async (id: number) => {
    return getModelDetailApi(id, template_fields.value.join(","),
        serviceName.value);
}
const loadDataWithLayout = async () => {
    if (!(serviceName.value && moduleName.value && template_fields.value.length)) {
        return;
    }
    const serviceFields = await serviceFieldStore.getFieldByServiceNameAsync(serviceName.value)
    if (row_id.value) {
        loadDetailData(row_id.value).then(data => {
            if (data) {
                Object.assign(recordRow.value, data)
                for (let key in recordRow.value) {
                    const field = serviceFields.find(f => f.name === key) as Field
                    if (field.type == FieldTypeEnum.One2manyField) {
                        recordRowWithField.value[key] = new FormField(recordRow.value[key], field)
                    } else {
                        recordRowWithField.value[key] = new FormField(recordRow.value[key], field)
                    }

                }
                template_component.value = createFormTemplateVNode();
            }
        })
    } else {
        for (let key of template_fields.value) {
            const field = serviceFields.find(f => f.name === key)
            if (field && field.type == FieldTypeEnum.One2manyField) {
                recordRowWithField.value[key] = new FormField([], field)
            } else if (field) {
                recordRowWithField.value[key] = new FormField(undefined, field)
            }
        }
        template_component.value = createFormTemplateVNode();
    }
}


const createFormTemplateVNode = () => {
    return defineComponent({
        setup() {
            const vNode = compile(xmlTemplate.value)
            return () => {
                return createVNode(vNode, {...recordRowWithField.value})
            }
        }
    })
}
// 新增，初始化对象
const createClick = () => {
    row_id.value = undefined;

    for (let fieldKey in recordRowWithField.value) {
        recordRowWithField.value[fieldKey].reset("")
    }
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
            goModelForm(moduleName.value, serviceName.value, data.id)
        })
    }
}

const insert = async () => {
    const recordRow = {} as any;
    for (let fieldKey in recordRowWithField.value) {
        if (recordRowWithField.value[fieldKey].isChanged()) {
            recordRow[fieldKey] = await recordRowWithField.value[fieldKey].getRawValue()
        }
    }

    return await addModelApi(recordRow, serviceName.value).then(data => {
        proxy?.$notify.success("新增", "新增成功");
        row_id.value = data.id
        return data
    }).catch(error => {
        proxy?.$notify.error("新增", error.msg);
    })
}

const update = async () => {
    const recordRow = {} as any;
    for (let fieldKey in recordRowWithField.value) {
        if (recordRowWithField.value[fieldKey].isChanged()) {
            recordRow[fieldKey] = await recordRowWithField.value[fieldKey].getRawValue()
        }
    }
    if (Object.keys(recordRow).length === 0) {
        proxy?.$notify.success("修改", "修改成功");
        return;
    }
    recordRow["id"] = row_id.value

    return editModelApi(recordRow, serviceName.value).then(data => {
        proxy?.$notify.success("修改", "修改成功");
    }).catch(error => {
        proxy?.$notify.error("修改", error.msg);
    })
}
</script>

<template>
    <div class="p-4 w-full overflow-hidden h-full box-border">
        <div>
            <MyButton type="primary" rounded @click="createClick">新增</MyButton>
            <MyButton type="success" rounded @click="saveClick" class="ml-2">保存</MyButton>
        </div>

        <div class="w-full flex overflow-hidden h-full box-border">
            <div class="w-[1060px] flex-1">
                <component :is="template_component"/>
            </div>
            <div class=" w-[590px] h-full box-border">
                <div class="w-full overflow-auto h-full">
                    <MyServiceLog :service="serviceName" :service-id="row_id"></MyServiceLog>
                </div>
            </div>
        </div>


    </div>

</template>

<style scoped>

</style>