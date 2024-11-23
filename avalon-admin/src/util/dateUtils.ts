/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import dayjs from 'dayjs';

/**
 * 返回参数的年，没有参数，则返回当前年份
 * @param dateString
 */
export function getYear(dateString: string) {
    if (dateString) {
        return dayjs(dateString).year()
    }
    return dayjs().year();
}

export function getHour(dateTimeString?: string) {
    if (dateTimeString) {
        return dayjs(dateTimeString).hour()
    }
    return dayjs().hour();
}

export function getMinute(dateTimeString?: string) {
    if (dateTimeString) {
        return dayjs(dateTimeString).minute()
    }
    return dayjs().minute();
}

// 月份默认加1
export function getMonth(dateString?: string) {
    if (dateString) {
        return dayjs(dateString).month() + 1
    }
    return dayjs().month() + 1;
}

export function getDay(dateString?: string) {
    if (dateString) {
        return dayjs(dateString).date();
    }
    return dayjs().date();
}

/**
 * 获取时间
 * @param datetimeString
 */
export function getTime(datetimeString?: string) {
    if (datetimeString) {
        return dayjs(datetimeString).format(TIME_FORMAT);
    }
    return dayjs().format(TIME_FORMAT);
}

export function getDate(dateString?: string) {
    if (dateString) {
        return dayjs(dateString).format(DATE_FORMAT);
    }
    return dayjs().format(DATE_FORMAT);
}

export function getDateTime(datetimeString?: string) {
    if (datetimeString) {
        return dayjs(datetimeString).format(DATETIME_FORMAT);
    }
    return dayjs().format(DATETIME_FORMAT);
}

export function getDateTimeWithAll(datetimeString?: string) {
    if (datetimeString) {
        return dayjs(datetimeString).format(DATETIME_ALL_FORMAT);
    }
    return dayjs().format(DATETIME_ALL_FORMAT);
}

/**
 * 获取月份第一天是星期几，无参数，则返回当前月份第一天是星期几
 * @param dateString
 */
export function getFirstDayOfMonth(dateString: string) {
    let date = dateString ? dayjs(dateString) : dayjs();

    const firstDayOfMonth = date.startOf('month');

    return firstDayOfMonth.day();
}

/**
 * 获取月份总天数，无参数，则返回当前月份的总天数
 * @param dateString
 */
export function getDaysInMonth(dateString: string) {
    if (dateString) {
        return dayjs(dateString).daysInMonth();
    }
    return dayjs().date();
}

const DATE_FORMAT = 'YYYY-MM-DD';
const TIME_FORMAT = 'HH:mm';
const DATETIME_FORMAT = 'YYYY-MM-DD HH:mm';
const DATETIME_ALL_FORMAT = 'YYYY-MM-DD HH:mm:ss';

export function parseDate(dateString: string): string {
    return dayjs(dateString).format(DATE_FORMAT);
}

export function getCurrentDateString(): string {
    return dayjs().format(DATE_FORMAT);
}