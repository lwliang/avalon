/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {createVNode, render} from "vue";
import MyNotification from "./my-notification.vue";

const notifyContain = document.createElement("div");
notifyContain.id = 'notifyContain'
notifyContain.classList.add('fixed', 'top-4', 'z-[99999]', 'right-4', 'overflow-y-auto')
document.body.appendChild(notifyContain);

function createNotification(title: String, content: String, type: String, duration?: Number) {
    const div = document.createElement("div");
    div.classList.add('my-2')
    notifyContain.appendChild(div);
    const vm = createVNode(MyNotification, {
        title,
        content,
        duration,
        type: type,
        onRemove: () => {
            notifyContain.removeChild(div)
        }
    })
    render(vm, div)
}

export default {
    success: (title: String, content: String, duration?: Number) => {
        createNotification(title, content, 'success', duration)
    },
    warn: (title: String, content: String, duration?: Number) => {
        createNotification(title, content, 'warning', duration)
    },
    error: (title: String, content: String, duration?: Number) => {
        createNotification(title, content, 'error', duration)
    },
    info: (title: String, content: String, duration?: Number) => {
        createNotification(title, content, 'info', duration)
    }
}