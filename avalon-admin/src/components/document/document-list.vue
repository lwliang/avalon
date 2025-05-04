<script setup lang="ts">
import FormField from "../../model/FormField.ts";
import DocumentFile from "./document-file.vue";
import mittBus from "../../global/bus/mittBus.ts";
import {addModelApi, createModelApi, deleteModelApi, editModelApi, getModelAllApi} from "../../api/modelApi.ts";
import {ComponentInternalInstance, getCurrentInstance, ref, watch} from "vue";
import {useMouse, useEventListener, useStorage} from "@vueuse/core";
import {getUserId} from "../../cache/userStorage.ts";
import MyDialog from "../dialog/my-dialog.vue";
import MyInput from "../input/my-input.vue";
import {postDownloadFileFromFileServer} from "../../api/http.ts";
import draggable from "vuedraggable";

const {proxy} = getCurrentInstance() as ComponentInternalInstance;

const parentId = ref()
watch(() => parentId.value, (newId) => {
    parentIdState.value = newId
})
const parentIdState = useStorage('fileParentId', 0) // returns Ref<number>
if (parentIdState.value) {
    parentIdState.value = 0
}
const getAllFileByParentId = () => {
    let condition = ""
    if (parentId.value) {
        condition = `('parentId',=,${parentId.value})`
    }
    getModelAllApi("id,name,isFolder,url,size,mine,ownerId", condition, "document.file").then(data => {
        formField.value.value.splice(0, formField.value.value.length)
        formField.value.value.push(...data)
    })
}

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/04/05 16:36
 */
const formField = defineModel({
    type: FormField,
    required: true
})
// {id,name,isFolder,url,size,mine,ownerId}

mittBus.on('uploadFile', async () => {
    console.log('upload-file')
    if (!parentId.value) {
        const defaultValue = await createModelApi({}, "document.show.transient")
        formField.value.value.splice(0, formField.value.value.length)
        formField.value.value.push(...(defaultValue.documents))
    } else {
        getAllFileByParentId()
    }
})

const createFolderShow = ref(false) // 创建文件夹
const {x, y} = useMouse()

const menuPosition = ref({x: 0, y: 0})

const showListMenu = () => {
    console.log('showListMenu true')
    fileSelectedIndex.value = 0
    fileSelectedTag.value = null
    createFolderShow.value = true
    createFileShow.value = false
    openFolderShow.value = false
    menuPosition.value.x = x.value - 90;
    menuPosition.value.y = y.value - 90;
}

useEventListener('click', () => {
    console.log('click true')
    createFolderShow.value = false
    createFileShow.value = false
    openFolderShow.value = false
    fileSelectedIndex.value = 0
    fileSelectedTag.value = null
})

const createFolderClick = async () => {
    createFolderNameShow.value = true
}
const createFolderNameShow = ref(false)
const hideClick = () => {
    createFolderNameShow.value = false
}
const sureClick = async () => {
    if (!folderName.value.value || !folderName.value.value.toString().trim()) {
        proxy?.$notify.error('提示', '输入文件夹名称')
        return
    }
    const value: any = {
        isFolder: true,
        name: folderName.value.value,
        ownerId: getUserId()
    }
    if (parentId.value) {
        value.parentId = parentId.value
    }
    await addModelApi(value, "document.file")
    createFolderNameShow.value = false
    folderName.value.value = ''
    mittBus.emit('uploadFile')
}
const folderName = ref(new FormField(""))

const createFileShow = ref(false) // 下载文件

const downloadFileClick = async () => {
    const fileNameTemp = fileSelectedTag.value.name;
    postDownloadFileFromFileServer(fileSelectedTag.value.url, {}).then((response) => {
        // 创建一个 URL 对象
        const url = window.URL.createObjectURL(new Blob([response.data]));

        // 创建一个临时的下载链接
        const link = document.createElement('a');
        link.href = url;
        // 获取 Content-Disposition 响应头
        const contentDisposition = response.headers.get('Content-Disposition');

        // 提取文件名
        let fileName = fileNameTemp
        if (contentDisposition && contentDisposition.includes('filename=')) {
            const fileNameMatch = contentDisposition.match(/filename="?(.+)"?/);
            if (fileNameMatch.length > 1) {
                fileName = decodeURIComponent(fileNameMatch[1]);
            }
        }
        // 文件名设置为导出的文件名
        link.setAttribute('download', fileName);

        // 触发下载
        document.body.appendChild(link);
        link.click();

        // 清理 URL 对象
        document.body.removeChild(link);
        window.URL.revokeObjectURL(url);
        proxy?.$notify.success('提示', "下载完成")
    })
}

const deleteFileClick = async () => {
    deleteModelApi(fileSelectedTag.value.id, "document.file").then(() => {
        mittBus.emit('uploadFile')
    })
}

const deleteFolderClick = async () => {
    deleteModelApi(fileSelectedTag.value.id, "document.file").then(() => {
        mittBus.emit('uploadFile')
    })
}

const fileSelectedIndex = ref(0)
const fileSelectedTag = ref<any>(null)
const showFileMenu = (id: number) => {
    createFolderShow.value = false
    const index = formField.value.value.findIndex((x: any) => x.id == id);
    fileSelectedIndex.value = index
    menuPosition.value.x = x.value - 90;
    menuPosition.value.y = y.value - 90;
    fileSelectedTag.value = formField.value.value[index]
    if (fileSelectedTag.value.isFolder) {
        openFolderShow.value = true
        createFileShow.value = false
    } else {
        createFileShow.value = true
        openFolderShow.value = false
    }
}

const openFolderShow = ref(false) // 右击文件夹时打开
const openFolderClick = () => {
    openFolderShow.value = false
    parentId.value = fileSelectedTag.value.id
    getAllFileByParentId()
}

const openFileClick = (id: number) => {
    const doc = formField.value.value.find((x: any) => x.id == id);
    if (doc && doc.isFolder) {
        parentId.value = doc.id
        getAllFileByParentId()
    }
}

const activeFileId = ref(0)
const activeFileClick = (id: number) => {
    activeFileId.value = id
}

const uploadFileClick = () => {

}
const uploadFolderClick = () => {

}

const dragging = ref(false)

const onDragEnd = (evt: any) => {
    dragging.value = false
    if (!evt.to) return // 未移动

    if (!dragIntoItem.value) {
        return;
    }
    const src = evt.item.__draggable_context.element

    // 调整目录
    let newSrc = {id: src.id, parentId: dragIntoItem.value}

    editModelApi(newSrc, "document.file").then(() => {
        const index = formField.value.value.findIndex((x: any) => x.id == src.id)
        formField.value.value.splice(index, 1)
    })
}
const onDragStart = (evt: any) => {
    dragging.value = true
    const currentElement = evt.item.__draggable_context.element
    if (currentElement.id != activeFileId.value) {
        activeFileId.value = currentElement.id
    }
}
const dragIntoItem = ref(0)

const ondragenter = (element: any) => {
    if (element && element.isFolder) {
        dragIntoItem.value = element.id
    }
}
const onDragOver = (element: any) => {
    const target = element
    if (target && target.isFolder) {
        dragIntoItem.value = target.id
    }
}
const ondragleave = (element: any) => {
    dragIntoItem.value = 0
}
</script>

<template>
    <div class="min-h-full"
         @contextmenu.prevent.stop="showListMenu">
        <draggable :list="formField.value" class="flex items-start  flex-wrap gap-2 min-h-full" item-key="id"
                   group="files" :sort="false"
                   @start="onDragStart"
                   @end="onDragEnd">
            <template #item="{ element }">
                <div
                    :class="['inline-block hover:bg-gray-200 p-2 rounded-lg',{'bg-gray-200':activeFileId==element.id || dragIntoItem==element.id}]"
                    @click="activeFileClick(element.id)"
                    @dblclick="openFileClick(element.id)"
                    @dragover="onDragOver(element)"
                    @dragleave="ondragleave(element)"
                    @dragenter="ondragenter(element)"
                    @contextmenu.prevent.stop="showFileMenu(element.id)">
                    <document-file :file="element"/>
                </div>
            </template>
        </draggable>
        <div v-if="!formField.value.length">
            暂无文件
        </div>
        <div v-if="createFolderShow" class="context-menu"
             :style="{top:`${menuPosition.y}px`,left:`${menuPosition.x}px`}">
            <div class="px-2 py-1 cursor-pointer" @click="createFolderClick">创建文件夹</div>
            <div class="px-2 py-1 cursor-pointer" @click="uploadFileClick" hidden>上传文件</div>
            <div class="px-2 py-1 cursor-pointer" @click="uploadFolderClick" hidden>上传文件夹</div>
        </div>
        <div v-if="createFileShow" class="context-menu"
             :style="{top:`${menuPosition.y}px`,left:`${menuPosition.x}px`}">
            <div class="px-2 py-1 cursor-pointer" @click="downloadFileClick">下载</div>
            <div class="px-2 py-1 cursor-pointer" @click="deleteFileClick">删除</div>
        </div>
        <div v-if="openFolderShow" class="context-menu"
             :style="{top:`${menuPosition.y}px`,left:`${menuPosition.x}px`}">
            <div class="px-2 py-1 cursor-pointer" @click="deleteFolderClick">删除</div>
        </div>
    </div>
    <MyDialog :show="createFolderNameShow" @close="hideClick" @sure="sureClick" title="输入文件夹名称">
        <MyInput ref="db_input" v-model="folderName" :required="true"></MyInput>
    </MyDialog>
</template>

<style scoped>
.context-menu {
    position: absolute;
    background-color: #fff;
    border: 1px solid #ccc;
    border-radius: 4px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
    z-index: 1000;
    width: 150px;
}
</style>