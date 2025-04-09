/**
 * @author lwlianghehe@gmail.com
 * @date 2025/04/06 9:09
 */

export default interface Form extends Record<string, any> {
    name: string,
    create: boolean,
    edit: boolean
}