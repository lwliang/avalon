/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.service;

import com.avalon.core.condition.Condition;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.model.PageInfo;
import com.avalon.core.model.PageParam;
import com.avalon.core.model.Record;

/**
 * 服务可以支持树结构的接口
 * 固定 字段 parentId 上级id，parentPath 上级路径 1,2;
 */
public interface ITreeSupport {
    PageInfo selectTreePage(PageParam pageParam,
                            Condition condition,
                            String childName,
                            String order,
                            String... fields) throws AvalonException;
    /**
     * 获取上级
     */
    Field getParentIdField();

    /**
     * 获取上级路径 格式 ,1,2, 顶级 ,
     */
    Field getParentPathField();
    /**
     * 获取树节点深度 顶级是1
     */
    Field getLevelField();
}
