<script setup lang="ts">
import ChatMember from "./ChatMember.ts";
import {onBeforeUnmount, onMounted, ref} from "vue";
import FormField from "../../model/FormField.ts";
import MyInput from "../input/my-input.vue";
import MyIcon from "../icon/my-icon.vue";
import MyImage from "../image/my-image.vue";
import chatMessage from "./chat-message.vue";
import {getFileUploadUrl} from "../../api/env.ts";

const members = defineModel({
    type: FormField,
    required: true
})

const search = ref(new FormField(''))
const activeChatMember = ref<ChatMember | null>()

const chatItemClick = (member: ChatMember) => {
    chatKey.value++;
    activeChatMember.value = null
    activeChatMember.value = member
}
const chatKey = ref(1)

const webSocketMessageHandler = (message: any) => {
    const fromUserId = message.fromUserId;

    const obj = members.value.value.find((x: any) => fromUserId == x.imUserId)
    if (obj) {
        obj['lastMsgId'] = message;
    }
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
    <div class="flex h-full overflow-hidden">
        <div class="w-[240px] px-2 flex flex-col flex-shrink-0">
            <div>
                <my-input suffix-icon-style="fas" suffixIcon="search" v-model="search"/>
            </div>
            <div class="flex-1 overflow-y-auto overflow-x-hidden">
                <div v-for="member in members.value" :key="member.id"
                     class="flex items-center gap-2 flex-shrink-0 cursor-pointer py-1 bg-white px-2"
                     @click="chatItemClick(member)">
                    <my-image width="35" height="35" :radius="5" :src="getFileUploadUrl(member.avatar)"/>
                    <div class="">
                        <div class="truncate flex-1">
                            {{ member.name }}
                        </div>
                        <div v-if="member.lastMsgId" class="text-gray-500 text-sm">
                            {{ member.lastMsgId.msgType == 'Text' ? member.lastMsgId.content : '[图片]' }}
                        </div>
                    </div>

                </div>
            </div>
        </div>
        <div class="flex-1 flex flex-col flex-shrink-0">
            <div class="flex-1 overflow-auto">
                <chat-message :key="chatKey" v-if="activeChatMember" :to-im-user-id="activeChatMember.imUserId"
                              :to-user-id="activeChatMember.id" :to-name="activeChatMember.name"/>
            </div>
        </div>
    </div>
</template>

<style scoped>

</style>