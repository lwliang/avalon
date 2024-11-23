/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

/* import the fontawesome core */
import {library} from '@fortawesome/fontawesome-svg-core'

/* import font awesome icon component */
import {FontAwesomeIcon} from '@fortawesome/vue-fontawesome'

/* import specific icons */
import {fas} from '@fortawesome/free-solid-svg-icons'
import {far} from '@fortawesome/free-regular-svg-icons'
import {fab} from '@fortawesome/free-brands-svg-icons'
import {App} from "vue";
import {definePropType} from "../../util/propUtils.ts";

/* add icons to the library */
library.add(fas, far, fab)

export function loadSvg(app: App) {
    app.component('font-awesome-icon', FontAwesomeIcon)
}

export const iconStyleType = definePropType<'fas' | 'fab' | 'far'>(String)
export const iconSizeType = definePropType<'2xs' | 'xs' | 'sm' | 'lg' | 'xl' | '2xl' | '1x'
    | '2x' | '3x' | '4x' | '5x' | '6x' | '7x' | '8x' | '9x' | '10x'>(String)

export const IconProp = {
    icon: String,
    size: {
        type: iconSizeType,
        default: '1x'
    },
    color: String,
    type: {
        type: iconStyleType,
        default: 'far'
    }
}