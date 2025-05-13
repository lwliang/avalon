/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/13 14:06
 */
import {definePropType} from "../../util/propUtils.ts";

export const conditionOperateType = definePropType<'=' | '!=' | '>' | '>=' | '<=' | '<'
    | 'in' | 'like' | 'notIn' | 'notLike' | 'between'>(String)