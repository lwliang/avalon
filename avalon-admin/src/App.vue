<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import mittBus from "./global/bus/mittBus.ts";
import {isLogin} from "./util/commonUtils.ts";
import {goLogin} from "./util/routerUtils.ts";


if (isLogin()) {
  document.body.setAttribute('login', '')
  mittBus.emit("loadModule")
  mittBus.emit("loadService")
  mittBus.emit('loadUserInfo')
} else {
  document.body.removeAttribute('login')
  goLogin()
}

</script>

<template>
  <router-view></router-view>
</template>

<style scoped>

</style>
