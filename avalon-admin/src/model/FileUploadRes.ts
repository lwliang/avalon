/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

interface FileUploadRes {
  originName: string
  url: string
}

export function isFileUploadRes(object: any): object is FileUploadRes {
  return 'originName' in object && 'url' in object
}
