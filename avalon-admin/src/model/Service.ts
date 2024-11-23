/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

export default interface Service {
    "id": number
    "name": string,
    "label": string,
    "tableName": string
    "moduleId": number,
    nameField: string,
    keyField: string
}