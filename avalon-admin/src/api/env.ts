/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

const erpPrefixRoot = '/erp'
const filePrefixRoot = '/file'
const pageSize = 80

export function getFileUploadUrl(url: any) {
    return filePrefixRoot + url
}


export function getErpPrefix() {
    return erpPrefixRoot
}

export function getFilePrefix() {
    return filePrefixRoot
}

export function getPageSize() {
    return pageSize
}
