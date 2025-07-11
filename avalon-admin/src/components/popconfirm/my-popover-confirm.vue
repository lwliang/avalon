<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import MyPopover from "../popover/my-popover.vue";
import {ref} from "vue";
defineProps({
    content: {
        type: String
    }
})

const emit = defineEmits(['click'])

const popper = ref<any>()

const cancelClick = () => {
    popper.value?.hide()
}

const sureClick = () => {
    emit('click')
    popper.value?.hide()
}
</script>

<template>
    <MyPopover ref="popper" placement="top" trigger="click" :content="content" popper-class="p-2">
        <template #reference>
            <slot></slot>
        </template>
        <template #default>
          <div class="flex justify-end pt-2">
            <el-button class="mr-2" link type="info" @click="cancelClick">取消</el-button>
            <el-button link type="danger" @click="sureClick">确认</el-button>
          </div>
        </template>
    </MyPopover>
</template>

<style scoped>

</style>