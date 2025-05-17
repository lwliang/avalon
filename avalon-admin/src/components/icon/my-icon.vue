<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {iconSizeType, iconStyleType} from "./my-icon.ts";
import {FontAwesomeIcon, FontAwesomeLayers} from "@fortawesome/vue-fontawesome";


defineProps({
    icon: String,
    size: {
        type: iconSizeType,
        default: '1x'
    },
    color: String,
    type: {
        type: iconStyleType,
        default: 'far'
    }
})
</script>

<template>
  <font-awesome-layers>
    <font-awesome-icon class="bg-transparent" :icon="[type,icon]" :size="size" :style="{'color': color}"/>
  </font-awesome-layers>
</template>

<style scoped>
</style>