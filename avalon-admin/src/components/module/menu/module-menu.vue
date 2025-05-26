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
  <div
      class="flex flex-col justify-start items-center py-3 h-full overflow-y-auto">
    <div v-for="(module,index) in modules" :key="index"
         class="flex flex-col justify-center items-center cursor-pointer my-1"
         @click="moduleItemClick(module)">
      <MyImage width="24px" height="24px" :src="getModuleIcon(module.name,module.icon)"></MyImage>
      <div class="pb-0.5 mt-1 text-sm">{{ module.label }}</div>
    </div>
  </div>
</template>

<style scoped>

</style>