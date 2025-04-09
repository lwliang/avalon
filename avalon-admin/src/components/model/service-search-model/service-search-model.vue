<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/12/17 11:03
 */

import MyOverlay from "../../overlay/my-overlay.vue";
import MyIcon from "../../icon/my-icon.vue";
import MyButton from "../../button/my-button.vue";
import ServiceSearch from "../../service-search/service-search.vue";
import {ref} from "vue";


const props = defineProps<{
    title: string,
    show: boolean,
    service: string
}>()
const emit = defineEmits(['close', 'sure'])
const closeClick = () => {
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
    <MyOverlay v-if="props.show">
        <div class="absolute-center w-[980px] h-[375px] bg-background flex flex-col">
            <div class="model-head py-2">
                <div class="dialog-title flex-1">
                    {{ title }}
                </div>
                <div class="dialog-close pr-2">
                    <MyIcon class="cursor-pointer" icon="xmark" type="fas" @click="closeClick"></MyIcon>
                </div>
            </div>
            <div class="model-content flex-1 overflow-hidden">
                <service-search :service="service" @rowSelectChange="rowSelectChangeEvent"/>
            </div>
            <div class="model-footer">
                <MyButton type="info" rounded @click="closeClick">取消</MyButton>
                <MyButton class="ml-3" type="primary" rounded @click="sureClick">确认</MyButton>
            </div>
        </div>
    </MyOverlay>
</template>

<style scoped>

</style>