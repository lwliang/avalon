/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

export function getSelectionOptionsAsync(serviceName: string, fields: string) {
    const xhr = new XMLHttpRequest()
    xhr.open('POST', '/erp/model/get/selection/map', false) // false  同步 true 异步
    xhr.setRequestHeader('Content-Type', 'application/json')
    xhr.setRequestHeader('token', localStorage.getItem('token') || '')
    xhr.send(JSON.stringify({serviceName, fields}))

    if (xhr.status === 200) {
        return JSON.parse(xhr.responseText).data
    }
}