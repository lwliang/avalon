<script setup lang="ts">
import type {DividerProps} from './types.ts'
import {computed} from 'vue';

const props = defineProps<DividerProps>();

const isVertical = computed(() => props.direction === 'vertical');

const borderColorClass = computed(() => {
  switch (props.theme) {
    case 'primary':
      return isVertical.value ? "border-l border-r-0 border-primary" : 'border-t border-b-0 border-primary';
    case 'success':
      return isVertical.value ? "border-l border-r-0 border-success" : 'border-t border-b-0 border-success';
    case 'warning':
      return isVertical.value ? "border-l border-r-0 border-warning" : 'border-t border-b-0 border-warning';
    case 'danger':
      return isVertical.value ? "border-l border-r-0 border-danger" : 'border-t border-b-0 border-danger';
    case 'info':
      return isVertical.value ? "border-l border-r-0 border-info" : 'border-t border-b-0 border-info';
    case 'border':
    default:
      return isVertical.value ? "border-l border-r-0 border-border" : 'border-t border-b-0 border-border';
  }
});

const textColorClass = computed(() => {
  switch (props.theme) {
    case 'primary':
      return 'text-primary';
    case 'success':
      return 'text-success';
    case 'warning':
      return 'text-warning';
    case 'danger':
      return 'text-danger';
    case 'info':
      return 'text-info';
    default:
      return 'text-text-regular';
  }
});

const textSizeClass = computed(() => {
  switch (props.textSize) {
    case 'lg':
      return 'text-lg';
    case 'sm':
      return 'text-sm';
    default:
      return 'text-default';
  }
});

const dividerBaseClass = computed(() => [
  'flex',
  isVertical.value ? 'flex-col items-center' : 'items-center w-full',
  'relative'
]);

const dividerLineClass = computed(() => [
  isVertical.value ? 'h-full' : 'flex-1',
  borderColorClass.value,
  'border-solid',
  isVertical.value ? 'mx-2' : 'my-2'
]);

const dividerTextClass = computed(() => [
  'px-3',
  textColorClass.value,
  textSizeClass.value,
  'bg-background'
]);

const borderStyleInline = computed(() => props.borderStyle ? {borderStyle: props.borderStyle} : {});
</script>

<template>
  <div :class="dividerBaseClass">
    <template v-if="isVertical">
      <div :class="dividerLineClass" :style="borderStyleInline"></div>
      <span v-if="$slots.default" :class="dividerTextClass">
        <slot/>
      </span>
      <div :class="dividerLineClass" :style="borderStyleInline"></div>
    </template>
    <template v-else>
      <div
          v-if="!contentPosition || contentPosition === 'center'"
          :class="dividerLineClass"
          :style="borderStyleInline"
      ></div>
      <div v-if="contentPosition === 'left'" class="w-2"></div>
      <span
          v-if="$slots.default"
          :class="[dividerTextClass, {
          'order-1': contentPosition === 'right',
          'order-0 mx-auto': !contentPosition || contentPosition === 'center',
          'mr-auto': contentPosition === 'left',
          'ml-auto': contentPosition === 'right'
        }]"
      >
        <slot/>
      </span>
      <div
          v-if="contentPosition === 'left'"
          :class="dividerLineClass"
          :style="borderStyleInline"
      ></div>
      <div v-if="contentPosition === 'right'" class="w-2"></div>
      <div
          v-if="contentPosition === 'right'"
          :class="dividerLineClass"
          :style="borderStyleInline"
      ></div>
      <div
          v-if="!contentPosition || contentPosition === 'center'"
          :class="dividerLineClass"
          :style="borderStyleInline"
      ></div>
    </template>
  </div>
</template>
