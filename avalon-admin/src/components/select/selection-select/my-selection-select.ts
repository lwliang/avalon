/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {InputProps} from "../../input/types.ts";

export interface MySelectionSelectProps extends InputProps {
    serviceName: string,
    field: string,
    isMulti?: boolean
}