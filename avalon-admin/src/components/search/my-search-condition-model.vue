<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/13 15:13
 */

import MyModel from "../model/my-model/my-model.vue";
import {ref} from "vue";
import MySearchCondition from "./my-search-condition.vue";
import {useTemplateRef} from "@vue/runtime-dom";

const props = defineProps<{
  serviceName: string,
  show: boolean,
}>()

const searchConditionRef = useTemplateRef<any>('searchCondition')

const emits = defineEmits(['sureClick', 'closeClick'])


const closeClick = () => {
  emits('closeClick')
}

const sureClick = () => {
  const conStr = searchConditionRef.value.getConditionString();
  emits('sureClick', conStr)
}
</script>

<template>
  <my-model title="高级查询" :show="show" @sure="sureClick" @close="closeClick" model-class="w-[970px] min-h-[245px]">
    <my-search-condition :serviceName="serviceName" ref="searchCondition"/>
  </my-model>
</template>

<style scoped>

</style>