/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.field;

import com.avalon.core.exception.AvalonException;

import java.sql.Timestamp;

public interface IFieldFormat {
    String getFormat();

    /**
     * 将格式转换为系统需要的格式 比如字符串2022-10-02 08:10:20 转 date
     *
     * @param value
     * @return
     * @throws AvalonException
     */
    Object parse(Object value) throws AvalonException;

    /**
     * 将值转换为 数据库可以接受的形式 比如 date 转字符串 2022-10-02 08:10:20
     *
     * @param value
     * @return
     */
    default Object getSqlValue(Object value) {
        return value;
    }


    default Object getClientValue(Object value) {
        if(value instanceof Timestamp) {
            return ((Timestamp)value).getTime();
        }
        return value;
    }


    /**
     * 将值转换为 报表可以接受的形式 比如 date 转字符串 2022-10-02 08:10
     *
     * @param value
     * @return
     */
    default Object getReportValue(Object value) {
        return value;
    }

}
