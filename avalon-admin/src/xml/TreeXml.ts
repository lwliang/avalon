import {EditableType} from "./xmlType.ts";

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/06 15:54
 */
export default interface TreeXml extends Record<string, any> {
    editable?: EditableType,
    template: string;
}