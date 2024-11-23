<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {iconSizeType, iconStyleType} from "./my-icon.ts";
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";


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
    <font-awesome-icon :icon="[type,icon]" :size="size" :style="{fill:color}"/>
</template>

<style scoped>

</style>