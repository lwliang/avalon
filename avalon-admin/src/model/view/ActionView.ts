/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {ViewModeEnum} from "../enum-type/ViewType.ts";

export default interface ActionView {
    id: number,
    name: string,
    label: string,
    serviceId: number,
    videMode: ViewModeEnum
    priority: number,
    arch: string
}