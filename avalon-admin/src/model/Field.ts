/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {FieldTypeEnum} from "./enum-type/FieldTypeEnum.ts";


export default interface Field {
    id:number,
    label: string,
    name: string,
    type: FieldTypeEnum,
    isPrimaryKey: boolean,
    isRequired: boolean,
    isUnique: boolean,
    isReadonly: boolean,
    isAutoIncrement: boolean,
    allowNull: boolean,
    defaultValue: string,
    isExternalField: boolean,
    realName: string
    serviceId: number,
    minValue: number,
    maxValue: number,
    relativeServiceName: string,
    masterForeignKeyName: string,
    relativeForeignKeyName: string,
    manyServiceTable: string,
    relativeFieldName: string,
}