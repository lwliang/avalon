/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.model;

import com.avalon.core.field.*;
import com.avalon.core.alias.AliasMap;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.FieldUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存关联字段
 */
public class JoinRelationMap {
    private final AvalonHashSet existTable = new AvalonHashSet();//判断是否重复表

    public Map<String, RelationField> getRelationFieldMap() {
        return relationFieldMap;
    }

    private final Map<String, RelationField> relationFieldMap = new HashMap<>();//字段名 与 字段定义

    public Map<String, RelationField> getTableRelationMap() {
        return tableRelationMap;
    }

    private final Map<String, RelationField> tableRelationMap = new HashMap<>();//表名 与 字段定义

    private final AliasMap aliasMap = new AliasMap();

    public AbstractService getService() {
        return service;
    }

    public void setService(AbstractService service) {
        this.service = service;
    }

    private AbstractService service;

    public JoinRelationMap(AbstractService service) {
        super();
        this.service = service;
    }


    public String getRelationTableName(String relationFieldName) {

        if (relationFieldMap.containsKey(relationFieldName)) {
            return relationFieldMap.get(relationFieldName).getRelativeServiceName();
        }

        Field field = service.getField(relationFieldName);

        if (field instanceof RelationField) {
            relationFieldMap.put(relationFieldName, (RelationField) field);
            return ((RelationField) field).getRelativeServiceName();
        }

        return null;
    }

    public Field getRelationFieldName(String field) {
        return service.getField(field);
    }


    /**
     * 获取及联查询表
     *
     * @return
     */
    public StringBuilder getFromTableSql() {
        StringBuilder sb = new StringBuilder();
        existTable.clear();
        for (Map.Entry<String, RelationField> fieldEntry : relationFieldMap.entrySet()) {
            String fieldName = fieldEntry.getKey();
            if (FieldUtils.hasJoinSelect(fieldName)) {//多级
                List<String> fieldList = FieldUtils.getJoinStringList(fieldName);
                AbstractService leftService = service;
                sb.append(leftService.getServiceTableName());
                for (int i = 0; i < fieldList.size(); i++) {
                    if (!existTable.add(fieldList.get(i))) {
                        continue;
                    }
                    RelationField relationField = (RelationField) service.getField(FieldUtils.getJoinString(fieldList, i));
                    sb.append(" LEFT JOIN ");
                    String rightTable = Fields.dot2UnderscoreName(relationField.getRelativeServiceName());
                    sb.append(FieldUtils.underscoreName(rightTable));
                    sb.append(" ON ");
                    if (relationField instanceof Many2manyField ||
                            relationField instanceof Many2oneField) {
                        sb.append(leftService.getServiceTableName() + FieldUtils.getJoinDivision() +
                                relationField.getFieldName());
                        sb.append(" = ");
                        sb.append(Fields.dot2UnderscoreName(rightTable) + FieldUtils.getJoinDivision()
                                + Fields.underscoreName(service.getServiceBean(
                                relationField.getRelativeServiceName()).getPrimaryKeyName()));
                    } else {
                        sb.append(leftService.getServiceTableName() + FieldUtils.getJoinDivision() +
                                leftService.getPrimaryKeyField().getFieldName());
                        sb.append(" = ");
                        sb.append(Fields.dot2UnderscoreName(rightTable) + FieldUtils.getJoinDivision()
                                + Fields.underscoreName(relationField.getRelativeFieldName()));
                    }
                    leftService = leftService.getServiceBean(relationField.getRelativeServiceName());
                }
            } else {//一级
                if (!existTable.add(fieldName)) {
                    continue;
                }
                RelationField relationField = fieldEntry.getValue();
                if (sb.length() == 0) {
                    sb.append(service.getServiceTableName());
                }
                sb.append(" LEFT JOIN ");
                String relationTable = Fields.dot2UnderscoreName(fieldEntry.getValue().getRelativeServiceName());
                sb.append(FieldUtils.underscoreName(relationTable));
                sb.append(" ON ");

                if (relationField instanceof Many2manyField ||
                        relationField instanceof Many2oneField) {
                    sb.append(service.getServiceTableName() + FieldUtils.getJoinDivision() +
                            relationField.getFieldName());
                    sb.append(" = ");
                    sb.append(Fields.dot2UnderscoreName(relationTable) + FieldUtils.getJoinDivision()
                            + Fields.underscoreName(service.getServiceBean(
                            relationField.getRelativeServiceName()).getPrimaryKeyName()));
                } else {
                    sb.append(service.getServiceTableName() + FieldUtils.getJoinDivision() +
                            service.getPrimaryKeyField().getFieldName());
                    sb.append(" = ");
                    sb.append(Fields.dot2UnderscoreName(relationTable) + FieldUtils.getJoinDivision()
                            + Fields.underscoreName(fieldEntry.getValue().getRelativeFieldName()));
                }
            }

        }

        return sb;
    }
}
