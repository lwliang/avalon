/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

export default interface FileUploadRes {
    originName: string
    url: string,
    size: number,
    mine: string
}

export function isFileUploadRes(object: any): object is FileUploadRes {
    return 'originName' in object && 'url' in object
}
