<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {useGlobalModuleDataStore} from "../../../global/store/moduleStore.ts";
import MyImage from "../../image/my-image.vue";
import {getModuleIcon} from "../../../api/moduleApi.ts";
import {ref} from "vue";
import Module from "../../../model/Module.ts";

const emit = defineEmits(['moduleClick'])

const modules = ref(useGlobalModuleDataStore().getInstallModule());

const moduleItemClick = (module: Module) => {
    emit('moduleClick', module);
}
</script>

<template>
    <div class="flex flex-wrap p-1">
        <div v-for="(module,index) in modules" :key="index"
             class="flex flex-col justify-center items-center m-2 cursor-pointer"
             @click="moduleItemClick(module)">
            <MyImage width="50" height="50" :src="getModuleIcon(module.name,module.icon)"></MyImage>
            <div class="pb-0.5 mt-1">{{ module.label }}</div>
        </div>
    </div>
</template>

<style scoped>

</style>