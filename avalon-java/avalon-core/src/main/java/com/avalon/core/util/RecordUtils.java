/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.util;

import com.avalon.core.exception.AvalonException;
import com.avalon.core.model.Record;
import com.avalon.core.service.AbstractService;

import java.util.ArrayList;
import java.util.Map;

public class RecordUtils {
    public static Record convert(ArrayList<Map<String, Object>> src) {

        if (ObjectUtils.isNull(src)) return null;

        Record record = new Record();

        for (Map<String, Object> map : src) {
            record.add(RecordRowUtils.convert(map));
        }
        return record;
    }

    /**
     * 对输入值进行格式化判断
     *
     * @param service
     * @param src
     * @return
     * @throws AvalonException
     */
    public static Record convert(AbstractService service, ArrayList<Map<String, Object>> src)
            throws AvalonException {

        if (ObjectUtils.isNull(src)) return null;

        Record record = new Record();

        for (Map<String, Object> map : src) {
            record.add(RecordRowUtils.convert(service, map));
        }
        return record;
    }
}
