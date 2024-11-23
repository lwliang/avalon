/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import type {PropType} from 'vue'
import {fromPairs} from "lodash";

export const definePropType = <T>(val: any): PropType<T> => val
