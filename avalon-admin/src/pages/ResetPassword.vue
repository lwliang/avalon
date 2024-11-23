<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import MyButton from "../components/button/my-button.vue";
import MyDivider from "../components/divider/my-divider.vue";
import MyImage from "../components/image/my-image.vue";
import MyInput from "../components/input/my-input.vue";
import MyLabel from "../components/label/my-label.vue";
import {ref} from "vue";
import FormField from "../model/FormField.ts";


import {goDatabaseManager, goLogin} from "../util/routerUtils.ts";
import {InputExpose} from "../global/input/InputExpose.ts";


const email_input = ref<InputExpose>()

const email = ref<FormField>(new FormField(""))

const register_error = ref(false)
const register_error_message = ref('')

const resetPasswordClick = () => {
    if (!email_input.value?.validate()) {
        return;
    }
    register_error.value = true
    register_error_message.value = '功能开发中,请联系管理员'
}

const goLoginClick = () => {
    goLogin()
}

const goDatabaseManagerClick = ()=>{
    goDatabaseManager()
}
</script>

<template>
    <div class="w-full flex justify-center py-12">
        <div class="w-[300px] bg-white p-4">
            <div>
                <my-image class="mx-auto" src="logo.png"></my-image>
            </div>
            <MyDivider margin="1rem auto"></MyDivider>
            <div class="pb-2">
                <my-label class="my-2" htmlFor="email">您的电子邮件</my-label>
                <MyInput ref="email_input" v-model="email" htmlId="email" htmlName="email"
                         :required="true"></MyInput>
                <div v-show="!email.isValidate" class="absolute text-danger">请填写Email</div>
            </div>
            <div class="py-4" v-if="register_error">
                <div class="alter-danger px-4 py-5 rounded">{{ register_error_message }}</div>
            </div>
            <div class="pt-4">
                <MyButton class="w-full" @click="resetPasswordClick" rounded>重置密码</MyButton>
            </div>
            <div class="flex justify-between pt-4">
                <MyButton is-link @click="goLoginClick">返回登录页面</MyButton>
            </div>
            <div class="pt-8">
                <MyDivider></MyDivider>
            </div>

            <div class="flex justify-center pt-4 h-auto items-center">
                <MyButton is-link @click="goDatabaseManagerClick">管理数据库</MyButton>
                <MyDivider margin="0 0.25rem" vertical height="10px"></MyDivider>
                <MyButton is-link>由Avalon提供支持</MyButton>
            </div>
        </div>
    </div>
</template>

<style scoped>
.alter-danger {
    color: #842029;
    background-color: #f8d7da;
    border-color: #f5c2c7;
}
</style>