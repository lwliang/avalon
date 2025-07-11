/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/22 10:00
 */

export type ProgressType = 'line' | 'circle'
export type ProgressStatus = 'primary' | 'success' | 'warning' | 'danger' | 'info'

export interface ProgressProps {
    percentage: number
    type?: ProgressType
    status?: ProgressStatus
    color?: string
    textInside?: boolean
    strokeWidth?: number,
    width?: number, // 在环形下有效
}
