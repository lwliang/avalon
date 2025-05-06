<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import MyDropdown from "../dropdown/my-dropdown.vue";
import MyDropdownItem from "../dropdown/my-dropdown-item.vue";
import {useUserInfoStore} from "../../global/store/userInfoStore.ts";
import {goExcalidraw, goLogin} from "../../util/routerUtils.ts";
import {getFileUploadUrl} from "../../api/env.ts";
import MyAvatar from "../avatar/my-avatar.vue";
import {clearToken} from "../../cache/tokenStorage.ts";

const userInfoStore = useUserInfoStore();

const logoutClick = () => {
    document.body.removeAttribute('login')
    clearToken()
    goLogin()
}

const goExcalidrawClick = () => {
    goExcalidraw()
}
</script>

<template>
    <my-dropdown>
        <template #default>
            <template v-if="!userInfoStore.user.avatar">
                <div class="hover:bg-gray-200 px-2 py-0.5 rounded">{{ userInfoStore.user.name }}</div>
            </template>
            <template v-else>
                <MyAvatar :src="getFileUploadUrl(userInfoStore.user.avatar)" width="32px" height="32px"></MyAvatar>
            </template>
        </template>
        <template #dropdown>
            <my-dropdown-item label="excalidraw" @itemClick="goExcalidrawClick"></my-dropdown-item>
            <my-dropdown-item label="退出" @itemClick="logoutClick"></my-dropdown-item>
        </template>
    </my-dropdown>
</template>

<style scoped>

</style>