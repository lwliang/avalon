/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/15 12:03
 */
import Field from "../model/Field.ts";
import {useFieldStore} from "../global/store/fieldStore.ts";
import FormField from "../model/FormField.ts";
import Snowflake from "../model/Snowflake.ts";
import { FieldTypeEnum } from "../model/enum-type/FieldTypeEnum.ts";
import { isObject } from "./typeUtils.ts";

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

export const getChangeFieldRecordRow = async (newRecordRow: any, oldRecordRow: any, fields:Field[]) => {
    const changedData = {} as any;
    for (let key in newRecordRow) {
      const field = fields.find(f => f.name === key)
      if (!field) { continue }
        if (field.type == FieldTypeEnum.One2manyField || field.type == FieldTypeEnum.Many2manyField) {
        if(JSON.stringify(newRecordRow[key]) !== JSON.stringify(oldRecordRow[key])) {
            changedData[key] = []
            for (let x in oldRecordRow[key]) { // 删除
                const row = newRecordRow[key].find((one:any)=>one.id==oldRecordRow[key][x].id)
                if (!row) {
                    changedData[key].push({id:oldRecordRow[key][x].id, 'op': 'delete'})
                    continue
                }
            }
            for (let x in newRecordRow[key]) {
                if (typeof newRecordRow[key][x].id == typeof Symbol()) { // 新增
                    if(field.type == FieldTypeEnum.Many2manyField) {
                        changedData[key].push({'op': 'insert',
                            [field.relativeForeignKeyName]:newRecordRow[key][x][field.relativeForeignKeyName].id})
                    } else {
                        const serviceFields = await useFieldStore().getFieldByServiceNameAsync(field.relativeServiceName)
                        const changeRow = await getChangeFieldRecordRow(newRecordRow[key][x], {}, serviceFields)
                        changedData[key].push({...changeRow, 'op': 'insert'})
                    }
                    continue
                }
                if(field.type == FieldTypeEnum.Many2manyField) { // 多对多不处理
                    continue;
                }
                const row = oldRecordRow[key].find((one:any)=>one.id==newRecordRow[key][x].id) // 找到对应的记录
                if(row) {
                    if(JSON.stringify(newRecordRow[key][x]) !== JSON.stringify(row)) { // 修改
                        const serviceFields = await useFieldStore().getFieldByServiceNameAsync(field.relativeServiceName)
                        const changeRow = await getChangeFieldRecordRow(newRecordRow[key][x], row, serviceFields)
                        changeRow['id'] = newRecordRow[key][x].id
                        changedData[key].push({...changeRow, 'op': 'update'})
                    }
                }
            }
         } 
        }  else if(field.type == FieldTypeEnum.Many2oneField) {
            if(oldRecordRow && oldRecordRow[key] && JSON.stringify(newRecordRow[key]) !== JSON.stringify(oldRecordRow[key])) {
                if (isObject(newRecordRow[key])) {
                    changedData[key] = newRecordRow[key].id
                } else {
                    changedData[key] = newRecordRow[key]
                }
            } else {
                if (isObject(newRecordRow[key])) {
                    changedData[key] = newRecordRow[key].id
                } else {
                    changedData[key] = newRecordRow[key]
                }
            }
        } else {
            if(JSON.stringify(newRecordRow[key]) !== JSON.stringify(oldRecordRow[key])) {
                changedData[key] = newRecordRow[key]
            }
        }
    }
    return changedData
}

export const getAllFieldRecordRow = async (recordRow: any, fields:Field[]) => {
    const allRecordRow = {} as any; 
    for (let key in recordRow) {
        const field = fields.find(f => f.name === key)
        if (!field) { continue }
        if (field.type == FieldTypeEnum.One2manyField || field.type == FieldTypeEnum.Many2manyField) {
            if(JSON.stringify(recordRow[key]) !== JSON.stringify(allRecordRow[key])) { 
                allRecordRow[key] = []
                for (let x in recordRow[key]) {
                    allRecordRow[key].push(await getAllFieldRecordRow(recordRow[key][x], fields))
                }
            }
        }  else if(field.type == FieldTypeEnum.Many2oneField) {
            if (isObject(recordRow[key])) {
                allRecordRow[key] = recordRow[key].id
            } else {
                allRecordRow[key] = recordRow[key]
            }
        } else {
            allRecordRow[key] = recordRow[key]
        }
    }
    return allRecordRow
}
  
export async function getServiceField(service: string, field: string): Promise<Field | undefined> {
    const fieldStore = useFieldStore();
    const serviceFields = await fieldStore.getFieldByServiceNameAsync(service)
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

// 获取全部的值
export const getFieldAllRecordRow = async (recordRowField: Record<string, FormField | any>) => {
    const recordRow = {} as any;
    for (let fieldKey in recordRowField) {
        if (recordRowField[fieldKey] instanceof FormField) {
            recordRow[fieldKey] = await recordRowField[fieldKey].getRawValue()
        } else {
            if (!recordRow[fieldKey]) {
                recordRow[fieldKey] = {}
            }
            for (let x in recordRowField[fieldKey]) {
                recordRow[fieldKey][x] = await recordRowField[fieldKey][x].getRawValue()
            }
        }
    }
    return recordRow
}

export const getOldFieldRecordRow = async (recordRowField: Record<string, FormField | any>) => {
    const recordRow = {} as any;
    for (let fieldKey in recordRowField) {
        if (recordRowField[fieldKey] instanceof FormField) {
            recordRow[fieldKey] = await recordRowField[fieldKey].getOldRawValue()
        } else {
            if (!recordRow[fieldKey]) {
                recordRow[fieldKey] = {}
            }
            for (let x in recordRowField[fieldKey]) {
                recordRow[fieldKey][x] = await recordRowField[fieldKey][x].getOldRawValue()
            }
        }
    }
    return recordRow
}

export const getModelKeyValue = () => {
    return Symbol(Snowflake.getNextId())
}

export const isModelKeyValue = (value: any) => {
    return typeof value == 'symbol'
}