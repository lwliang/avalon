<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2025/04/06 11:47
 */

import MyIcon from "../icon/my-icon.vue";
import MyButton from "../button/my-button.vue";
import './my-dialog.css'
import {ref} from "vue";
import {uploadFile} from "../../api/fileUploadApi.ts";
import FileUploadRes from "../../model/FileUploadRes.ts";
import {getUserId} from "../../cache/userStorage.ts";
import {addModelApi, createModelApi} from "../../api/modelApi.ts";
import myNotification from "../notification/index.ts"
import mittBus from "../../global/bus/mittBus.ts";
import {useStorage} from '@vueuse/core'

const parentIdState = useStorage('fileParentId', 0) // returns Ref<number>
const props = defineProps({
    zIndex: {
        type: Number,
        default: 2000
    },
    title: {
        type: String,
        default: "标题"
    },
    show: {
        type: Boolean,
        default: false
    },
    closeCallback: {
        type: Function
    },
    sureCallback: {
        type: Function
    }
})

const emit = defineEmits(['close', 'sure'])
const file_upload_ctrl = ref<HTMLInputElement | null>(null)

const closeClick = () => {
    emit('close')
    if (props.closeCallback) {
        props.closeCallback()
    }
}

const sureClick = async () => {
    convertFileToUploadFile()
    await uploadFileRecursion(uploadFiles.value, parentIdState.value)
    myNotification.success("提示", "上传成功")
    mittBus.emit('uploadFile')
    emit('sure')
    if (props.sureCallback) {
        props.sureCallback()
    }
}

const uploadFileRecursion = async (files: UploadFile[], parentId: number) => {
    if (!files || files.length == 0) return
    for (let filesKey of files) {
        if (!filesKey.file) { // 有父级
            const value: any = {
                isFolder: true,
                name: filesKey.dir,
                ownerId: getUserId()
            }
            if (parentId) {
                value.parentId = parentId
            }
            const res: any = await addModelApi(value, "document.file")
            filesKey.id = res.id
            if (filesKey.children) {
                await uploadFileRecursion(filesKey.children, res.id)
            }
        } else { // 是文件
            const res = await uploadFile(filesKey.file) as FileUploadRes
            const value: any = {
                isFolder: false,
                url: res.url,
                size: res.size,
                name: filesKey.fileName ? filesKey.fileName : res.originName,
                mine: res.mine,
                ownerId: getUserId()
            }
            if (parentId) {
                value.parentId = parentId
            }
            await addModelApi(value, "document.file")
            filesKey.done = true
            if (filesKey.belong) {
                filesKey.belong.done = true
            }
        }
    }
}

const uploadFileClick = () => {
    webkitdirectory.value = false
    setTimeout(() => {
        if (file_upload_ctrl.value) {
            file_upload_ctrl.value.click()
        }
    }, 100)
}

const uploadDirClick = () => {
    webkitdirectory.value = true
    setTimeout(() => {
        if (file_upload_ctrl.value) {
            file_upload_ctrl.value.click()
        }
    }, 100)
}

const webkitdirectory = ref(false) // 上传目录

interface UploadFile {
    file: File | null,
    fileName?: string
    done: boolean,
    dir?: string
    id?: number
    children?: UploadFile[],
    belong?: UploadFile
}

// 上传文件或文件夹
const files = ref<UploadFile[]>([])
const uploadFiles = ref<UploadFile[]>([])
const handleFileChange = (event: any) => {
    const fileList = event.target.files;
    for (const file of fileList) {
        files.value.push({file: file, done: false})
    }
}
const convertFileToUploadFile = () => {
    uploadFiles.value.splice(0, uploadFiles.value.length)
    for (const file of files.value) {
        if (!file.file) continue
        if (file.file.webkitRelativePath) { // 目录文件，最后一个是文件，之前都是目录
            const pathParts = file.file.webkitRelativePath.split("/")
            let buff: UploadFile[] | undefined = uploadFiles.value
            let result: UploadFile | undefined = undefined
            for (let i = 0; i < pathParts.length; i++) {
                const path = pathParts[i]
                if (!buff) continue
                if (i == pathParts.length - 1) { // 是文件
                    if (result) {
                        result.children?.push({file: file.file, done: false, belong: file, fileName: path})
                    }
                } else { // 目录
                    result = buff.find((x: UploadFile) => x.dir == path)
                    if (result) {
                        buff = result.children
                    } else { // 不存在则创建目录
                        result = {file: null, done: false, dir: path, children: []}
                        buff.push(result)
                    }
                }
            }
        } else { // 是文件
            uploadFiles.value.push({file: file.file, done: false, belong: file})
        }
    }
}

const deleteFileClick = (index: number) => {
    files.value.splice(index, 1)
}
</script>

<template>
    <teleport to="body">
        <div class="dialog-overlay" :class="zIndex" v-if="show">
            <div class="dialog w-[500px] p-4">
                <div class="dialog-header flex">
                    <div class="dialog-title flex-1">
                        {{ title }}
                    </div>
                    <div class="dialog-close pr-2">
                        <MyIcon class="cursor-pointer" icon="xmark" type="fas" @click="closeClick"></MyIcon>
                    </div>
                </div>
                <div class="dialog-content">
                    <div>
                        <MyButton class="ml-3" type="primary" rounded @click="uploadFileClick">上传文件</MyButton>
                        <MyButton class="ml-3" type="primary" rounded @click="uploadDirClick">上传目录</MyButton>
                        <input ref="file_upload_ctrl" type="file" multiple hidden="hidden"
                               :webkitdirectory="webkitdirectory" @change="handleFileChange">
                    </div>
                    <div>
                        <div v-for="(file,index) in files" :key="index" class="flex justify-center cursor-pointer">
                            <div class="mr-2" v-if="file.done">
                                <MyIcon icon="check" type="fas" color="#00FF00"></MyIcon>
                            </div>
                            <div class="flex-1">{{
                                    file.file?.webkitRelativePath ? file.file.webkitRelativePath : file.file?.name
                                }}
                            </div>
                            <div @click="deleteFileClick(index)">
                                <MyIcon icon="trash-can" type="fas" color="#000"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="dialog-footer flex justify-end">
                    <template v-if="$slots.footer">
                        <slot name="footer"></slot>
                    </template>
                    <template v-else>
                        <MyButton type="info" rounded @click="closeClick">取消</MyButton>
                        <MyButton class="ml-3" type="primary" rounded @click="sureClick">上传</MyButton>
                    </template>
                </div>
            </div>
        </div>
    </teleport>
</template>

<style scoped>

</style>