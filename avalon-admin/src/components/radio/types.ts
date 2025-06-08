/**
 * @author lwlianghehe@gmail.com
 * @date 2025/5/20
 */

export interface RadioProps {
    label?: string
    value: string | number | boolean
    disabled?: boolean
    border?: boolean
    required?: boolean
    size?: 'large' | 'default' | 'small'
    name?: string
}
