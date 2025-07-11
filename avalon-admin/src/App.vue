<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import mittBus from "./global/bus/mittBus.ts";
import {isLogin} from "./util/commonUtils.ts";
import {goExcalidraw, goLogin} from "./util/routerUtils.ts";
import {onMounted, onUnmounted} from "@vue/runtime-dom";
import {Location} from "@element-plus/icons-vue";
import {useRoute} from "vue-router";


const route = useRoute()
if (isLogin()) {
  mittBus.emit("loadModule")
  mittBus.emit("loadService")
  mittBus.emit('loadUserInfo')
}

const handleKeydown = (event: KeyboardEvent) => {
  if (event.altKey && event.shiftKey) {
    switch (event.key) {
      case "h":
      case "H":
        goExcalidraw()
        break
    }
  }
}

onMounted(() => {
  window.addEventListener('keydown', handleKeydown)
})
onUnmounted(() => {
  window.removeEventListener('keydown', handleKeydown)
})

</script>

<template>
  <router-view></router-view>
<!--  <router-view v-slot="{Component}">-->
<!--    <keep-alive>-->
<!--      <component :is="Component" :key="route.fullPath"/>-->
<!--    </keep-alive>-->
<!--  </router-view>-->
  <div class="contents pb-1"></div>
</template>

<style scoped>

</style>
