<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import './my-dialog.css'
import MyIcon from "../icon/my-icon.vue";
import MyButton from "../button/my-button.vue";
import {ComponentInternalInstance, computed, getCurrentInstance, ref, watch} from "vue";
import FormField from "../../model/FormField.ts";
import Field from "../../model/Field.ts";
import {getServiceFieldApi} from "../../api/modelApi.ts";
import MyFieldTree from "../tree/my-field-tree.vue";
import {useDebounceFn} from "@vueuse/core";

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

const leftSearch = ref<FormField>(new FormField(''))
const rightSearch = ref<FormField>(new FormField(''))

watch(() => leftSearch.value.value, (condition) => {
    searchFields(`'${condition}'`)
})


const closeClick = () => {
    emit('close')
}

const sureClick = () => {
    if (!selectFields.value.length) {
        proxy?.$notify.error("提示", "请选择要导出的字段")
        return
    }
    const fieldStr = selectFields.value.map(x => x.name).join(",")
    emit('sure', fieldStr)
}
const fields = ref<Field[]>([])

let searchFields = (field: string) => {
    getServiceFieldApi(props.service as string, field).then(data => {
        fields.value.splice(0, fields.value.length)
        fields.value.push(...data)
    })
}

searchFields = useDebounceFn(searchFields, 200)
searchFields("")

const selectFields = ref<{ label: string, name: string, id: string }[]>([])
const selectFieldStr = computed(() => {
    return selectFields.value.filter(x => x.label.includes(rightSearch.value.value));
})
const pushSelectField = (selectField: { label: string, name: string, id: string }) => {
    selectFields.value.push(selectField);
}

const leftFieldSelect = (field: { label: string, name: string, id: string }) => {
    pushSelectField(field)
}

const deleteSelectField = (field: { label: string, name: string, id: string }) => {
    let index = selectFields.value.findIndex(x => x.id == field.id);
    if (index >= 0) {
        selectFields.value.splice(index, 1)
    }
}
</script>

<template>
    <teleport to="body">
        <div class="dialog-overlay" :class="zIndex" v-if="show">
            <div class="dialog w-[40%] h-[70%] flex flex-col">
                <div class="dialog-header flex border-b p-4">
                    <div class="dialog-title flex-1">
                        {{ title }}
                    </div>
                    <div class="dialog-close pr-2">
                        <MyIcon class="cursor-pointer" icon="xmark" type="fas" @click="closeClick"></MyIcon>
                    </div>
                </div>
                <div class="dialog-content p-4 flex-1 overflow-hidden">
                    <template v-if="$slots.default">
                        <slot name="default"></slot>
                    </template>
                    <template v-else>
                        <div class="flex h-full">
                            <div class="flex-1 flex flex-col h-full overflow-hidden">
                                <div class="pb-2 flex-shrink-0">可用字段</div>
                                <MyInput class="flex-shrink-0" v-model="leftSearch" placeholder="搜索"></MyInput>
                                <div class="h-4 flex-shrink-0"></div>
                                <div class="flex-1 overflow-y-auto border relative rounded">
                                    <div class="p-2">
                                        <MyFieldTree :nodes="fields" @fieldSelect="leftFieldSelect"
                                                     :condition="leftSearch.value"/>
                                    </div>
                                </div>
                            </div>
                            <div class="w-4">

                            </div>
                            <div class="flex-1 flex flex-col h-full ">
                                <div class="pb-2">要导出的字段</div>
                                <MyInput v-model="rightSearch" placeholder="搜索"></MyInput>
                                <div class="h-4 flex-shrink-0"></div>
                                <div class="flex-1 overflow-y-auto border relative rounded p-2">
                                    <div v-for="(field,index) in selectFieldStr" :key="index"
                                         class="flex items-center">
                                        <div class="pr-1 cursor-pointer">
                                            <MyIcon icon="sort" type="fas" color="#000"></MyIcon>
                                        </div>
                                        <div class="flex-1">{{ field.label }}</div>
                                        <div class="pl-1 cursor-pointer" @click="deleteSelectField(field)">
                                            <MyIcon icon="trash-can" type="fas" color="#000"></MyIcon>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </template>
                </div>
                <div class="dialog-footer flex justify-end p-4">
                    <template v-if="$slots.footer">
                        <slot name="footer"></slot>
                    </template>
                    <template v-else>
                        <MyButton type="info" rounded @click="closeClick">取消</MyButton>
                        <MyButton class="ml-3" type="primary" rounded @click="sureClick">导出</MyButton>
                    </template>
                </div>
            </div>
        </div>
    </teleport>
</template>

<style scoped>

</style>