<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/13 15:13
 */

import MyDialog from "../dialog/my-dialog.vue";
import { ref } from "vue";
import MySearchCondition from "./my-search-condition.vue";

const props = defineProps<{
  serviceName: string,
}>()

const searchConditionRef = ref<any>()

const emits = defineEmits(['sureClick', 'closeClick'])

const show = defineModel<boolean>({required: true})


const closeClick = () => {
  show.value = false
  emits('closeClick')
}

const sureClick = () => {
  const conStr = searchConditionRef.value.getConditionString();
  emits('sureClick', conStr)
}
</script>

<template>
  <my-dialog
    title="高级查询"
    v-model="show"
    :close-on-click-modal="false"
    :draggable="true"
    @sure="sureClick"
    @close="closeClick"
    body-class="min-h-[245px]"
  >
    <my-search-condition :serviceName="serviceName" ref="searchConditionRef"/>
  </my-dialog>
</template>

<style scoped>

</style>