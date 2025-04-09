<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2025/02/23
 */
import { ref } from "vue";
console.log('Hello,World,test');
const count = ref(1)
</script>

<template>
  <div>
    <div class="text-red-950">Hello ,World</div>
    <div>{{count}}</div>
    <MyButton @click="count++">+1</MyButton>
  </div>
</template>

<style scoped>

</style>