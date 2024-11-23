/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.select.builder;

import com.avalon.core.condition.AndCondition;
import com.avalon.core.condition.Condition;
import com.avalon.core.condition.OrCondition;
import com.avalon.core.model.FieldValueStatement;
import com.avalon.core.select.*;
import com.avalon.core.service.AbstractService;
import com.avalon.core.tree.QueryNode;
import com.avalon.core.util.FieldUtils;
import com.avalon.core.util.ObjectUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class SelectBuilder {
    private String orderBy;
    private Boolean isDistinct = true;//是否去重
    private QueryNode queryRoot;
    private Condition condition;

    /*辅助缓存*/
    private SelectAliasSupport aliasSupport;
    private AbstractService service;
    private FieldValueStatement fieldValueStatement;

    public Boolean getDistinct() {
        return isDistinct;
    }

    public void setDistinct(Boolean distinct) {
        isDistinct = distinct;
    }

    protected void setAliasSupport(SelectAliasSupport aliasSupport) {
        this.aliasSupport = aliasSupport;
    }

    protected void setService(AbstractService service) {
        this.service = service;
    }

    protected void setFieldValueStatement(FieldValueStatement fieldValueStatement) {
        this.fieldValueStatement = fieldValueStatement;
    }

    public String getAliasDelimiter() {
        if (service.getContext().isMysql()) {
            return ".";
        }
        return "$";
    }

    protected SelectBuilder() {

    }

    public SelectBuilder(AbstractService service) {
        this.aliasSupport = new SelectAliasSupport();
        this.service = service;
        this.fieldValueStatement = new FieldValueStatement();
    }

    public void addField(String field) {
        if (ObjectUtils.isNull(queryRoot)) {
            queryRoot = new QueryNode();
            queryRoot.setService(service);
        }
        queryRoot.addField(field);
    }

    public QueryNode getAliasNode(String aliasTable) {
        return queryRoot.getAliasNode(aliasTable);
    }

    public QueryStatement getMasterSql(Integer limit) {
        fieldValueStatement.clear();
        List<String> selects = new ArrayList<>();
        List<String> tables = new ArrayList<>();
        queryNodeTables(queryRoot, tables);
        selects.add(queryRoot.getSelectField(aliasSupport));
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        if (ObjectUtils.isNotNull(getDistinct()) && getDistinct()) {
            sql.append("DISTINCT ");
        }
        int count = 0;
        for (String select : selects) {
            if (select.trim().isEmpty()) {
                continue;
            }
            if (count > 0) {
                sql.append(",");
            }

            sql.append(select);
            count++;

        }
        sql.append(" FROM ");
        for (String table : tables) {
            if (!table.trim().isEmpty()) {
                sql.append(table);
            }
        }
        if (ObjectUtils.isNotNull(condition)) {
            sql.append(" WHERE ");
            sql.append(getConditionSql(condition));
        }

        if (!orderBy.isEmpty()) {
            sql.append(" ORDER BY ");
            sql.append(getOrderSql(queryRoot));
        }

        if (ObjectUtils.isNotNull(limit) && !limit.equals(0)) {
            sql.append(" LIMIT ");
            sql.append(limit);
        }

        QueryStatement queryStatement = new QueryStatement();
        queryStatement.setSql(sql);
        queryStatement.setValueStatement((FieldValueStatement) fieldValueStatement.clone());
        return queryStatement;
    }

    /**
     * 获取主表的字段sql
     *
     * @return sql
     */
    public QueryStatement getMasterSql() {
        return getMasterSql(null);
    }

    public QueryStatement getMasterIds() {
        fieldValueStatement.clear();
        List<String> tables = new ArrayList<>();
        queryNodeTables(queryRoot, tables);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        if (ObjectUtils.isNotNull(getDistinct()) && getDistinct()) {
            sql.append("DISTINCT ");
        }
        String alias = queryRoot.getAlias(aliasSupport);
        sql.append(alias)
                .append(".")
                .append(service.getPrimaryKeyName());
        if (service.getContext().isMysql()) {
            sql.append(" AS ")
                    .append("'")
                    .append(alias)
                    .append(FieldUtils.getJoinDivision())
                    .append(service.getPrimaryKeyName())
                    .append("'");
        } else {
            sql.append(" AS ")
                    .append("\"")
                    .append(alias)
                    .append(FieldUtils.getJoinDivision())
                    .append(service.getPrimaryKeyName())
                    .append("\"");
        }

        sql.append(" FROM ");
        for (String table : tables) {
            if (!table.trim().isEmpty()) {
                sql.append(table);
            }
        }
        if (ObjectUtils.isNotNull(condition)) {
            sql.append(" WHERE ");
            sql.append(getConditionSql(condition));
        }

        if (!orderBy.isEmpty()) {
            sql.append(" ORDER BY ");
            sql.append(getOrderSql(queryRoot));
        }
        QueryStatement queryStatement = new QueryStatement();
        queryStatement.setSql(sql);
        queryStatement.setValueStatement((FieldValueStatement) fieldValueStatement.clone());
        return queryStatement;
    }

    /**
     * 获取完整的sql语句
     *
     * @param node 需要查询的根节点
     * @return
     */
    private StringBuilder getCompleteSql(QueryNode node) {
        List<String> selects = new ArrayList<>();
        List<String> tables = new ArrayList<>();
        queryNode(node, selects, tables);

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        if (ObjectUtils.isNotNull(getDistinct()) && getDistinct()) {
            sql.append("DISTINCT ");
        }
        int count = 0;
        for (String select : selects) {
            if (select.trim().isEmpty()) {
                continue;
            }
            if (count > 0) {
                sql.append(",");
            }

            sql.append(select);
            count++;

        }
        sql.append(" FROM ");
        for (String table : tables) {
            if (!table.trim().isEmpty()) {
                sql.append(table);
            }
        }
        if (ObjectUtils.isNotNull(condition)) {
            sql.append(" WHERE ");
            sql.append(getConditionSql(condition));
        }

        if (!orderBy.isEmpty()) {
            sql.append(" ORDER BY ");
            sql.append(getOrderSql(node));
        }
        return sql;
    }

    private String getConditionSql(Condition condition) {
        return condition.getSql(this);
    }

    private String getOrderSql(QueryNode node) {
        List<String> orderList = FieldUtils.getFieldList(orderBy);
        StringBuilder orderBySql = new StringBuilder();
        for (String s : orderList) {
            String[] s1 = s.split(" ");
            String field = s1[0];
            String asc = "asc";
            if (s1.length > 1) {
                asc = s1[1];
            }
            if (!orderBySql.isEmpty()) {
                orderBySql.append(",");
            }
            orderBySql.append(node.getFieldWithAliasTable(field, aliasSupport))
                    .append(" ")
                    .append(asc.toUpperCase());
        }

        return orderBySql.toString();
    }

    private void queryNodeTables(QueryNode node, List<String> tables) {
        tables.add(node.getTable(aliasSupport));

        for (QueryNode queryNode : node.getQueryNodeList()) {
            queryNodeTables(queryNode, tables);
        }
    }

    private void queryNode(QueryNode node, List<String> selects, List<String> tables) {
        selects.add(node.getSelectField(aliasSupport));
        tables.add(node.getTable(aliasSupport));

        for (QueryNode queryNode : node.getQueryNodeList()) {
            queryNode(queryNode, selects, tables);
        }
    }

    /**
     * 获取全部记录sql语句
     *
     * @return
     */
    protected QueryStatement getAllSql() {
        fieldValueStatement.clear();
        StringBuilder completeSql = getCompleteSql(queryRoot);
        QueryStatement queryStatement = new QueryStatement();
        queryStatement.setSql(completeSql);
        queryStatement.setValueStatement((FieldValueStatement) fieldValueStatement.clone());
        return queryStatement;
    }

    /**
     * 获取当前查询sql语句
     */
    public QueryStatement getSql() {
        return getAllSql();
    }

    public void completeTable(Condition condition) {
        if (condition instanceof AndCondition) {
            AndCondition andCondition = (AndCondition) condition;
            completeTable(andCondition.getLeftCondition());
            completeTable(andCondition.getRightCondition());
        } else if (condition instanceof OrCondition) {
            OrCondition orCondition = (OrCondition) condition;
            completeTable(orCondition.getLeftCondition());
            completeTable(orCondition.getRightCondition());
        } else {
            String field = condition.getRealName();
            String joinTableString = FieldUtils.getJoinTableString(field);
            if (ObjectUtils.isNotEmpty(joinTableString)) {
                addField(joinTableString);
            }
        }
    }


    /**
     * 获取个数的sql语句
     *
     * @return
     */
    public QueryStatement getCountSql() {
        QueryStatement masterSql = getMasterSql();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("COUNT(*)");
        sb.append(" FROM( ");
        sb.append(masterSql.getSql());
        sb.append(")");
        sb.append(" AS ZX");
        masterSql.setSql(sb);
        return masterSql;
    }

    public QueryStatement getSumSql() {
        // 只能查询第一个字段
        QueryStatement masterSql = getMasterSql();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("SUM( ZX.\"");
        String alias = aliasSupport.getAlias(queryRoot);
        sb.append(alias)
                .append(FieldUtils.getJoinDivision());
        sb.append(queryRoot.getFields().get(0).getFieldName());
        sb.append("\")");
        sb.append(" as ");
        sb.append(" \" ");
        sb.append(queryRoot.getFields().get(0).getName());
        sb.append(" \" ");
        sb.append(" FROM( ");
        sb.append(masterSql.getSql());
        sb.append(")");
        sb.append(" AS ZX");
        masterSql.setSql(sb);
        return masterSql;
    }
}
