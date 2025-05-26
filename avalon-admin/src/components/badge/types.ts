/**
 * @author lwlianghehe@gmail.com
 * @date 2025/5/20
 */

export interface BadgeProps {
    value: string | number
    max?: number
    isDot?: boolean
    hidden?: boolean
    type?: 'primary' | 'success' | 'warning' | 'danger' | 'info'
    color?: string
}

