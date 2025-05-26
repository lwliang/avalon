/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/22 21:14
 */

export type NotificationType = 'success' | 'warning' | 'info' | 'danger' | ''
export type NotificationPosition = 'top-right' | 'top-left' | 'bottom-right' | 'bottom-left'

export interface NotificationProps {
    title?: string
    message?: string
    type?: NotificationType
    duration?: number   // 0 不自动关闭，默认 4500
    position?: NotificationPosition
    showClose?: boolean // 默认 true
    offset?: number     // 默认 0
}
