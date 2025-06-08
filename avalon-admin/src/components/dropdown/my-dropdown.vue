<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {PlacementType, TriggerType} from "../popover/types.ts";
import MyPopover from "../popover/my-popover.vue";

const props = withDefaults(defineProps<{
  trigger?: TriggerType;
  placement?: PlacementType,
  teleported?:boolean
}>(), {
  trigger: 'hover',
  placement: 'bottom',
  teleported: true
})
</script>

<template>
  <MyPopover ref="popper" placement="bottom" :trigger="trigger" popper-class="px-0 py-1" :teleported="teleported">
    <template #default>
      <div class="flex cursor-pointer items-center">
        <slot name="default"></slot>
      </div>
    </template>
    <template #content>
      <slot name="dropdown"></slot>
    </template>
  </MyPopover>
</template>

<style scoped>

</style>