<script setup lang="ts">
import FormField from "../../model/FormField.ts";
import DocumentFile from "./document-file.vue";
import mittBus from "../../global/bus/mittBus.ts";
import {addModelApi, createModelApi, deleteModelApi, getModelAllApi} from "../../api/modelApi.ts";
import {ComponentInternalInstance, getCurrentInstance, ref, watch} from "vue";
import {useMouse, useEventListener, useStorage} from "@vueuse/core";
import {getUserId} from "../../cache/userStorage.ts";
import MyDialog from "../dialog/my-dialog.vue";
import MyInput from "../input/my-input.vue";
import {postDownloadFileFromFileServer} from "../../api/http.ts";

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
    fileSelectedIndex.value = 0
    fileSelectedTag.value = null
    createFolderShow.value = true
    createFileShow.value = false
    openFolderShow.value = false
    menuPosition.value.x = x.value - 90;
    menuPosition.value.y = y.value - 90;
}

useEventListener('click', () => {
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
const showFileMenu = (index: number) => {
    createFolderShow.value = false
    fileSelectedIndex.value = index
    menuPosition.value.x = x.value - 90;
    menuPosition.value.y = y.value - 90;
    fileSelectedTag.value = formField.value.value[index]
    if (fileSelectedTag.value.isFolder) {
        openFolderShow.value = true
    } else {
        createFileShow.value = true
    }

}

const openFolderShow = ref(false) // 右击文件夹时打开
const openFolderClick = () => {
    openFolderShow.value = false
    parentId.value = fileSelectedTag.value.id
    getAllFileByParentId()
}

const openFileClick = (index: number) => {
    if (formField.value.value[index].isFolder) {
        parentId.value = formField.value.value[index].id
        getAllFileByParentId()
    }
}

const uploadFileClick = () => {

}
const uploadFolderClick = () => {

}
</script>

<template>
    <div class="flex items-start  flex-wrap gap-2 min-h-full" style="align-content: flex-start"
         @contextmenu.prevent.stop="showListMenu">
        <div v-for="(file,index) in formField.value" :key="index" class="inline-block" @click="openFileClick(index)"
             @contextmenu.prevent.stop="showFileMenu(index)">
            <document-file :file="file"/>
        </div>
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