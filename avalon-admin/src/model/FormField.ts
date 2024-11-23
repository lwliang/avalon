/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import Field from "./Field.ts";
import {cloneDeep, isEqual} from "lodash";
import {FieldTypeEnum} from "./enum-type/FieldTypeEnum.ts";
import {isObject} from "../util/typeUtils.ts";
import {OperateTypeEnum} from "./enum-type/OperateTypeEnum.ts";
import {useGlobalFieldDataStore} from "../global/store/fieldStore.ts";
import {useGlobalServiceDataStore} from "../global/store/serviceStore.ts";
import {uploadFile} from "../api/fileUploadApi.ts";
import Service from "./Service.ts";
import {getDateTime, getDateTimeWithAll} from "../util/dateUtils.ts";

const useFieldDataStore = useGlobalFieldDataStore()
const useServiceDataStore = useGlobalServiceDataStore();

export default class FormField {
    value: any;
    originalValue: any;
    isValidate: boolean = true;
    Field?: Field;

    isChanged(): boolean {
        return !isEqual(this.value, this.originalValue);
    }

    isValid(): boolean {
        return this.isValidate;
    }

    constructor(value: any, field?: Field) {
        this.Field = field;
        this.value = value;
        this.originalValue = cloneDeep(value);
    }

    async getRawValue() {
        if (!this.Field) {
            return this.value;
        }
        return await this._doGetRawValue(this.value, this.Field, this.originalValue)
    }

    async _doGetRawValue(value: any, field: Field, originValue: any) {
        if (field.type == FieldTypeEnum.One2manyField) {
            if (!value) return value;
            const fields = await useFieldDataStore.getFieldByServiceNameAsync(field.relativeServiceName);
            const keyField = useFieldDataStore.getPrimaryKeyFieldByServiceName(field.relativeServiceName)
            return this._getOne2ManyValue(value, originValue, field.relativeServiceName, fields, keyField)
        } else if (field.type == FieldTypeEnum.Many2oneField) {
            if (isObject(value)) {
                const service = useServiceDataStore.getServiceByName(field.relativeServiceName);
                return value[service.keyField]
            }
            return value
        } else if (field.type == FieldTypeEnum.Many2manyField) {
            if (!value) return value;
            const service = await useServiceDataStore.getServiceByNameAsync(field.relativeServiceName);
            return this._getMany2ManyValue(value, originValue, service, field.relativeForeignKeyName)
        } else if (field.type == FieldTypeEnum.One2oneField) {

        } else if (field.type == FieldTypeEnum.DateTimeField) {
            if (!value) return value;
            return getDateTimeWithAll(value);
        } else if (field.type == FieldTypeEnum.ImageField) {
            if (value) {
                const result = await uploadFile(value)
                return result.url
            }
        } else {
            return value
        }
    }

    async _getOne2ManyValue(value: any, originValue: any,
                            service: string, fields: Field[], keyField: Field) {
        const result: any[] = []

        // 新增 和 修改
        for (let valueElement of value) {
            const row: any = {}
            if (keyField.name in valueElement && typeof valueElement[keyField.name] == 'symbol') { // 新增记录
                for (const key in valueElement) {
                    const field = fields.find(x => x.name == key)
                    if (!field) continue
                    row[key] = await this._doGetRawValue(valueElement[key], field, null)
                }
                row.op = OperateTypeEnum.insert
                result.push(row)
                continue;
            }

            const origin = originValue.find((y: any) => y[keyField.name] == valueElement[keyField.name])
            for (const key in valueElement) {
                const field = fields.find(x => x.name == key)

                if (!field) continue
                if (isEqual(valueElement[key], origin[key])) {
                    continue;
                }
                row[key] = await this._doGetRawValue(valueElement[key], field, origin[key])
            }
            if (Object.keys(row).length) {
                row['op'] = OperateTypeEnum.update
                row[keyField.name] = valueElement[keyField.name]
                result.push(row)
            }
        }

        // 删除
        if (originValue) {
            for (let originValueElement of originValue) {
                const e = value.find((v: any) => v[keyField.name] == originValueElement[keyField.name]);
                if (!e) { // 不存在
                    const row: any = {}
                    row[keyField.name] = originValueElement[keyField.name]
                    row['op'] = OperateTypeEnum.delete
                    result.push(row)
                }
            }
        }

        return result;
    }

    async _getMany2ManyValue(value: any, originValue: any, service: Service, relativeField: string) {
        const result: any[] = []
        const keyId = "id"
        // 新增 和 修改
        for (let valueElement of value) {
            const row: any = {}
            if (keyId in valueElement && typeof valueElement[keyId] == 'symbol') { // 新增记录
                row[relativeField] = valueElement[relativeField][service.keyField]
                row.op = OperateTypeEnum.insert
                result.push(row)
            }
        }

        // 删除
        if (originValue) {
            for (let originValueElement of originValue) {
                const e = value.find((v: any) => v[keyId] == originValueElement[keyId]);
                if (!e) { // 不存在
                    const row: any = {}
                    row[keyId] = originValueElement[keyId]
                    row['op'] = OperateTypeEnum.delete
                    result.push(row)
                }
            }
        }

        return result;
    }

    // 重置
    reset(value: any) {
        if (value && this.Field?.type == FieldTypeEnum.DateTimeField) {
            this.value = getDateTime(value)
            this.originalValue = this.value
            return
        }
        this.value = value
        this.originalValue = cloneDeep(value)
        this.isValidate = true
    }
}