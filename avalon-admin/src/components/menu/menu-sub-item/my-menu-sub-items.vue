<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import MenuModel from "../../../model/MenuModel.ts";

const props = defineProps<{ menus: MenuModel[] }>()
const emit = defineEmits(['menuClick'])

const menuClickHandler = (menu: MenuModel) => {
    emit('menuClick', menu)
}
</script>

<template>
    <div class="flex flex-col w-full">
        <div v-for="(menu,index) in menus" :key="index" class="cursor-pointer my-menu mx-1 whitespace-nowrap"
             @click="menuClickHandler(menu)">
            {{ menu.name }}
        </div>
    </div>

</template>

<style scoped>

</style>