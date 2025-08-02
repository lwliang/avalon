<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {useRouteStore} from "../global/store/routeStore.ts";
import {goLogin, goModelParent} from "../util/routerUtils.ts";
import {isLogin} from "../util/commonUtils.ts";

const routeStore = useRouteStore()


if (isLogin()) {
    if (!routeStore.currentRoute || routeStore.currentRoute == "/") {
        goModelParent()
    }
} else {
  if(routeStore.currentRoute == "/") {
    goLogin()
  }
}
</script>

<template>

</template>

<style scoped>

</style>