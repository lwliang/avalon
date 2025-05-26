/**
 * @author lwlianghehe@gmail.com
 * @date 2025/15/19
 */

export type TextType = 'default' | 'primary' | 'success' | 'warning' | 'danger' | 'info'
export type TextSize = 'large' | 'medium' | 'small'
export type TextWeight = 'regular' | 'medium' | 'bold'

export interface TextProps {
    type?: TextType
    size?: TextSize
    weight?: TextWeight
    truncated?: boolean         // 单行省略
    lineClamp?: number          // 多行省略
    width?: number | string,
    textClass?: string,
    title?: string
}

