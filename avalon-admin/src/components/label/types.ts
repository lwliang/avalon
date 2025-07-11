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
    icon?: string // Element Plus 图标名称
    closable?: boolean // 是否可关闭
    disableTransitions?: boolean // 是否禁用过渡动画
    hit?: boolean // 是否有边框描边
    color?: string // 背景色
    effect?: 'dark' | 'light' | 'plain' // 主题
}