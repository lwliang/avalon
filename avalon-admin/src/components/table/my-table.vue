<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import Field from "../../model/Field.ts";
import {FieldTypeEnum} from "../../model/enum-type/FieldTypeEnum.ts";
import {computed, ref, watch} from "vue";
import {getSelectionValueByServiceAndField} from "../../cache/SelectionValueMemory.ts";
import {useGlobalServiceDataStore} from "../../global/store/serviceStore.ts";
import {ComputedRef} from "@vue/reactivity";
import MyIcon from "../icon/my-icon.vue";
import MyButton from "../button/my-button.vue";
import {getFileUploadUrl} from "../../api/env.ts";
import MyImage from "../image/my-image.vue";
import MyMany2manySelect from "../select/many2may-select/my-many2many-select.vue";
import FormField from "../../model/FormField.ts";
import MyCheck from "../check/my-check.vue";
import {getDateTime} from "../../util/dateUtils.ts";
import MyDebug from "../debug/my-debug.vue";
import {useUserInfoStore} from "../../global/store/userInfoStore.ts";
import {useDebounceFn} from "@vueuse/core";

const emit = defineEmits(['rowClick', 'rowDeleteClick', 'rowSelectChange'])

const props = defineProps<{
    record: any[],
    serviceName: string,
    fields: Field[],
    height?: string,
    showDeleteBtn?: boolean,
    showSelectBtn?: boolean
}>()
const selectionDynamic = ref<any>({})
const userInfoStore = useUserInfoStore()
const serviceStore = useGlobalServiceDataStore()

const loadData = async () => {
    if (!props.serviceName) {
        return;
    }

    for (let field of props.fields) {
        if (field.type == FieldTypeEnum.SelectionField || field.type == FieldTypeEnum.FieldSelectionField) { // 得到字段对应的selection的值
            selectionDynamic.value[field.name] = await getSelectionValueByServiceAndField(props.serviceName, field.name)
        }
    }
}


const getSelectionField = (field: Field, row: any): ComputedRef => {
    return computed(() => {
        if (field.name in selectionDynamic.value) {
            return selectionDynamic.value[field.name][row[field.name]]
        }
        return ''
    })
}

watch(() => props.fields.length, (newValue) => { // 增加selection字段获取
    if (newValue) {
        loadData()
    }
}, {immediate: true})

const rowClick = (row: any) => {
    emit('rowClick', row)
}

const rowDeleteClick = (row: any) => {
    emit('rowDeleteClick', row)
}

const getImageUrl = (file: any) => {
    if (file instanceof File) {
        return URL.createObjectURL(file)
    }

    return getFileUploadUrl(file)
}
const getMany2manyFormField = (obj: any, value: any, field: Field) => {
    obj[field.name + '_many'] = new FormField(value, field)
    return ''
}
const getFormField = (obj: any, value: any, field: Field) => {
    obj[field.name + '_field'] = new FormField(value, field)
    return ''
}

const allSelect = ref(new FormField(false))
const web_select = 'web_select';
let selectChange = (value: any) => {
    let selectedSum = 0
    for (let row of props.record) {
        if (row[web_select].value) {
            selectedSum++;
        }
    }
    if (selectedSum == props.record.length && selectedSum != 0) {
        allSelect.value.value = true;
    } else {
        allSelect.value.value = false;
    }
    const ids = props.record.filter(x => x[web_select].value).map(y => y.id);
    emit('rowSelectChange', selectedSum, ids)
}

selectChange = useDebounceFn(selectChange, 100)
watch(() => props.record.length, (length) => {
    if (props.showSelectBtn) {
        for (let row of props.record) {
            row[web_select] = new FormField(false)
        }
        selectChange([]);
    }
}, {immediate: true, deep: true})

watch(() => allSelect.value.value, (all) => {
    if (all) { // 全选
        for (let row of props.record) {
            row[web_select].value = true;
        }
    } else { // 全关
        for (let row of props.record) {
            row[web_select].value = false;
        }
    }
})

</script>

<template>
    <div class="w-full overflow-auto" :style="{'max-height': height || 'auto'}">
        <table class="data-table w-[1000px]">
            <thead class="sticky top-0" style="left: auto;bottom: auto;right: auto;z-index: 10;">
            <tr class="border-b">
                <th v-if="showSelectBtn" class="w-[28px]">
                    <MyCheck v-model="allSelect"/>
                </th>
                <template v-for="field in fields" :key="field.id">
                    <th v-if="!field.isPrimaryKey" class="whitespace-nowrap">
                        <span>{{ field.label }}</span>
                        <template v-if="userInfoStore.user.debug">
                            <span class="px-0.5"></span>
                            <MyDebug :service="serviceName" :field="field.name"/>
                        </template>
                    </th>
                </template>

                <th class="w-[24px]" v-if="showDeleteBtn">
                    <MyIcon icon="sliders" type="fas"/>
                </th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="row in record" :key="row.id" class="border-b cursor-pointer" @click="rowClick(row)">
                <td v-if="showSelectBtn" class="w-[28px]" @click.stop="void(0)">
                    <MyCheck v-model="row[web_select]" @change="selectChange"/>
                </td>
                <template v-for="field in fields" :key="field.id">
                    <td v-if="!field.isPrimaryKey">
                        <template v-if="field.type == FieldTypeEnum.SelectionField">
                            {{ getSelectionField(field, row) }}
                        </template>
                        <template v-else-if="field.type == FieldTypeEnum.FieldSelectionField">
                            {{ getSelectionField(field, row) }}
                        </template>
                        <template v-else-if="field.type == FieldTypeEnum.Many2oneField">
                            {{
                                row[field.name] ? row[field.name][serviceStore.getServiceByName(field.relativeServiceName).nameField] : ''
                            }}
                        </template>
                        <template v-else-if="field.type == FieldTypeEnum.ImageField">
                            <MyImage width="50" height="50" :src="getImageUrl(row[field.name])"></MyImage>
                        </template>
                        <template v-else-if="field.type == FieldTypeEnum.Many2manyField">
                            {{ getMany2manyFormField(row, row[field.name], field) }}
                            <MyMany2manySelect v-model="row[field.name+'_many']" :readonly="true"
                                               :ref="field.name+'_input'"
                                               :service="field.relativeServiceName"
                                               :field="field.name"
                                               :htmlId="field.name"
                                               :htmlName="field.name"/>
                        </template>
                        <template v-else-if="field.type == FieldTypeEnum.BooleanField">
                            {{ getFormField(row, row[field.name], field) }}
                            <MyCheck :ref="field.name+'_input'" v-model="row[field.name+'_field']" :readonly="true"
                                     :field="field.name"
                                     :htmlId="field.name"
                                     :htmlName="field.name"></MyCheck>
                        </template>
                        <template v-else-if="field.type == FieldTypeEnum.DateTimeField">
                            {{ row[field.name] ? getDateTime(row[field.name]) : '' }}
                        </template>
                        <template v-else>
                            {{ row[field.name] }}
                        </template>
                    </td>
                </template>

                <td class="w-[24px]" @click.stop="()=>{}" v-if="showDeleteBtn">
                    <MyButton @click="rowDeleteClick(row)" icon="trash-can" icon-style="fas" is-link type="primary"
                              icon-color="#212529"></MyButton>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</template>

<style scoped>

</style>