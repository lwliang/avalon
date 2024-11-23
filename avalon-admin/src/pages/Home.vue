<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {useRouteStore} from "../global/store/routeStore.ts";
import {goModelParent} from "../util/routerUtils.ts";
import {isLogin} from "../util/commonUtils.ts";

const routeStore = useRouteStore()

if (isLogin()) {
    if (!routeStore.currentRoute || routeStore.currentRoute == "/") {
        goModelParent()
    }
}

</script>

<template>

</template>

<style scoped>

</style>