/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/22 14:21
 */

export type PlacementType = 'top' | 'top-start' | 'top-end'
    | 'bottom' | 'bottom-start' | 'bottom-end'
    | 'left' | 'left-start' | 'left-end'
    | 'right' | 'right-start' | 'right-end'

export type TriggerType = 'click' | 'focus' | 'hover' | 'contextmenu'

export type PopperProps = {
    trigger?: TriggerType
    title?: string
    content?: string
    width?: string | number
    placement?: PlacementType
    disabled?: boolean
    visible?: boolean
    teleported?: boolean
    arrowShow?: boolean,
    popperClass?: string,
    defaultClass?: string,
    popperStyle?: string | object
}
