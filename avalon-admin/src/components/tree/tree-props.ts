/**
 * @author lwlianghehe@gmail.com
 * @date 2024/12/12 18:40
 */

export interface TreeData extends Record<string, any> {
    id: number
    name: string
    parentId: TreeData
    childIds: TreeData[]
}