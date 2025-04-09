package com.avalon.core.service;

import com.avalon.core.field.FieldList;

/**
 * func: 加载用户自定义的字段
 *
 * @author lwlianghehe@gmail.com
 * @date 2024/12/01 19:31
 */
public interface IExtendFieldService {
    boolean needExtendField(); // 是否需要加载拓展字段

    FieldList getExtendField(); // 获取拓展字段

    void updateExtendField(); // 更新拓展字段
}
