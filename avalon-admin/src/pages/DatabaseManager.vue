<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import MyImage from "../components/image/my-image.vue";
import {ComponentInternalInstance, ref} from 'vue'
import MyButton from "../components/button/my-button.vue";
import MyBtnGroup from "../components/button-group/my-btn-group.vue";
import MyDivider from "../components/divider/my-divider.vue";
import {createDB, dropDB, getDB} from "../api/dbAPI.ts";
import {getCurrentInstance} from "vue";
import MyInput from "../components/input/my-input.vue";
import MyDialog from "../components/dialog/my-dialog.vue";
import FormField from "../model/FormField.ts";
import MyPopoverConfirm from "../components/popconfirm/my-popover-confirm.vue";
import {useStorage} from "@vueuse/core";
import {goLogin} from "../util/routerUtils.ts";
import {InputExpose} from "../global/input/InputExpose.ts";

const {proxy} = getCurrentInstance() as ComponentInternalInstance;

const databases = ref<any[]>([])
const selectDB = useStorage('db', '')

const loadDB = () => {
    getDB().then(data => {
        databases.value.splice(0, databases.value.length)
        databases.value.push(...data)
    })
}
loadDB();

const developerClick = () => {
    proxy?.$notify.info('提示', '开发中敬请期待');
}

const createDBClick = () => {
    show.value = true

}


const show = ref(false)

const db = ref(new FormField(""))
const db_input = ref<InputExpose>()

const hideClick = () => {
    show.value = false
}

const sureClick = () => {
    if (!db_input.value?.validate()) {
        proxy?.$notify.error('提示', "请填写数据库名称");
        return;
    }

    createDB(db.value.value).then(() => {
        proxy?.$notify.success('成功', '创建数据库完成');
        show.value = false
        loadDB();
    }).catch(error => {
        proxy?.$notify.error('失败', error.msg);
    })
}

const dropDBClick = (db: string) => {
    dropDB(db).then(() => {
        proxy?.$notify.success('成功', `删除${db}数据库完成`);
        loadDB();
    }).catch(error => {
        proxy?.$notify.error('失败', error.msg);
    })
}

const selectDBClick = (db: string) => {
    selectDB.value = db
    goLogin({db})
}
</script>

<template>
    <div class="w-full pt-8">
        <div class="max-w-[700px] mx-auto">
            <div>
                <my-image class="mx-auto" src="/avalon.png" width="300"></my-image>
            </div>
            <div class="pt-8 rounded">
                <div class="p-2 bg-white" v-for="(db,index) in databases" :key="index">
                    <div class="flex justify-between">
                        <my-button type="primary" is-link @click="selectDBClick(db.dataName)">{{
                                db.dataName
                            }}
                        </my-button>
                        <div >
                            <my-btn-group>
                                <my-button icon="floppy-disk" @click="developerClick">备份</my-button>
                                <my-button type="info" icon="clone" @click="developerClick">复制</my-button>
                                <my-button type="warning" icon="window-restore" @click="developerClick">恢复</my-button>
                                <MyPopoverConfirm @click="dropDBClick(db.dataName)" content="确认删除?">
                                    <my-button type="danger" icon="trash-can">删除
                                    </my-button>
                                </MyPopoverConfirm>
                            </my-btn-group>
                        </div>
                    </div>
                    <my-divider v-if="databases.length != 1 && index != databases.length -1"
                                margin="16px auto 0"></my-divider>
                </div>

            </div>
            <div class="pt-4">
                <my-button icon-style="fas" type="success" icon="plus" @click="createDBClick">创建数据库</my-button>
                <div class="inline-block w-5"></div>
                <my-button icon-style="fas" type="danger" icon="lock" @click="developerClick">设置主密码</my-button>
            </div>
        </div>
    </div>
    <MyDialog :show="show" @close="hideClick" @sure="sureClick" title="创建数据库">
        <MyInput ref="db_input" v-model="db" :required="true"></MyInput>
    </MyDialog>
</template>

<style scoped>

</style>