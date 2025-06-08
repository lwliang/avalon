/**
 * @author lwlianghehe@gmail.com
 * @date 2025/5/19
 */
import type {IconType} from '../icon/types'

export type InputSize = 'large' | 'default' | 'small'

/**
 * Input 组件 Props 类型定义
 */
export interface InputProps {
    /** 是否禁用输入框 */
    disabled?: boolean

    /** 是否只读 */
    readonly?: boolean

    /** 输入框尺寸（large/default/small） */
    size?: InputSize

    /** 是否可清空 */
    clearable?: boolean

    /** 是否必填 */
    required?: boolean

    /** 校验正则表达式字符串 */
    pattern?: string

    /** 前缀图标（图标名或 [前缀, 名称]） */
    prefixIcon?: string | [string, string]

    /** 前缀图标类型（如 'fas'/'far'/'fab'） */
    prefixIconType?: IconType

    /** 后缀图标（图标名或 [前缀, 名称]） */
    suffixIcon?: string | [string, string]

    /** 后缀图标类型 */
    suffixIconType?: IconType

    /** 占位符文本 */
    placeholder?: string

    /** 输入框类型，默认 'text' */
    type?: string // 'text' | 'password' | 'number' | ...
}