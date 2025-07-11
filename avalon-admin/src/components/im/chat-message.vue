<script setup lang="ts">
import {ref, onMounted, onBeforeUnmount} from "vue";
import ChatMessage from "../../model/im/ChatMessage.ts";
import TextMessage from "./text-message.vue";
import {getImUserIdCache, getPageMessage} from "../../ws/wsAPI.ts";
import MyTextarea from "../textarea/my-textarea.vue";
import MyIcon from "../icon/my-icon.vue";
import {uploadImage} from "../../api/fileUploadApi.ts";
import ImageMessage from "./image-message.vue";
import {getFileUploadUrl} from "../../api/env.ts";
import {useScroll, useEventListener} from "@vueuse/core";

const messages = ref<ChatMessage[]>([])
const props = defineProps({
    toImUserId: Number, // 聊天对象id
    toUserId: Number,
    toName: String,
})
const imUserId = ref()
const pageNum = ref(1)
const pageSize = ref(10)
const message_container = ref<HTMLElement | null>(null)
const init = ref(true)
getImUserIdCache().then(im => {
    imUserId.value = im
    init.value = true;
    initPageMessage()
})
const scrollLastY = ref(-1000)

useEventListener(message_container, 'scroll', () => {
    const {y} = useScroll(message_container)
    if (scrollLastY.value) {
        if (y.value < scrollLastY.value) {
            if (y.value <= 50) {
                loadPageMessage()
            }
        }
    }
    scrollLastY.value = y.value
})

const scrollToBottom = () => {
    if (!message_container.value) return

    message_container.value.scrollTo({
        top: message_container.value.scrollHeight,
        behavior: 'smooth',// 平滑效果
    })
}

const initPageMessage = () => {
    if (!imUserId.value) return
    if (props.toImUserId) {
        getPageMessage(imUserId.value, props.toImUserId, pageNum.value, pageSize.value).then(data => {
            if (data.length >= pageSize.value) {
                pageNum.value++
                if (!checkScroll()) {
                    initPageMessage()
                }
            }
            data = data.reverse()
            messages.value.splice(0, 0, ...data)
            scrollToBottom()
        })
    }
}

const loadPageMessage = () => {
    if (!imUserId.value) return
    if (props.toImUserId) {
        getPageMessage(imUserId.value, props.toImUserId, pageNum.value, pageSize.value).then(data => {
            if (data.length >= pageSize.value) {
                pageNum.value++
            }
            data = data.reverse()
            messages.value.splice(0, 0, ...data)
        })
    }
}


const checkScroll = () => {
    if (message_container.value) {
        return message_container.value.scrollHeight > message_container.value.clientHeight;// 有垂直滚动条
    }
    return false;
}

const inputMessage = ref('')

const inputMessageHandler = (content: string) => {
    if (!content) {
        inputMessage.value = ''
        return
    }
    if (window.ws) {
        window.ws.sendSingleText(imUserId.value, props.toImUserId, content).then((msg: any) => {
            inputMessage.value = ''
            messages.value.push(msg)
        })
    }
}


const inputImageClick = () => {
    if (file.value) {
        file.value.click();
    }
}
const file = ref<HTMLElement | null>(null)

const fileBlob = ref<File>()

const fileChange = (event: any) => {
    if (event.target.files.length === 0) {
        return
    }
    fileBlob.value = event.target.files[0]
    uploadImage(event.target.files[0]).then(data => {
        if (window.ws) {
            window.ws.sendSingleImage(imUserId.value, props.toImUserId,
                getFileUploadUrl(data.url)).then((msg: any) => {
                messages.value.push(msg)
            })
        }
    })
}

const webSocketMessageHandler = (message: ChatMessage) => {
    messages.value.push(message)
}
let msgEventTimeId: any = null;
onMounted(() => {
    msgEventTimeId = setInterval(() => {
        if (window.ws) {
            window.ws.addMessageEvent(webSocketMessageHandler)
            clearInterval(msgEventTimeId)
            msgEventTimeId = null;
        }
    }, 1000)

})

onBeforeUnmount(() => {
    if (window.ws) {
        window.ws.removeMessageEvent(webSocketMessageHandler)
    }
})
</script>

<template>
    <div class="flex flex-col flex-shrink-0 h-full overflow-hidden">
        <div>
            {{ toName }}
        </div>
        <div class="flex-1 overflow-y-auto flex flex-col" ref="message_container">
            <div v-for="(msg,index) in messages" :key="index">
                <template v-if="msg.msgType == 'Text'">
                    <text-message class="my-2" :msg-id="msg.id" :from-im-id="msg.fromUserId" :to-im-id="msg.toUserId"
                                  :text="msg.content" :left="msg.toUserId == imUserId"></text-message>
                </template>
                <template v-if="msg.msgType == 'Image'">
                    <image-message class="my-2" :msg-id="msg.id" :from-im-id="msg.fromUserId" :to-im-id="msg.toUserId"
                                   :content="msg.content" :left="msg.toUserId == imUserId"/>
                </template>
            </div>
        </div>
        <div class="relative">
            <my-textarea :pt="20" v-model="inputMessage" @inputMessage="inputMessageHandler"/>

            <MyIcon @click="inputImageClick" type="fas" icon="image"
                    class="cursor-pointer absolute top-1 left-[1rem]"/>
            <input accept="image/*" type="file" hidden="hidden" ref="file" @change="fileChange">
        </div>
    </div>
</template>

<style scoped>

</style>