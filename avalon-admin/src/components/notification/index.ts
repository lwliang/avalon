// notification.ts
import {createVNode, render, VNode} from "vue"
import MyNotification from "./my-notification.vue"
import type {NotificationType, NotificationPosition} from './types'

type NotifyInstance = {
    vm: VNode,
    div: HTMLDivElement,
    height: number,
    position: NotificationPosition,
}

const GAP = 80 // 每条通知的间隔
const INIT_OFFSET = 20

const instances: Record<NotificationPosition, NotifyInstance[]> = {
    'top-right': [],
    'top-left': [],
    'bottom-right': [],
    'bottom-left': [],
}

function getNextOffset(position: NotificationPosition): number {
    const arr = instances[position]
    return arr.reduce((acc, inst) => acc + inst.height + GAP, INIT_OFFSET)
}

function removeInstance(instance: NotifyInstance) {
    const arr = instances[instance.position]
    const idx = arr.indexOf(instance)
    if (idx > -1) {
        arr.splice(idx, 1)
        // 重新布局后面的
        for (let i = idx; i < arr.length; i++) {
            const inst = arr[i]
            const newOffset = i === 0
                ? INIT_OFFSET
                : arr[i - 1].div.offsetTop + arr[i - 1].div.offsetHeight + GAP
            inst.vm.component!.props.offset = newOffset
            inst.div.style.top = inst.position.includes('top') ? `${newOffset}px` : ''
            inst.div.style.bottom = inst.position.includes('bottom') ? `${newOffset}px` : ''
        }
    }
}

export interface CreateNotificationOptions {
    title?: string
    message?: string
    type?: NotificationType
    duration?: number
    position?: NotificationPosition
    showClose?: boolean
    // offset?: number   // 不建议外部传入，由管理器自动分配
}

function createNotification(options: CreateNotificationOptions) {
    const {
        position = 'top-right',
        ...rest
    } = options

    const div = document.createElement("div")
    div.classList.add("my-2", "absolute") // 绝对定位
    // 挂载到body，必要时可为每个位置建一个容器
    document.body.appendChild(div)

    // 自动分配 offset
    const offset = getNextOffset(position)
    const vm = createVNode(MyNotification, {
        ...rest,
        position,
        offset,
        onRemove: () => {
            render(null, div)
            div.remove()
            removeInstance(instance)
        }
    })
    render(vm, div)

    // 记录高度
    // 等待下一个tick，确保渲染后能获取高度
    setTimeout(() => {
        instance.height = div.offsetHeight
    })

    // 记录实例
    const instance: NotifyInstance = {vm, div, height: 0, position}
    instances[position].push(instance)
}

const notification = {
    success: (title: string, message: string, duration?: number, options: Partial<CreateNotificationOptions> = {}) =>
        createNotification({title, message, type: "success", duration, ...options}),
    warning: (title: string, message: string, duration?: number, options: Partial<CreateNotificationOptions> = {}) =>
        createNotification({title, message, type: "warning", duration, ...options}),
    error: (title: string, message: string, duration?: number, options: Partial<CreateNotificationOptions> = {}) =>
        createNotification({title, message, type: "danger", duration, ...options}),
    info: (title: string, message: string, duration?: number, options: Partial<CreateNotificationOptions> = {}) =>
        createNotification({title, message, type: "info", duration, ...options}),
    open: createNotification,
}

export default notification
