<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import MyDropdown from "../dropdown/my-dropdown.vue";
import MyDropdownItem from "../dropdown/my-dropdown-item.vue";
import {useUserInfoStore} from "../../global/store/userInfoStore.ts";
import {goLogin} from "../../util/routerUtils.ts";

const userInfoStore = useUserInfoStore();

const logoutClick = () => {
    goLogin()
}
</script>

<template>
    <my-dropdown>
        <template #default>
            <div class="hover:bg-gray-200 px-2 py-0.5 rounded">{{ userInfoStore.user.name }}</div>
        </template>
        <template #dropdown>
            <my-dropdown-item label="退出" @itemClick="logoutClick"></my-dropdown-item>
        </template>
    </my-dropdown>
</template>

<style scoped>

</style>