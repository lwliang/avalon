/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import PageParam from "./PageParam.ts";


export default interface ServiceConditionPage {
    serviceName: string
    page: PageParam
    fields: string
    order: string
    isDistinct: boolean
    rpnCondition: string
}
