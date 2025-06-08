/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {InputProps} from "../../input/types.ts";


export interface MyMany2oneSelectProps extends InputProps {
    serviceName: string,
    load?: Function // 加载数据函数
}