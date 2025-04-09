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
    const vm = createVNode(uploadDocumentDialog, {
        title,
        show,
        zIndex,
        closeCallback: () => {
            // 卸载组件
            render(null, div);
        },
        sureCallback: () => {
            // 卸载组件
            render(null, div);
        }
    })
    render(vm, div)
}