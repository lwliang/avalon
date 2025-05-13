<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/12/12 18:37
 */

import Field from "../../model/Field.ts";
import MyFieldTreeNode from "./my-field-tree-node.vue";

const props = defineProps<{
  nodes: Field[],
  condition?: String,
  showPlusButton?: boolean
}>()

const emit = defineEmits(['fieldSelect'])

const fieldSelectClick = (fieldName: String) => {
  emit('fieldSelect', fieldName)
}

</script>

<template>
  <MyFieldTreeNode :showPlusButton="showPlusButton" v-for="(node,index) in nodes" :node="node" :key="index"
                   :condition="condition"
                   @fieldSelect="fieldSelectClick"></MyFieldTreeNode>
</template>

<style scoped>

</style>