/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.tree;

import com.avalon.core.alias.DefaultAliasSupport;
import com.avalon.core.alias.IAliasRequire;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.*;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.FieldUtils;
import com.avalon.core.util.ObjectUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
public class QueryNode implements IAliasRequire {
    private Field field; //第一级字段
    private String tableField; //表字段
    private QueryNodeList queryNodeList; //子节点
    private String alias; //别名
    private AbstractService service; //服务
    private List<String> originalFields; //原始字段 用于查询
    private List<Field> fields; //
    private QueryNode parentNode; //父节点
    private String aggregation; // 聚合函数 目前只能支持一级 sum(field) as field 未完成
    private final static String[] AggregationList = new String[]{"sum\\((.*)\\)"};

    public QueryNode() {
        queryNodeList = new QueryNodeList();
        originalFields = new ArrayList<>();
        fields = new ArrayList<>();
    }

    @Override
    public String getAlias(DefaultAliasSupport defaultAliasSupport) {
        if (ObjectUtils.isNotEmpty(alias)) {
            return alias;
        }
        alias = defaultAliasSupport.getAlias(this);
        return alias;
    }

    /**
     * @param fieldName
     */
    public void addField(String fieldName) {
        if (FieldUtils.hasJoinSelect(fieldName)) {
            String tableName = FieldUtils.getJoinFirstTableString(fieldName);
            QueryNode queryNode = addTable(tableName);
            queryNode.addField(FieldUtils.getJoinFirstFieldString(fieldName));
        } else {
            Field field1 = service.getField(fieldName);
            if (ObjectUtils.isNull(field1)) {
                throw new AvalonException("字段不存在:" + fieldName);
            }
            if (!FieldUtils.isVirtualRelation(field1)) {
                originalFields.add(fieldName);
                fields.add(field1);
                if(field1 instanceof Many2oneField) {
                    addTable(fieldName);
                }
            } else {
                addTable(fieldName);
            }
        }
    }

    private QueryNode addTable(String tableName) {
        QueryNode queryNode = queryNodeList.getNodeByTable(tableName);
        if (ObjectUtils.isNull(queryNode)) {
            queryNode = new QueryNode();
            queryNodeList.add(queryNode);
            RelationField relationField = (RelationField) service.getField(tableName);
            queryNode.setService(relationField.getRealService());
            queryNode.setField(relationField);
            queryNode.setTableField(tableName);
            queryNode.setParentNode(this);
        }
        return queryNode;
    }


    public String getSelectField(DefaultAliasSupport defaultAliasSupport) {
        StringBuilder selectFields = new StringBuilder();
        for (Field field : fields) {
            if (!selectFields.isEmpty()) {
                selectFields.append(",");
            }
            String alias = getAlias(defaultAliasSupport);
            selectFields.append(alias)
                    .append(FieldUtils.getJoinDivision())
                    .append(field.getFieldName());

            if (service.getContext().isMysql()) {
                selectFields.append(" AS ")
                        .append("'")
                        .append(alias)
                        .append(FieldUtils.getJoinDivision())
                        .append(field.getFieldName())
                        .append("'");
            } else {
                selectFields.append(" AS ")
                        .append("\"")
                        .append(alias)
                        .append(FieldUtils.getJoinDivision())
                        .append(field.getFieldName())
                        .append("\"");
            }
        }
        return selectFields.toString();
    }

    public String getTable(DefaultAliasSupport defaultAliasSupport) {
        if (ObjectUtils.isEmpty(parentNode)) {
            return getService().getServiceTableName() + " " + getAlias(defaultAliasSupport);
        }
        String tables = "";
        if (field instanceof One2oneField || field instanceof One2manyField) {
            RelationField relationField = ((RelationField) field);
            tables = " left join " +
                    getService().getServiceTableName() + " " + getAlias(defaultAliasSupport) +
                    " on " + getAlias(defaultAliasSupport) + FieldUtils.getJoinDivision() +
                    Fields.underscoreName(relationField.getRelativeFieldName()) +
                    " = " + parentNode.getAlias(defaultAliasSupport) + FieldUtils.getJoinDivision() + parentNode.getService().getPrimaryKeyName();
        } else if (field instanceof Many2oneField) {
            tables = " left join " +
                    getService().getServiceTableName() + " " + getAlias(defaultAliasSupport) +
                    " on " + getAlias(defaultAliasSupport) + FieldUtils.getJoinDivision() + service.getPrimaryKeyName() +
                    " = " + parentNode.getAlias(defaultAliasSupport) + FieldUtils.getJoinDivision() + field.getFieldName();
        } else if (field instanceof Many2manyField) {
            Many2manyField relationField = ((Many2manyField) field);
            tables = " left join " +
                    getService().getServiceTableName() + " " + getAlias(defaultAliasSupport) +
                    " on " + getAlias(defaultAliasSupport) + FieldUtils.getJoinDivision() + relationField.getMasterForeignSqlKeyName() +
                    " = " + parentNode.getAlias(defaultAliasSupport) + FieldUtils.getJoinDivision() + parentNode.getService().getPrimaryKeyName();
        }
        return tables;
    }

    public String getFieldWithAliasTable(String field, DefaultAliasSupport defaultAliasSupport) {
        if (!FieldUtils.hasJoinSelect(field)) {
            return getAlias(defaultAliasSupport) + FieldUtils.getJoinDivision() + FieldUtils.underscoreName(field);
        }
        String tableName = FieldUtils.getJoinFirstTableString(field);
        QueryNode queryNode = queryNodeList.getNodeByTable(tableName);
        return queryNode.getFieldWithAliasTable(FieldUtils.getJoinFirstFieldString(field), defaultAliasSupport);
    }

    public QueryNode getAliasNode(String alias) {
        if (alias.equals(this.alias)) {
            return this;
        }
        for (QueryNode queryNode : queryNodeList) {
            QueryNode node = queryNode.getAliasNode(alias);
            if (ObjectUtils.isNotNull(node)) {
                return node;
            }
        }
        return null;
    }
}
