/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/19
 */
import {IconType} from '../icon/types'

export type LabelType = 'primary' | 'success' | 'warning' | 'danger' | 'info' | 'default'
export type LabelSize = 'large' | 'default' | 'small'

export interface LabelProps {
    type?: LabelType
    size?: LabelSize
    icon?: string
    iconType?: IconType
}