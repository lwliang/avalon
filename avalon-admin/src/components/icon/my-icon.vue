<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {IconFlipType, IconSizeType, IconType} from "./types.ts";
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import {useAttrs} from "@vue/runtime-dom";
import {computed} from "vue";

// 使用 withDefaults 设置默认值
const props = withDefaults(
    defineProps<{
      /**
       * 类型（'fas' | 'far' | 'fab'），与 icon 组合用
       */
      type?: IconType;
      /**
       * 图标定义，支持字符串或数组格式
       * - 字符串：'user'
       * - 数组：['fas', 'user']
       */
      icon: string | [string, string];
      /**
       * 尺寸：'xs' | 'sm' | 'lg' | '1x'...'10x'
       */
      size?: IconSizeType;
      /**
       * 颜色
       */
      color?: string;
      /**
       * 是否旋转动画
       */
      spin?: boolean;
      /**
       * 旋转角度：90/180/270
       */
      rotation?: 90 | 180 | 270 | "90" | "180" | "270";
      /**
       * 翻转：'horizontal' | 'vertical' | 'both'
       */
      flip?: IconFlipType;
      /**
       * 直接设置 font-size (px/rem...)
       */
      fontSize?: string;
    }>(),
    {
      size: "1x", // 设置默认值为 "1x"
    }
);

const attrs = useAttrs()

const finalIcon = computed(() => {
  // 如果是数组，直接返回
  if (Array.isArray(props.icon)) return props.icon
  // 如果有type，用[type, icon]
  if (props.type) return [props.type, props.icon]
  // 默认 solid
  return ['fas', props.icon]
})
</script>

<template>
  <font-awesome-icon
      :icon="finalIcon"
      :size="size"
      :spin="spin"
      :color="color"
      :rotation="rotation"
      :flip="flip"
      :style="{ fontSize }"
      v-bind="attrs"
  />
</template>

<style scoped>
</style>