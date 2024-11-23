/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {isArray as _isArray} from 'lodash'
import {isString as _isString} from 'lodash'
import {isNull as _isNull} from "lodash";
import {isNumber as _isNumber} from "lodash";
import {isBoolean as _isBoolean} from "lodash";
import {isObject as _isObject} from 'lodash'
import {isFunction as _isFunction} from "lodash";
import {isUndefined as _isUndefined} from "lodash";

export function isString(object: any): boolean {
    return _isString(object)
}

export function isNumber(object: any): boolean {
    return _isNumber(object)
}

export function isBoolean(object: any): boolean {
    return _isBoolean(object)
}

export function isObject(object: any): boolean {
    return _isObject(object)
}

export function isArray(object: any): boolean {
    return _isArray(object)
}

export function isFunction(object: any): boolean {
    return _isFunction(object)
}

export function isUndefined(object: any): boolean {
    return _isUndefined(object)
}

export function isNull(object: any): boolean {
    return _isNull(object)
}

export function isNullOrUndefined(object: any): boolean {
    return isUndefined(object) || isNull(object)
}