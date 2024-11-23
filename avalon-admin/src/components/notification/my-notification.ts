/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {definePropType} from "../../util/propUtils.ts";
export const notificationType = definePropType<'success' | 'error' | 'warning' | 'info'>(String)