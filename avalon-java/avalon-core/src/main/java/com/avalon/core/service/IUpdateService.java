/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.service;

import com.avalon.core.condition.Condition;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.util.FieldValue;

public interface IUpdateService {
    Integer update(RecordRow recordRow, Condition condition) throws AvalonException;//更新记录 直接更新

    Integer update(RecordRow recordRow) throws AvalonException;//更新记录 检查满足更新条件 及联更新

    Integer updateMulti(Record record) throws AvalonException;//批量更新

    //更新数值字段 使用 name = name + value
    void updateNumberValue(RecordRow recordRow);
    //更新数值字段 使用 name = name + value
    void updateNumberValue(RecordRow recordRow, Condition condition);
}
