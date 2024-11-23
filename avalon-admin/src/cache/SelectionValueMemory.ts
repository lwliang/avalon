/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

const selectionValues: any = {}
import {getModelSelectionApi} from "../api/modelApi.ts";

export async function getSelectionValueByServiceAndField(service: string, field: string) {
    const key = service + '_' + field;
    if (Object.keys(selectionValues).includes(key)) {
        return selectionValues[key]
    }
    return await getModelSelectionApi(service, field).then(data => {
        selectionValues[key] = data
        return selectionValues[key]
    })
}
