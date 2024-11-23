<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import mittBus from "./global/bus/mittBus.ts";
import {isLogin} from "./util/commonUtils.ts";


if (isLogin()) {
    mittBus.emit("loadMenu")
    mittBus.emit("loadModule")
    mittBus.emit("loadService")
    mittBus.emit("loadField")
    mittBus.emit('loadUserInfo')
    console.log("App vue")
}

</script>

<template>
    <router-view></router-view>
</template>

<style scoped>

</style>
