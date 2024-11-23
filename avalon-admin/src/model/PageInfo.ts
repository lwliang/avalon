/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

export default interface PageInfo {
    total: number
    pageCur: number
    pageSize: number
    pageCount: number
    nextPage: boolean
    prePage: boolean
    data: Array<any>
}
