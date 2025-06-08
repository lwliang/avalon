package com.avalon.core.service;

import com.avalon.core.exception.AvalonException;
import com.avalon.core.model.ChangeMethodList;
import com.avalon.core.model.ChangeRecordRow;
import com.avalon.core.model.RecordRow;

import java.util.List;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/07 10:03
 */
public interface IChangeService {
    /**
     * 监听来自前端的变更数据，统一入口
     *
     * @param newRow 变更的值
     * @param oldRow 之前的值
     * @return 需要同步更新的值
     * @throws AvalonException
     */
    ChangeRecordRow onChange(RecordRow changeFieldRow, RecordRow newRow, RecordRow oldRow) throws AvalonException;

    /**
     * 获取需要监听变更的方法
     *
     * @return 方法
     */
    ChangeMethodList getOnChangeMethods();

    /**
     * 获取需要监听变更的字段
     *
     * @return 字段列表
     */
    List<String> getOnChangeFields();
}
