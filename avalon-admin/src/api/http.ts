/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import axios from 'axios'
import {getErpPrefix, getFilePrefix} from "./env.ts";
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
    if (response.config.responseType == 'blob') {
        return response
    }

    return response.data
}, (error) => {
    // 判断是“网络错误”或“服务器无法连接”
    if (!error.response) {

        // 断网/服务挂了/CORS等
        if (error.code === 'ECONNABORTED') {
            return Promise.reject({code: 400, msg: '请求超时'})
        } else if (error.message && error.message.includes('Network Error')) {
            return Promise.reject({code: 400, msg: '网络错误'})
        } else {
            return Promise.reject({code: 400, msg: '无法连接服务器'})
        }
        // 你可以自定义提示
    } else {
        // 有 response，是接口报错（如 404、500 等）
        console.log('服务器有响应，但报错', error.response.status);
        if (error.response.status === 401) {
            goLogin()
        }
        if (error.response.data) {
            MyNotification.error('错误', error.response.data.msg)
            return Promise.reject(error.response.data)
        }
        return Promise.reject({code: error.code, msg: error.message})
    }
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

export function postDownloadFileHttp(url: string, params: any): Promise<any> {
    return new Promise((resolve, reject) => {
        axios.post(getErpPrefix() + url, params, {responseType: 'blob'}).then((res) => {
            resolve(res)
        }).catch((err) => {
            reject(err)
        })
    })
}

export function postDownloadFileFromFileServer(url: string, params: any): Promise<any> {
    return new Promise((resolve, reject) => {
        axios.post(getFilePrefix() + url, params, {responseType: 'blob'}).then((res) => {
            resolve(res)
        }).catch((err) => {
            reject(err)
        })
    })
}

export function postUploadFileHttp(url: string, file: File): Promise<any> {
    const formData = new FormData()
    formData.append('file', file)
    return new Promise((resolve, reject) => {
        axios.post(getErpPrefix() + url, formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }).then((res) => {
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
