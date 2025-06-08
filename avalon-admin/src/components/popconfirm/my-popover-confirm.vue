<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import MyPopover from "../popover/my-popover.vue";
import MyButton from "../button/my-button.vue";
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
        <template #default>
            <slot></slot>
        </template>
        <template #content>
          <div class="flex justify-end pt-2">
            <MyButton class="mr-2" is-link type="info" @click="cancelClick">取消</MyButton>
            <MyButton is-link type="danger" @click="sureClick">确认</MyButton>
          </div>
        </template>
    </MyPopover>
</template>

<style scoped>

</style>