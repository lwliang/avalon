/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.service;

import com.avalon.core.exception.AvalonException;
import com.avalon.core.model.PrimaryKey;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;

import java.util.List;

public interface IInsertService {
    PrimaryKey insert(RecordRow recordRow) throws AvalonException;//插入记录 会检查当前记录 及联插入

    List<Object> insertMulti(Record record) throws AvalonException;//批量插入记录 会检查当前记录 及联插入

    // 批量保存，内部根据主键判断新增或更新
    List<Object> saveMulti(Record record) throws AvalonException;
}
