/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.service;

import com.avalon.core.condition.Condition;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;

import java.util.List;

public interface IDeleteService {
    Integer delete(Object id) throws AvalonException;//删除指定主键记录 不会检查是否满足删除条件， 直接删除

    Integer delete(Condition condition, String serviceName) throws AvalonException;//条件删除 会检查记录是否存在

    Integer delete(RecordRow row) throws AvalonException;//删除记录 会检查当前记录 及联删除

    Integer deleteMulti(List<Integer> ids) throws AvalonException;//批量删除记录 会检查当前记录 及联删除

    Integer deleteMulti(Record record) throws AvalonException;//批量删除记录 会检查当前记录 及联删除
}
