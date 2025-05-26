/**
 * @author lwlianghehe@gmail.com
 * @date 2025/5/20
 */
import {IconType} from "../icon/types.ts";

export interface SwitchProps {
    disabled?: boolean
    loading?: boolean
    width?: number | string
    activeIcon?: string | [string, string]
    activeIconType?: IconType
    inactiveIcon?: string | [string, string]
    inactiveIconType?: IconType
    size?: 'large' | 'default' | 'small'
    activeText?: string
    inactiveText?: string
    activeValue?: string | number | boolean
    inactiveValue?: string | number | boolean
    name?: string
    require?: boolean
}
