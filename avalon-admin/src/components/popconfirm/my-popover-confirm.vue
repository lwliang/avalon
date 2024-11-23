<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import MyPopover from "../popover/my-popover.vue";
import MyButton from "../button/my-button.vue";
import {ref} from "vue";
import {PopperAPI} from "../popover/my-popover.ts";

defineProps({
    content: {
        type: String
    }
})

const emit = defineEmits(['click'])

const popper = ref<PopperAPI>()

const cancelClick = () => {
    popper.value?.hide()
}

const sureClick = () => {
    emit('click')
    popper.value?.hide()
}
</script>

<template>
    <MyPopover ref="popper" placement="top" trigger="click" :content="content">
        <template #default>
            <slot></slot>
        </template>

        <template #footer>
            <MyButton class="mr-2" is-link type="info" @click="cancelClick">取消</MyButton>
            <MyButton is-link type="danger" @click="sureClick">确认</MyButton>
        </template>

    </MyPopover>
</template>

<style scoped>

</style>