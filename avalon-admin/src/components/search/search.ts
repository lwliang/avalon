/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/13 14:06
 */
import {definePropType} from "../../util/propUtils.ts";

export type ConditionOperateType =
    '='
    | '!='
    | '>'
    | '>='
    | '<='
    | '<'
    | 'in'
    | 'like'
    | 'notIn'
    | 'notLike'
    | 'between';