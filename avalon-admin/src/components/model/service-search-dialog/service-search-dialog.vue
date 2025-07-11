<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/12/17 11:03
 */

import MyDialog from "../../dialog/my-dialog.vue";
import ServiceSearch from "../../service-search/service-search.vue";
import { ref } from "vue";

const props = defineProps<{
  title: string,
  service: string
}>()

const emit = defineEmits(['close', 'sure'])

const show = defineModel<boolean>({required: true})

const closeClick = () => {
  show.value = false
  emit('close')
}

const sureClick = async () => {
  emit('sure', selectIds.value)
}

const selectIds = ref<any[]>([])

const rowSelectChangeEvent = (ids: any[]) => {
  selectIds.value.splice(0, selectIds.value.length)
  selectIds.value.push(...ids)
}

</script>

<template>
  <my-dialog
    :title="title"
    v-model="show"
    :draggable="true"
    :close-on-click-modal="false"
    @close="closeClick"
    @sure="sureClick"
    body-class="h-[375px]"
  >
    <service-search :service="service" @rowSelectChange="rowSelectChangeEvent"/>
  </my-dialog>
</template>

<style scoped>

</style>