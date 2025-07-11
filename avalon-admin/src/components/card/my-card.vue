<script setup lang="ts">
import { computed } from "vue";
import type { CardProps } from "./types";

const props = withDefaults(defineProps<CardProps>(), {
  shadow: "always",
  round: true,
  border: true,
});

// 将自定义属性映射到 el-card 属性
const cardProps = computed(() => ({
  shadow: props.shadow,
  bodyStyle: {
    padding: '16px'
  }
}));

const cardClass = computed(() => {
  const classes = [];
  
  if (props.round) {
    classes.push('rounded');
  }
  
  if (props.border) {
    classes.push('border border-border border-solid');
  }
  
  return classes.join(' ');
});

const cardWidth = computed(() => {
  if (!props.width) return undefined;
  if (typeof props.width === "number") return { width: `${props.width}px` };
  return { width: props.width };
});
</script>

<template>
  <el-card
    v-bind="cardProps"
    :class="cardClass"
    :style="cardWidth"
  >
    <slot />
  </el-card>
</template>
