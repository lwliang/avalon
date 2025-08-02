/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {createApp, version} from 'vue'
import {createPinia} from 'pinia'
import router from './router'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import './reset.css'
import './style.css'
import App from './App.vue'
import log from 'loglevel'
import {registerIcons} from "./components/icon";
import MyButton from "./components/button/my-button.vue";
import MyButtonGroup from "./components/button-group/my-button-group.vue";
import MyImage from "./components/image/my-image.vue";
import {getModuleIcon} from "./api/moduleApi.ts";
import MyLabel from "./components/label/my-label.vue";
import MyInput from "./components/input/my-input.vue";
import MyIcon from "./components/icon/my-icon.vue";
import Sheet from "./components/form-layout/sheel/sheet.vue";
import MyRow from "./components/form-layout/row/my-row.vue";
import MyCol from "./components/form-layout/col/my-col.vue";
import MySelectionSelect from "./components/select/selection-select/my-selection-select.vue";
import MyMany2OneSelect from "./components/select/many2one-select/my-many2one-select.vue";
import MyTabs from "./components/tabs/my-tabs.vue";
import MySubTree from './pages/window/action/form/my-sub-tree.vue';
import MyFormDialog from "./components/model/form-dialog/my-form-dialog.vue";
import MyImageUpload from "./components/upload/my-image-upload.vue";
import MyPassword from "./components/password/my-password.vue";
import MyDate from "./components/date/my-date.vue";
import MyCheckBox from "./components/checkbox/my-check-box.vue";
import MyCheckBoxGroup from "./components/checkbox-group/my-check-box-group.vue";
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
import RegistryPlugin from "./global/registry/registryPlugin.ts";
import {registerActions} from './global/registry/actions'
import MarkdownVue from "./components/markdown/markdown.vue";
import ExcalidrawEditorVue from "./components/excalidraw/ExcalidrawVue.vue";
import Address from "./components/address/address.vue";
import MyText from "./components/text/my-text.vue";
import MyRadio from "./components/radio/my-radio.vue";
import MyRadioGroup from "./components/radio-group/my-radio-group.vue";
import MySwitch from "./components/switch/my-switch.vue";
import MyCard from './components/card/my-card.vue'
import MyProgress from "./components/progress/my-progress.vue";
import dayjs from 'dayjs'
import isSameOrAfter from 'dayjs/plugin/isSameOrAfter'
import isSameOrBefore from 'dayjs/plugin/isSameOrBefore'
import customParseFormat from 'dayjs/plugin/customParseFormat'
import MyPopover from './components/popover/my-popover.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import myJson from './components/json/my-json.vue'
import MyHtml from './components/html/my-html.vue'
import ReportTable from './components/table/report-table.vue'
import ReportEchart from './components/report/report-echart.vue'


// 注册插件
dayjs.extend(isSameOrAfter)
dayjs.extend(isSameOrBefore)
dayjs.extend(customParseFormat)


window.ReconnectingWebSocket = ReconnectingWebSocket;


const app = createApp(App)
registerIcons(app)

app.use(createPinia())
app.use(router)
app.component('MyButton', MyButton)
app.component('MyButtonGroup', MyButtonGroup)
app.component('MyImage', MyImage)
app.component('MyAvatar', MyAvatar)
app.component('MyLabel', MyLabel)
app.component('MyInput', MyInput)
app.component('MyIcon', MyIcon)
app.component('Sheet', Sheet)
app.component('MyRow', MyRow)
app.component('MyCol', MyCol)
app.component('MySelectionSelect', MySelectionSelect)
app.component('MyMany2OneSelect', MyMany2OneSelect)
app.component('MyTabs', MyTabs)
app.component('MySubTree', MySubTree)
app.component('MyFormDialog', MyFormDialog)
app.component('MyImageUpload', MyImageUpload)
app.component('MyVideoUpload', MyVideoUpload)
app.component('MyPassword', MyPassword)
app.component('MyDate', MyDate)
app.component('MyCheckBox', MyCheckBox)
app.component('MyCheckBoxGroup', MyCheckBoxGroup)
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
app.component('MyAddress', Address)
app.component('MyText', MyText)
app.component('MyRadio', MyRadio)
app.component('MyRadioGroup', MyRadioGroup)
app.component('MySwitch', MySwitch)
app.component('MyCard', MyCard)
app.component('MyProgress', MyProgress)
app.component('MyPopover', MyPopover)
app.component('MyJson', myJson)
app.component('MyHtml', MyHtml)
app.component('ReportTable', ReportTable)
app.component('ReportEchart', ReportEchart)

// 使用注册表插件
app.use(RegistryPlugin);
// 动态注册动作
const registry = app.config.globalProperties.$registry;
registerActions(registry);


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

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.use(ElementPlus)
console.log('Vue Version:', version); // 输出 Vue 版本
app.mount('#app')

export default app; // 导出实例