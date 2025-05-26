<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import './my-menu.css';
import MenuModel from "../../model/MenuModel.ts";
import MyPopover from "../popover/my-popover.vue";
import {TriggerType} from "../popover/types.ts";
import MyMenuSubItems from "./my-menu-sub-items.vue";

const props = defineProps<{
  menu: MenuModel,
  trigger?: TriggerType
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
    <div class="cursor-pointer  truncate menu-item "
         @click="menuClickHandler(menu)">
      {{ menu.name }}
    </div>
  </template>
  <template v-else>
    <MyPopover ref="popper" placement="bottom" :trigger="trigger" :arrow-show="false" popper-class="py-1">
      <template #default>
        <div class="cursor-pointer my-menu truncate menu-item">
          {{ menu.name }}
        </div>
      </template>

      <template #content>
        <div class="dropdown-item flex flex-col">
          <MyMenuSubItems :menus="menu.children" @menuClick="menuClickHandler"></MyMenuSubItems>
        </div>
      </template>
    </MyPopover>
  </template>
</template>

<style scoped>

</style>