<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import MyDropdown from "../dropdown/my-dropdown.vue";
import MyDropdownMenu from "../dropdown/my-dropdown-menu.vue";
import MyDropdownItem from "../dropdown/my-dropdown-item.vue";
import {useUserInfoStore} from "../../global/store/userInfoStore.ts";
import {goExcalidraw, goLogin} from "../../util/routerUtils.ts";
import {getFileUploadUrl} from "../../api/env.ts";
import MyAvatar from "../avatar/my-avatar.vue";
import {clearToken} from "../../cache/tokenStorage.ts";
import {emitLogout} from "../../global/bus/mittBus.ts";

const userInfoStore = useUserInfoStore();

const logoutClick = () => {
  document.body.removeAttribute('login')
  emitLogout()
  clearToken()
  goLogin()
}

const goExcalidrawClick = () => {
  goExcalidraw()
}

</script>

<template>
  <my-dropdown trigger="click">
    <template #default>
      <template v-if="!userInfoStore.user.avatar">
        <div class="hover:bg-gray-200  rounded">{{ userInfoStore.user.name }}</div>
      </template>
      <template v-else>
        <MyAvatar :src="getFileUploadUrl(userInfoStore.user.avatar)" size="small" class="cursor-pointer"></MyAvatar>
      </template>
    </template>
    <template #dropdown>
      <my-dropdown-menu>  
        <my-dropdown-item @click="goExcalidrawClick">excalidraw</my-dropdown-item>
        <my-dropdown-item @click="logoutClick">退出</my-dropdown-item>
      </my-dropdown-menu>
    </template>
  </my-dropdown>
</template>

<style scoped>

</style>