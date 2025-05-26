/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

/* import the fontawesome core */
import {library} from '@fortawesome/fontawesome-svg-core'

/* import font awesome icon component */
import {FontAwesomeIcon, FontAwesomeLayers, FontAwesomeLayersText} from '@fortawesome/vue-fontawesome'


/* import specific icons */
import {fas} from '@fortawesome/free-solid-svg-icons'
import {far} from '@fortawesome/free-regular-svg-icons'
import {fab} from '@fortawesome/free-brands-svg-icons'
import {App} from "vue";
import {definePropType} from "../../util/propUtils.ts";

/* add icons to the library */
library.add(fas, far, fab)

export function registerIcons(app: App) {
    app.component('font-awesome-icon', FontAwesomeIcon)
        .component('font-awesome-layers', FontAwesomeLayers)
        .component('font-awesome-layer-text', FontAwesomeLayersText)
}
