<script setup lang="ts">
import {ref, onMounted} from "vue";
import FormField from "../../model/FormField.ts";
import Vditor from "vditor";
import "Vditor/dist/index.css";
import {uploadImage} from "../../api/fileUploadApi.ts";
import {getFilePrefix} from "../../api/env.ts";

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/04/20 7:23
 */
const props = defineProps({
    htmlId: String,
    htmlName: String,
    required: Boolean,
    readonly: Boolean
})

const formField = defineModel({
    type: FormField,
    required: true
})

const markdown = ref(null)
const vditor = ref<Vditor>()

onMounted(() => {
    if (markdown.value) {
        vditor.value = new Vditor(markdown.value, {
            height: '100%',
            mode: 'wysiwyg',
            placeholder: "在这里输入 Markdown 内容...",
            toolbar: [
                "headings",
                "bold",
                "italic",
                "strike",
                "|",
                "list",
                "ordered-list",
                "check",
                "|",
                "code",
                "quote",
                "table",
                "upload",
                "|",
                "preview",
                "fullscreen"
            ],
            value: formField.value.value || '',
            upload: {
                accept: "image/*",
                handler: async (files: File[]): Promise<string> => {
                    for (let file of files) {
                        const image = await uploadImage(file)
                        if (vditor.value) {
                            vditor.value.insertValue(`![图片描述](${getFilePrefix() + image.url})`)
                        }
                    }
                    return ''
                }
            },
            input: (value: string) => {
                formField.value.value = value
            },
            cache: {
                enable: false
            }
        })
    }

})

</script>

<template>
    <div ref="markdown" class="markdown h-full" :id="htmlId" style="min-height: 300px;">

    </div>
</template>

<style scoped>

</style>