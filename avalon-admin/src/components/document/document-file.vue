<script setup lang="ts">
import MyImage from "../image/my-image.vue";
import {getFileUploadUrl} from "../../api/env.ts";

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/04/05 19:13
 */

const props = defineProps({
  file: {
    type: Object,// {id,name,isFolder,url,size,mine,ownerId}
    default: {}
  }
})


</script>

<template>
  <div class="flex flex-col justify-center items-center cursor-pointer" style="height: 112px">
    <my-image src="/document/folder.png" v-if="file.isFolder" width="64px" height="64px" :radius="2"/>
    <template v-else>
      <my-image :src="getFileUploadUrl(file.url)" width="64px" height="64px" :radius="2"
                v-if="file.mine.includes('image')"/>
      <my-image src="/document/pdf.png" v-else-if="file.mine == 'application/pdf'" width="64" height="64"
                :radius="2"/>
      <my-image src="/document/excel.png" v-else-if="file.mine.includes('.sheet')" width="64" height="64"
                :radius="2"/>
      <my-image src="/document/word.png" v-else-if="file.mine.includes('.document')" width="64" height="64"
                :radius="2"/>
      <my-image src="/document/ppt.png" v-else-if="file.mine.includes('.presentation')" width="64" height="64"
                :radius="2"/>
      <my-image src="/document/zip.png" v-else-if="file.mine.includes('x-zip-compressed')" width="64" height="64"
                :radius="2"/>
      <my-image src="/document/document.png" v-else width="64" height="64" :radius="2"/>
    </template>
    <my-text :lineClamp="2" :width="120" text-class="break-all mt-2" :title="file.name">
      {{ file.name }}
    </my-text>
  </div>
</template>

<style scoped>

</style>