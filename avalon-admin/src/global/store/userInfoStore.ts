/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {defineStore} from "pinia";
import User from "../../model/User.ts";

export const useUserInfoStore = defineStore('userInfo', {
    state: () => ({
        user: {} as User
    }),
    actions: {
        setUserInfo(user: User) {
            this.user = user
        }
    },
    getters: {}
})