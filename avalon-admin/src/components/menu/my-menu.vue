<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import './my-menu.css';
import {useGlobalMenuDataStore} from "../../global/store/menuStore.ts";
import MenuModel from "../../model/MenuModel.ts";
import myMenuItem from './menu-item/my-menu-item.vue';


const emit = defineEmits(['menuClick'])

const menuStore = useGlobalMenuDataStore().getModuleMenu();

const menuClickHandler = (menu: MenuModel) => {
    emit('menuClick', menu)
}

</script>

<template>
    <div class="flex px-1">
        <myMenuItem v-for="menu in menuStore" :key="menu.id" :menu="menu"
                    @menuClick="menuClickHandler"/>
    </div>
</template>

<style scoped>

</style>