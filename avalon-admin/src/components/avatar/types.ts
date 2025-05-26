/**
 * @author lwlianghehe@gmail.com
 * @date 2025/5/20
 */

import {IconType} from "../icon/types.ts";

export interface AvatarProps {
    icon?: string | [string, string]
    iconType?: IconType
    shape?: 'circle' | 'square',
    size?: 'large' | 'default' | 'small' | number
    src?: string
    fit?: 'fill' | 'contain' | 'cover' | 'none' | 'scale-down'
}