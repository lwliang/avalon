<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import './my-menu.css';
import {useMenuStore} from "../../global/store/menuStore.ts";
import MenuModel from "../../model/MenuModel.ts";
import myMenuItem from './my-menu-item.vue';


const emit = defineEmits<{
  (e: 'menuClick', menu: MenuModel): void
}>()

const menuStore = useMenuStore().getModuleMenu();

const menuClickHandler = (menu: MenuModel) => {
  emit('menuClick', menu)
}

</script>

<template>
  <el-scrollbar>
    <div class="flex">
      <myMenuItem v-for="menu in menuStore" :key="menu.id" :menu="menu"
                @menuClick="menuClickHandler"/>
    </div>
  </el-scrollbar>
</template>

<style scoped>

</style>