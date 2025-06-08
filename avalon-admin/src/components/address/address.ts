/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/11 18:15
 */
import {definePropType} from "../../util/propUtils.ts";

// 省市区
export const AddressType = definePropType<'province' | 'city' | 'district'>(String)
