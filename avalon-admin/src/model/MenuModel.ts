/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

export default interface MenuModel {
    "id": number,
    "label": boolean,
    "name": string,
    "icon": string,
    "param": string,
    "type": "object" | "action",
    "sequence": number,
    "objectAction": string,
    serviceId: {
        id: number,
        name: string
    }
    "action": {
        "id": number,
        "serviceId": {
            "id": number,
            "name": string,
            "moduleId": {
                "id": number,
                "name": string
            }
        },
        "viewMode": string // kanban,form,search,tree
    },
    children: MenuModel[]
}