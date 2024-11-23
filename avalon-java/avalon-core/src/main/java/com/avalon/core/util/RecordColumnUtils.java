/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.util;

import com.avalon.core.model.RecordColumn;

public class RecordColumnUtils {
    /**
     * 判断字段值是否为空
     *
     * @param recordColumn
     * @return
     */
    public static Boolean isEmpty(RecordColumn recordColumn) {

        return ObjectUtils.isNull(recordColumn) || recordColumn.isEmpty();
    }
}
