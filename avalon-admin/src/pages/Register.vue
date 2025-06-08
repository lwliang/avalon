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

import {setUserId} from "../cache/userStorage.ts";
import {setToken} from "../cache/tokenStorage.ts";
import {goDatabaseManager, goLogin} from "../util/routerUtils.ts";
import {register} from "../api/loginApi.ts";
import {InputExpose} from "../global/input/InputExpose.ts";
import MyText from "../components/text/my-text.vue";


const account_input = ref<InputExpose>()
const name_input = ref<InputExpose>()
const password_input = ref<InputExpose>()
const re_password_input = ref<InputExpose>()

const account = ref<FormField>(new FormField(""))
const name = ref<FormField>(new FormField(""))
const password = ref<FormField>(new FormField(""))
const re_password = ref<FormField>(new FormField(""))

const register_error = ref(false)
const register_error_message = ref('')

const registerClick = () => {
  if (!account_input.value?.validate()) {
    return;
  }
  if (!name_input.value?.validate()) {
    return;
  }
  if (!password_input.value?.validate()) {
    return;
  }
  if (!re_password_input.value?.validate()) {
    return;
  }
  register_error.value = true
  register_error_message.value = ''
  if (password.value.value !== re_password.value.value) {
    register_error.value = true
    register_error_message.value = '密码不一致'
    return;
  }

  register_error.value = false
  register(name.value.value,
      account.value.value,
      password.value.value).then(res => {
    setUserId(res.id)
    setToken(res.token)
    goLoginClick()
  }).catch((error) => {
    console.log(error)
    register_error.value = true
  })
}

const goLoginClick = () => {
  goLogin()
}

const goDatabaseManagerClick = () => {
  goDatabaseManager()
}
</script>

<template>
  <div class="w-full h-full flex justify-center py-12 " style="background-image:url(/color4bg.png);background-size: cover">
    <div>
      <div class="w-[300px] p-4 bg-background">
        <div>
          <my-image class="mx-auto" src="logo.png"></my-image>
        </div>
        <MyDivider margin="1rem auto"></MyDivider>
        <div class="pb-2">
          <my-label class="my-2" htmlFor="account">账户</my-label>
          <MyInput ref="account_input" v-model="account" htmlId="account" htmlName="account"
                   :required="true"></MyInput>
          <div v-show="!account.isValidate" class="absolute text-danger">请填写账号</div>
        </div>
        <div class="pb-2">
          <my-label class="my-2" htmlFor="name">姓名</my-label>
          <MyInput ref="name_input" v-model="name" htmlId="name" htmlName="name" :required="true"></MyInput>
          <div v-show="!name.isValidate" class="absolute text-danger">请填写姓名</div>
        </div>
        <div class="pb-2">
          <my-label class="my-2" html-for="password">密码</my-label>
          <MyPassword ref="password_input" v-model="password" htmlId="password" htmlName="password"
                      :required="true"></MyPassword>
          <div v-show="!password.isValidate" class="text-danger absolute">请填写密码</div>
        </div>
        <div class="pb-2">
          <my-label class="my-2" html-for="re_password">确认密码</my-label>
          <MyPassword ref="re_password_input" v-model="re_password" htmlId="re_password" htmlName="re_password"
                      :required="true"></MyPassword>
          <div v-show="!re_password.isValidate" class="text-danger absolute">请填写确认密码</div>
        </div>
        <div class="py-4" v-if="register_error">
          <div class="text-danger px-4 py-5 rounded">{{ register_error_message }}</div>
        </div>
        <div class="pt-4">
          <MyButton type="primary" class="w-full" @click="registerClick" rounded>注册</MyButton>
        </div>
        <div class="flex justify-between pt-4">
          <my-text type="primary" class="cursor-pointer" @click="goLoginClick">已经拥有账户?</my-text>
        </div>
        <div class="pt-8">
          <MyDivider></MyDivider>
        </div>

        <div class="flex justify-center pt-4 h-auto items-center">
          <my-text type="primary" class="cursor-pointer" @click="goDatabaseManagerClick">管理数据库</my-text>
          <MyDivider direction="vertical"></MyDivider>
          <my-text type="primary" class="cursor-pointer">由Avalon提供支持</my-text>
        </div>
      </div>
    </div>

  </div>
</template>

<style scoped>
</style>