<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import './my-image.css'
import {isAbsoluteUrl} from "../../util/commonUtils.ts";
import {getFilePrefix} from "../../api/env.ts";

const props = defineProps({
  src: String,
  alt: {
    type: String,
    default: '未找到图片'
  },
  width: String,
  height: String,
  radius: {
    type: Number,
    default: 0
  }
})

// const getImageUrl = () => {
//     if (props.src) {
//         if (isAbsoluteUrl(props.src)) {
//             return props.src
//         }
//
//         return getFilePrefix() + props.src
//     }
//     return props.src
// }

</script>

<template>
  <template v-if="src">
    <img :style="{'border-radius':radius + 'px', height:`${height}px`}" :src="src" :alt="alt" :width="width"
         :height="height">
  </template>
  <template v-else>
    <img :style="{'border-radius':radius + 'px', height:`${height}px`}" src="/no-image.png" :alt="alt" :width="width"
         :height="height">
  </template>
</template>

<style scoped>

</style>