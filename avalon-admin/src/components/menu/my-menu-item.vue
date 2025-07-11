<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import './my-menu.css';
import MenuModel from "../../model/MenuModel.ts";
import MyMenuSubItems from "./my-menu-sub-items.vue";

const props = defineProps<{
  menu: MenuModel,
  trigger?: 'click' | 'hover' | 'focus'
}>()
const emit = defineEmits<{
  (e: 'menuClick', menu: MenuModel): void
}>()

const menuClickHandler = (menu: MenuModel) => {
  emit('menuClick', menu)
}
</script>

<template>
  <template v-if="!menu.children || !menu.children.length">
    <div class="cursor-pointer text-nowrap menu-item"
         @click="menuClickHandler(menu)">
      {{ menu.name }}
    </div>
  </template>
  <template v-else>
    <el-popover
      placement="bottom"
      :trigger="trigger || 'hover'"
      :show-arrow="false"
      popper-class="menu-popover"
      width="auto"
    >
      <template #reference>
        <div class="cursor-pointer my-menu text-nowrap menu-item">
          {{ menu.name }}
        </div>
      </template>
      
      <template #default>
        <div class="dropdown-item">
          <MyMenuSubItems :menus="menu.children" @menuClick="menuClickHandler" />
        </div>
      </template>
    </el-popover>
  </template>
</template>

<style scoped>
/* 自定义popover样式 */
:global(.menu-popover) {
  padding: 4px 0 !important;
  min-width: 120px;
}

.dropdown-item {
  display: flex;
  flex-direction: column;
}
</style>