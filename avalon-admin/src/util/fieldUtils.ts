/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/15 12:03
 */
import Field from "../model/Field.ts";
import {useGlobalFieldDataStore} from "../global/store/fieldStore.ts";

const useFieldDataStore = useGlobalFieldDataStore()

export function hasJoin(field: string) {
    return field.includes(".");
}

export function dotToUnderscore(field: string) {
    return field.replaceAll(".", "_")
}

export function getJoinFirstField(field: string) {
    if (!hasJoin(field)) return field;
    const i = field.indexOf(".");
    return field.substring(0, i);
}

export function getJoinLastField(field: string) {
    if (!hasJoin(field)) return field;
    const i = field.indexOf(".");
    return field.substring(i + 1);
}

export async function getServiceField(service: string, field: string): Promise<Field | undefined> {
    const serviceFields = await useFieldDataStore.getFieldByServiceNameAsync(service)
    if (hasJoin(field)) {
        const prefixField = getJoinFirstField(field)
        const lastField = getJoinLastField(field);
        const find = serviceFields.find(x => x.name == prefixField);
        if (find) {
            return await getServiceField(find.relativeServiceName, lastField);
        }
        return undefined;
    }
    return serviceFields.find(x => x.name == field)
}