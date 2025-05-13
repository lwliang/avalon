/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

export function isWhitespace(char: string) {
    return /^\s*$/.test(char)
}

export function stringToBool(str: string) {
    return str.toLowerCase() === "true";
}

//简单字符串替换（适合无嵌套、无转义的简单场景）
export function toStandardJsonString(str: string) {
    // 1. 全部单引号换成双引号
    let s = str.replace(/'/g, '"');
    // 2. key 没有引号的情况（如 {type: 'district'}），用正则加上
    s = s.replace(/([{,]\s*)([a-zA-Z0-9_]+)\s*:/g, '$1"$2":');
    return s;
}