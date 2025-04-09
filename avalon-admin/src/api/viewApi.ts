/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {getErpPrefix} from './env.ts'
import {postErpHttp} from "./http.ts";
import {useGlobalServiceDataStore} from "../global/store/serviceStore.ts";


export async function getBaseActionView(fields: string,
                                        condition: string) {
    const serviceName = "base.action.view"
    const param: any = {fields, condition, serviceName};
    const serviceStore = useGlobalServiceDataStore();
    const service = await serviceStore.getServiceByNameAsync(serviceName)
    param.order = `${service.keyField} desc`;
    return postErpHttp(`/view/get/${serviceName}/all`, param);
}