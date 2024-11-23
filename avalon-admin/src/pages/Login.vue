<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import MyButton from "../components/button/my-button.vue";
import MyDivider from "../components/divider/my-divider.vue";
import MyPassword from "../components/password/my-password.vue";
import MyImage from "../components/image/my-image.vue";
import MyInput from "../components/input/my-input.vue";
import MyLabel from "../components/label/my-label.vue";
import {ref} from "vue";
import FormField from "../model/FormField.ts";
import {login} from "../api/loginApi.ts";
import {useRouter} from "vue-router";
import {setUserId} from "../cache/userStorage.ts";
import {setToken} from "../cache/tokenStorage.ts";
import {goDatabaseManager, goRegister, goResetPassword} from "../util/routerUtils.ts";
import {useStorage} from "@vueuse/core";
import {getDB} from "../api/dbAPI.ts";
import {emitLogin} from '../global/bus/mittBus.ts'
import {InputExpose} from "../global/input/InputExpose.ts";


const account_input = ref<InputExpose>()
const db_input = ref<InputExpose>()
const password_input = ref<InputExpose>()
const account = ref<FormField>(new FormField(""))
const password = ref<FormField>(new FormField(""))

const login_error = ref(false)
const router = useRouter()
const db = ref<FormField>(new FormField(useStorage('db', '').value))

const allDBS = ref<any>([])

getDB().then(data => {
    allDBS.value.splice(0, allDBS.value.length)
    allDBS.value.push(...data)
})


const loginClick = () => {
    if (!account_input.value?.validate()) {
        return;
    }
    if (!password_input.value?.validate()) {
        return;
    }
    login_error.value = false
    login(db.value.value, account.value.value, password.value.value).then(res => {
        setUserId(res.id)
        setToken(res.token)
        emitLogin();
        router.push({
            path: '/model'
        })
    }).catch((error) => {
        console.log(error)
        login_error.value = true
    })

}
const goRegisterClick = () => {
    goRegister();
}

const goResetPasswordClick = () => {
    goResetPassword()
}

const goDatabaseManagerClick = () => {
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
            <div class="pb-2" v-if="allDBS.length">
                <my-label class="my-2" htmlFor="account">数据库</my-label>
                <MyInput ref="db_input" v-model="db" htmlId="db" htmlName="db" :readonly="true" right-content="选择"
                         @rightBtnClick="goDatabaseManagerClick"
                         :required="true" icon="database" icon-style="fas" icon-color="#FFF"></MyInput>
            </div>
            <div class="pb-2">
                <my-label class="my-2" htmlFor="account">账户</my-label>
                <MyInput ref="account_input" v-model="account" htmlId="account" htmlName="account"
                         :required="true"></MyInput>
                <div v-show="!account.isValidate" class="absolute text-danger">请填写账号</div>
            </div>
            <div class="pb-2">
                <my-label class="my-2" html-for="password">密码</my-label>
                <MyPassword ref="password_input" v-model="password" htmlId="password" htmlName="password"
                            :required="true"></MyPassword>
                <div v-show="!password.isValidate" class="text-danger absolute">请填写密码</div>
            </div>
            <div class="py-4" v-if="login_error">
                <div class="alter-danger px-4 py-5 rounded">错误的登录名/密码</div>
            </div>
            <div class="pt-4">
                <MyButton class="w-full" @click="loginClick" rounded>登录</MyButton>
            </div>
            <div class="flex justify-between pt-4">
                <MyButton is-link @click="goRegisterClick">还没有账户?</MyButton>
                <MyButton is-link @click="goResetPasswordClick">重置密码</MyButton>
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