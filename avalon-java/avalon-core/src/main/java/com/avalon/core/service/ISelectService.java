/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.service;

import com.avalon.core.condition.Condition;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.model.*;
import com.avalon.core.model.Record;
import com.avalon.core.util.FieldValue;

public interface ISelectService {
    /**
     * 查询主键集合
     *
     * @param condition
     * @return
     * @throws AvalonException
     */
    Record search(Condition condition) throws AvalonException;

    /**
     * 统计个数
     *
     * @param condition
     * @return
     * @throws AvalonException
     */
    Integer selectCount(Condition condition) throws AvalonException;

    Record select(Condition condition, String... fields) throws AvalonException;

    Record select(String order, Condition condition, String... fields) throws AvalonException;

    Record select(Integer limit, String order, Condition condition, String... fields) throws AvalonException;

    PageInfo selectPage(PageParam pageParam,
                        String order,
                        Condition condition,
                        String... fields) throws AvalonException;

    //获取字段值 从数据库获取
    FieldValue getFieldValue(String fieldName, Condition condition);
}
