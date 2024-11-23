/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {definePropType} from "../../util/propUtils.ts";
import {iconStyleType} from "../icon/my-icon.ts";

export const btnType = definePropType<'primary' | 'success' | 'danger' | 'warning' | 'info'>(String)

export const btnActionType = definePropType<'object' | 'action'>(String)


export const buttonProp = {
    type: {
        type: btnType,
        default: 'primary'
    },
    rounded: Boolean,
    isLink: Boolean,
    icon: String,
    iconColor: {
        type: String,
        default: "#FFF"
    },
    iconStyle: {
        type: iconStyleType,
        default: 'far'
    },
    action: {
        type: String,
        default: ''
    },
    actionType: {
        type: btnActionType,
        default: 'action'
    }
}