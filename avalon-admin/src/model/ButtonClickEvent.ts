/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/22 9:30
 */

export interface ButtonClickEvent {
    actionType?: 'object' | 'action',
    action?: string,
    event: MouseEvent
}