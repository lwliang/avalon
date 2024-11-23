<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import './my-dialog.css'
import MyIcon from "../icon/my-icon.vue";
import MyButton from "../button/my-button.vue";

defineProps({
    zIndex: {
        type: Number,
        default: 2000
    },
    title: {
        type: String,
        default: "标题"
    },
    show: {
        type: Boolean,
        default: false
    }
})

const emit = defineEmits(['close', 'sure'])

const closeClick = () => {
    emit('close')
}

const sureClick = () => {
    emit('sure')
}
</script>

<template>
    <teleport to="body">
        <div class="dialog-overlay" :class="zIndex" v-if="show">
            <div class="dialog">
                <div class="dialog-header flex">
                    <div class="dialog-title flex-1">
                        {{ title }}
                    </div>
                    <div class="dialog-close pr-2">
                        <MyIcon class="cursor-pointer" icon="xmark" type="fas" @click="closeClick"></MyIcon>
                    </div>
                </div>
                <div class="dialog-content">
                    <slot name="default"></slot>
                </div>
                <div class="dialog-footer flex justify-end">
                    <template v-if="$slots.footer">
                        <slot name="footer"></slot>
                    </template>
                    <template v-else>
                        <MyButton type="info" rounded @click="closeClick">取消</MyButton>
                        <MyButton class="ml-3" type="primary" rounded @click="sureClick">确认</MyButton>
                    </template>
                </div>
            </div>
        </div>
    </teleport>
</template>

<style scoped>

</style>