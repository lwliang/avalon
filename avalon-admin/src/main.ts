/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {createApp} from 'vue'
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
import MyUpload from "./components/upload/my-upload.vue";
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


const app = createApp(App)
loadSvg(app)

app.use(createPinia())
app.use(router)
app.component('MyButton', MyButton)
app.component('MyImage', MyImage)
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
app.component('MyUpload', MyUpload)
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

// 全局变量
app.config.globalProperties.$notify = MyNotification

app.provide('getModuleIcon', getModuleIcon)

app.mount('#app')