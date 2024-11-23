/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import axios from "./http.ts";
import {getFilePrefix} from "./env.ts";


export function postFileHttp(url: string, params: FormData): Promise<any> {
    return new Promise((resolve, reject) => {
        axios.post(url, params, {
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

export function uploadFile(file: File): Promise<any> {
    const formData = new FormData()
    formData.append('file', file)
    return postFileHttp(getFilePrefix() + '/file/upload', formData)
}
