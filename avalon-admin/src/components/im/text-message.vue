<script setup lang="ts">
import MyImage from "../image/my-image.vue";
import {ref} from "vue";
import {getFileUploadUrl} from "../../api/env.ts";
import {getUserByImUserIdCache} from "../../ws/wsAPI.ts";

const props = defineProps({
    msgId: String,
    fromImId: Number,
    toImId: Number,
    text: String,
    nickname: String,
    left: {
        type: Boolean,
        default: true
    }
})

const avatar = ref('')

if (props.fromImId) {
    getUserByImUserIdCache(props.fromImId).then(user => {
        avatar.value = getFileUploadUrl(user.avatar)
    })
}

</script>

<template>
    <div>
        <div v-if="left" class="flex">
            <div>
                <my-image height="35" width="35" :src="avatar" :radius="5"></my-image>
            </div>
            <div class="max-w-[50%] bg-white rounded px-2 py-1 ml-3">
                {{ text }}
            </div>
        </div>
        <div v-else class="flex justify-end">
            <div class="max-w-[50%] bg-white rounded px-2 py-1 mr-3 right-color">
                {{ text }}
            </div>
            <div>
                <my-image height="35" width="35" :src="avatar" :radius="5"></my-image>
            </div>
        </div>
    </div>
</template>

<style scoped>
.right-color {
    background-color: rgb(137, 217, 97);
}
</style>