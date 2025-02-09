<script setup lang="ts">
import MyImage from "../image/my-image.vue";
import {ref} from "vue";
import {getFileUploadUrl} from "../../api/env.ts";
import {getUserByImUserIdCache} from "../../ws/wsAPI.ts";

const props = defineProps({
    msgId: String,
    fromImId: Number,
    toImId: Number,
    content: String,
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
            <div class="ml-3">
                <MyImage width="100" :src="content" :radius="5"/>
            </div>
        </div>
        <div v-else class="flex justify-end">
            <div class="mr-3">
                <MyImage width="100" :src="content" :radius="5"/>
            </div>
            <div>
                <my-image height="35" width="35" :src="avatar" :radius="5"></my-image>
            </div>
        </div>
    </div>
</template>

<style scoped>

</style>