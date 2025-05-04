/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {createApp, version} from 'vue'
import {createPinia} from 'pinia'
import router from './router'
import './style.css'
import App from './App.vue'
import log from 'loglevel'
import {loadSvg} from "./components/icon/my-icon.ts";
import MyNotification from "./components/notification/index.ts";
import MyButton from "./components/button/my-button.vue";
import MyImage from "./components/image/my-image.vue";
import {getModuleIcon} from "./api/moduleApi.ts";
import MyLabel from "./components/label/my-label.vue";
import MyInput from "./components/input/my-input.vue";
import MyIcon from "./components/icon/my-icon.vue";
import Sheet from "./components/form-layout/sheel/sheet.vue";
import Row from "./components/form-layout/row/row.vue";
import MyCol from "./components/form-layout/col/my-col.vue";
import MySelectionSelect from "./components/select/selection-select/my-selection-select.vue";
import MyIdSelect from "./components/select/id-select/my-id-select.vue";
import MyTabs from "./components/tabs/my-tabs.vue";
import MyTabPanel from "./components/tabs/my-tab-panel.vue";
import MySubTree from './pages/window/action/form/my-sub-tree.vue';
import MyOverlay from "./components/overlay/my-overlay.vue";
import MyFormModel from "./components/model/form-model/my-form-model.vue";
import MyImageUpload from "./components/upload/my-image-upload.vue";
import MyPassword from "./components/password/my-password.vue";
import MyDate from "./components/date/my-date.vue";
import MyCheck from "./components/check/my-check.vue";
import MyTextarea from "./components/textarea/my-textarea.vue";
import MyTag from "./components/tag/my-tag.vue";
import MyMany2manySelect from "./components/select/many2may-select/my-many2many-select.vue";
import MyTime from "./components/time/my-time.vue";
import MyDatetime from "./components/datetime/my-datetime.vue";
import MySearch from "./components/search/my-search.vue";
import MyPagination from "./components/pagination/my-pagination.vue";
import MyAvatar from "./components/avatar/my-avatar.vue";
import MyDebug from "./components/debug/my-debug.vue";
import MyXmlViewer from "./components/xml-viewer/my-xml-viewer.vue";
import MyVideoUpload from "./components/upload/my-video-upload.vue";
import MyVideo from "./components/video/my-video.vue";
import {ReconnectingWebSocket} from "./ws/WebScoket.ts";
import {getToken} from "./cache/tokenStorage.ts";
import ChatWindow from "./components/im/chat-window.vue";
import DocumentList from "./components/document/document-list.vue";
import MyTree from "./components/tree/my-tree.vue";
import RegistryPlugin from "./global/registry/registryPlugin.ts";
import {registerActions} from './global/registry/actions'
import MarkdownVue from "./components/markdown/markdown.vue";
import ExcalidrawEditorVue from "./components/excalidraw/ExcalidrawVue.vue";


window.ReconnectingWebSocket = ReconnectingWebSocket;


const app = createApp(App)
loadSvg(app)

app.use(createPinia())
app.use(router)
app.component('MyButton', MyButton)
app.component('MyImage', MyImage)
app.component('MyAvatar', MyAvatar)
app.component('MyLabel', MyLabel)
app.component('MyInput', MyInput)
app.component('MyIcon', MyIcon)
app.component('Sheet', Sheet)
app.component('Row', Row)
app.component('MyCol', MyCol)
app.component('MySelectionSelect', MySelectionSelect)
app.component('MyIdSelect', MyIdSelect)
app.component('MyTabs', MyTabs)
app.component('MyTabPanel', MyTabPanel)
app.component('MySubTree', MySubTree)
app.component('MyOverlay', MyOverlay)
app.component('MyFormModel', MyFormModel)
app.component('MyImageUpload', MyImageUpload)
app.component('MyVideoUpload', MyVideoUpload)
app.component('MyPassword', MyPassword)
app.component('MyDate', MyDate)
app.component('MyCheck', MyCheck)
app.component('MyTextarea', MyTextarea)
app.component('MyTag', MyTag)
app.component('MyMany2manySelect', MyMany2manySelect)
app.component('MyTime', MyTime)
app.component('MyDatetime', MyDatetime)
app.component('MySearch', MySearch)
app.component('MyPagination', MyPagination)
app.component('MyDebug', MyDebug)
app.component('MyXmlViewer', MyXmlViewer)
app.component('MyVideo', MyVideo)
app.component('ChatWindow', ChatWindow)
app.component('DocumentList', DocumentList)
app.component('Markdown', MarkdownVue)
app.component('Excalidraw', ExcalidrawEditorVue)
app.component('MyTree', MyTree)

// 使用注册表插件
app.use(RegistryPlugin);
// 动态注册动作
const registry = app.config.globalProperties.$registry;
registerActions(registry);

// 全局变量
app.config.globalProperties.$notify = MyNotification

app.provide('getModuleIcon', getModuleIcon)

window.getToken = getToken
window.console.log = log.info
window.console.debug = log.debug
window.console.info = log.info
window.console.warn = log.warn
window.console.error = log.error

if (import.meta.env.MODE === 'development') {
    log.setLevel('debug')
} else {
    log.setLevel('error')
}


console.log('Vue Version:', version); // 输出 Vue 版本
app.mount('#app')

export default app; // 导出实例