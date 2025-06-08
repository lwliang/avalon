<script setup lang="ts">
import { computed } from "vue";
import type { CardProps } from "./types";

const props = withDefaults(defineProps<CardProps>(), {
  shadow: "always",
  round: true,
  border: true,
});

const themeShadow = "shadow-md shadow-primary/10";
const hoverShadow =
  "shadow-sm hover:shadow-lg hover:shadow-primary/20 transition-shadow";

const shadowClass = computed(() =>
  props.shadow === "never"
    ? "shadow-none"
    : props.shadow === "hover"
    ? hoverShadow
    : themeShadow
);

const bgClass = "bg-background";
const borderClass = computed(() =>
  props.border ? "border border-border border-solid" : ""
);
const roundedClass = computed(() => (props.round ? "rounded" : ""));

const cardWidth = computed(() => {
  if (!props.width) return undefined;
  if (typeof props.width === "number") return { width: `${props.width}px` };
  return { width: props.width };
});
</script>

<template>
  <div
    :class="['p-4', bgClass, borderClass, roundedClass, shadowClass]"
    :style="cardWidth"
  >
    <slot />
  </div>
</template>
