/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import axios from 'axios'
import {getErpPrefix} from "./env.ts";
import {goLogin} from "../util/routerUtils.ts";
import MyNotification from "../components/notification/index.ts";


const baseURL = '/'
// const baseURL = 'http://localhost:8090/'
const noNeedToken = ['/login', '/register']

// 拦截器
axios.interceptors.request.use((config) => {
    config.baseURL = baseURL
    if (config.url && !noNeedToken.includes(config.url, 0)) {
        config.headers.token = localStorage.getItem('token')
    }

    return config  // 必须返回config
}, (error) => {
    return Promise.reject(error)
})

axios.interceptors.response.use((response) => {
    if (response.data)
        return response.data
    return {}
}, (error) => {
    if (error.response.status === 401) {
        goLogin()
    }
    if (error.response.data) {
        MyNotification.error('错误', error.response.data.msg)
        return Promise.reject(error.response.data)
    }
    return Promise.reject({code: error.code, msg: error.message})
})

export function getHttp(url: string, config: any): Promise<any> {
    return new Promise((resolve, reject) => {
        axios.get(url, config).then((res) => {
            resolve(res)
        }).catch((err) => {
            reject(err)
        })
    })
}

export function postHttp(url: string, params: any): Promise<any> {
    return new Promise((resolve, reject) => {
        axios.post(url, params).then((res) => {
            resolve(res)
        }).catch((err) => {
            reject(err)
        })
    })
}

// erp 专用前缀接口
export function postErpHttp(url: string, params: any): Promise<any> {
    return postHttp(getErpPrefix() + url, params)
}

export function getErpHttp(url: string, params: any): Promise<any> {
    return getHttp(getErpPrefix() + url, params)
}

export default axios
