/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.tree;

import com.avalon.core.field.Field;
import com.avalon.core.service.AbstractService;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SelectOneModel {
    private List<String> fieldList; // 字表查询字段
    private Field field; // 字表引入的字段

    /**
     * 添加字段 重复不添加
     *
     * @param fieldName 字段名
     */
    public void addField(String fieldName) {
        if (fieldList == null) {
            fieldList = new ArrayList<>();
        }

        if (!fieldList.contains(fieldName)) {
            fieldList.add(fieldName);
        }
    }

    private AbstractService service;
}
