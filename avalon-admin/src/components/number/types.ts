/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/15
 */

export type NumberSize = 'large' | 'default' | 'small'

/**
 * Number Input 组件 Props 类型定义
 */
export interface NumberInputProps {
    /** 是否禁用输入框 */
    disabled?: boolean

    /** 是否只读 */
    readonly?: boolean

    /** 输入框尺寸（large/default/small） */
    size?: NumberSize

    /** 是否可清空 */
    clearable?: boolean

    /** 是否必填 */
    required?: boolean

    /** 占位符文本 */
    placeholder?: string

    /** 最小值 */
    min?: number

    /** 最大值 */
    max?: number

    /** 步长 */
    step?: number

    /** 数值精度（小数位数） */
    precision?: number

    /** 是否严格步长 */
    stepStrictly?: boolean

    /** 是否显示控制按钮 */
    controls?: boolean

    /** 控制按钮位置 */
    controlsPosition?: 'right' | ''

    /** 前缀文本 */
    prefix?: string

    /** 后缀文本 */
    suffix?: string
} 