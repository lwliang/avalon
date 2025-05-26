/**
 * @author lwlianghehe@gmail.com
 * @date 2025/5/21
 */

export interface TagProps {
    type?: 'primary' | 'success' | 'info' | 'warning' | 'danger'
    closable?: boolean
    color?: string
    size?: 'large' | 'default' | 'small'
    effect?: 'dark' | 'light' | 'plain'
    round?: boolean,
    label?: String,
    value?: any
}
