/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/17 9:56
 */

export type FilterOperator =
    | '='
    | '!='
    | 'in'
    | 'notIn'
    | 'like'
    | 'notLike'
    | '>'
    | '>='
    | '<'
    | '<='
    | 'between'

export interface FilterCondition {
    field: string
    operator: FilterOperator
    value: any
}
