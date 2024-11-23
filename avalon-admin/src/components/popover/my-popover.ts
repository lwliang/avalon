/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {definePropType} from "../../util/propUtils.ts";

export const popoverType = definePropType<'top'
    | 'top-start'
    | 'top-end'
    | 'right'
    | 'right-start'
    | 'right-end'
    | 'bottom'
    | 'bottom-start'
    | 'bottom-end'
    | 'left'
    | 'left-start'
    | 'left-end'>(String)

export const popoverTrigger = definePropType<'hover' | 'click'>(String)
export const optionType = definePropType<Record<string,String>>(Object)

export interface PopperAPI {
    show: () => void
    hide: () => void
}