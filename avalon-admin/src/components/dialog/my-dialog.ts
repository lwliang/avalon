/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {createVNode, render} from "vue";
import uploadDocumentDialog from "./upload-document-dialog.vue";

let zIndex = 1;

export function createUploadDocumentDialog(title: String, show: boolean) {
    zIndex++;
    const div = document.createElement("div");
    div.classList.add('upload-document')
    document.body.appendChild(div); // 添加到 body 中
    const vm = createVNode(uploadDocumentDialog, {
        title,
        modelValue: show, // 使用 modelValue 而不是 show
        zIndex,
        closeCallback: () => {
            // 卸载组件
            render(null, div);
            document.body.removeChild(div); // 移除 DOM 元素
        },
        sureCallback: () => {
            // 卸载组件
            render(null, div);
            document.body.removeChild(div); // 移除 DOM 元素
        }
    })
    render(vm, div)
}