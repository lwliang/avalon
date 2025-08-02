<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import MenuModel from "../../model/MenuModel.ts";
import './my-menu.css';

const props = defineProps<{ menus: MenuModel[], level?: number }>()
const emit = defineEmits<{
  (e: 'menuClick', menu: MenuModel): void
}>()

const menuClickHandler = (menu: MenuModel) => {
    emit('menuClick', menu)
}
</script>

<template>
    <div class="flex flex-col w-full">
        <div v-for="(menu,index) in menus" :key="index" class="menu-sub-item">
            <!-- 没有子菜单的菜单项 二级菜单 -->
            <div v-if="!menu.children || !menu.children.length" 
                 class="cursor-pointer menu-item whitespace-nowrap px-3 py-1 flex items-center"
                 @click="menuClickHandler(menu)">
                 <div :style="{width: level+'px'}"></div>
                {{ menu.name }}
            </div>
            
            <!-- 有子菜单的菜单项 -->
            <div v-else class="submenu-container">
                
                <div class="cursor-pointer menu-item whitespace-nowrap px-3 py-1"
                     @click="menuClickHandler(menu)">
                    {{ menu.name }}
                </div>
                
                <!-- 子菜单内容 -->
                <div class="submenu-content">
                    <MyMenuSubItems :menus="menu.children" :level="10" @menuClick="menuClickHandler" />
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.menu-sub-item {
    position: relative;
}

.submenu-container {
    width: 100%;
}

.submenu-content {
    
}
.menu-item {
    transition: background-color 0.2s ease;
}
</style>