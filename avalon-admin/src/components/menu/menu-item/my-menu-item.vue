<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import MenuModel from "../../../model/MenuModel.ts";
import MyPopover from "../../popover/my-popover.vue";
import {popoverTrigger} from "../../popover/my-popover.ts";
import MyMenuSubItems from "../menu-sub-item/my-menu-sub-items.vue";
import type {PropType} from "vue";

const props = defineProps({
    menu: {
        type: Object as PropType<MenuModel>,
        required: true
    },
    trigger: {
        type: popoverTrigger,
        default: 'hover'
    }
})
const emit = defineEmits(['menuClick'])

const menuClickHandler = (menu: MenuModel) => {
    emit('menuClick', menu)
}
</script>

<template>
    <template v-if="!menu.children || !menu.children.length">
        <div class="cursor-pointer my-menu mx-1"
             @click="menuClickHandler(menu)">
            {{ menu.name }}
        </div>
    </template>
    <template v-else>
        <MyPopover ref="popper" placement="bottom" :trigger="trigger" :arrow-show="false">
            <template #default>
                <div class="cursor-pointer my-menu mx-1">
                    {{ menu.name }}
                </div>
            </template>

            <template #option>
                <div class="dropdown-item flex flex-col">
                    <MyMenuSubItems :menus="menu.children" @menuClick="menuClickHandler"></MyMenuSubItems>
                </div>
            </template>
        </MyPopover>
    </template>
</template>

<style scoped>

</style>