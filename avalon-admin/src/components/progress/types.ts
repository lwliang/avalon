/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/22 10:00
 */
import FormField from "../../model/FormField.ts";


export type ProgressType = 'line' | 'circle'
export type ProgressStatus = 'primary' | 'success' | 'warning' | 'danger' | 'info'

export interface ProgressProps {
    percentage: FormField | number
    type?: ProgressType
    status?: ProgressStatus
    color?: string
    textInside?: boolean
    strokeWidth?: number,
    width?:number, // 在环形下有效
}
