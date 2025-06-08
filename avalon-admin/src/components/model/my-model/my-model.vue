<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/13 15:15
 */

import MyIcon from "../../icon/my-icon.vue";
import MyButton from "../../button/my-button.vue";
import MyOverlay from "../../overlay/my-overlay.vue";

const props = defineProps<{
  show: boolean,
  title: string,
  modelClass?: string,
}>()

const emit = defineEmits(['close', 'sure'])

const closeClick = () => {
  emit('close')
}
const sureClick = async () => {
  emit('sure')
}
</script>

<template>
  <MyOverlay :show="props.show">
    <div :class="['absolute-center flex flex-col bg-background',modelClass]">
      <div class="model-head">
        <div class="dialog-title flex-1 pb-4 font-bold">
          {{ title }}
        </div>
        <div class="dialog-close pr-2">
          <MyIcon class="cursor-pointer" icon="xmark" type="fas" @click="closeClick"></MyIcon>
        </div>
      </div>
      <div class="model-content flex-1 ">
        <slot></slot>
      </div>
      <div class="model-footer pt-3">
        <MyButton type="info" rounded @click="closeClick">取消</MyButton>
        <MyButton class="ml-3" type="primary" rounded @click="sureClick">确认</MyButton>
      </div>
    </div>
  </MyOverlay>
</template>

<style scoped>

</style>