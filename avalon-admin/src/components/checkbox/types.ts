/**
 * @author lwlianghehe@gmail.com
 * @date 2025/5/20
 */

// types.ts
export interface CheckboxProps {
    label?: string
    trueValue?: string | number | boolean
    falseValue?: string | number | boolean
    disabled?: boolean
    border?: boolean
    required?: boolean
    size?: 'large' | 'default' | 'small'
    indeterminate?: boolean
    class?: string
}