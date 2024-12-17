<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/12/12 18:38
 */
import Field from '../../model/Field';
import {FieldTypeEnum} from "../../model/enum-type/FieldTypeEnum.ts";
import {ref} from "vue";
import {getServiceFieldApi} from "../../api/modelApi.ts";
import './my-field-tree-node.css'
import {nextId} from "../../util/SnowflakeUtils.ts";

const props = defineProps<{
    node: Field,
    condition?: String
}>()

const emit = defineEmits(['fieldSelect'])
const childOpen = ref(false)

const childFields = ref<Field[]>([])

const fieldClick = () => {
    if (props.node.type == FieldTypeEnum.One2manyField
        || props.node.type == FieldTypeEnum.One2oneField ||
        props.node.type == FieldTypeEnum.Many2manyField ||
        props.node.type == FieldTypeEnum.Many2oneField) {// 点击张开
        if (!childFields.value.length) {
            getServiceFieldApi(props.node.relativeServiceName, `'${props.condition}'`).then(data => {
                childFields.value.splice(0, childFields.value.length)
                childFields.value.push(...data)
                childOpen.value = !childOpen.value
            })
        } else {
            childOpen.value = !childOpen.value
        }
        return
    }
    emit('fieldSelect', {name: props.node.name, label: props.node.label, id: nextId()})
}

const fieldAddClick = () => {
    emit('fieldSelect', {name: props.node.name, label: props.node.label, id: nextId()})
}

const fieldSelect = (node: { name: String, label: string, id: string }) => {
    emit('fieldSelect', {
        name: props.node.name + "." + node.name,
        label: props.node.label + "/" + node.label,
        id: node.id
    })
}
</script>

<template>
    <div class="w-full">
        <div class="flex node-item" @click="fieldClick">
            <div class="w-[15px]">
                <MyIcon icon="angle-right" type="fas" color="var(--color-border)" v-if="node.type == FieldTypeEnum.One2manyField
        || node.type == FieldTypeEnum.One2oneField || node.type == FieldTypeEnum.Many2manyField || node.type == FieldTypeEnum.Many2oneField"></MyIcon>
            </div>
            <div class="flex-1">
                {{ node.label }}
            </div>
            <div @click.stop="fieldAddClick">
                <MyIcon icon="plus" type="fas" color="var(--overlay-color)"></MyIcon>
            </div>
        </div>
        <div class="pl-4" v-if="childOpen">
            <MyFieldTreeNode v-for="(node,index) in childFields" :node="node" :key="index"
                             @fieldSelect="fieldSelect"></MyFieldTreeNode>
        </div>
    </div>
</template>

<style scoped>


</style>