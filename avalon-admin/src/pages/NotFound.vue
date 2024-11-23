<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {goModelParent} from "../util/routerUtils.ts";

setTimeout(() => {
    goModelParent()
}, 5000)
</script>

<template>
    <h1>路径不存在</h1>
    <h2>即将跳转</h2>
</template>

<style scoped>

</style>