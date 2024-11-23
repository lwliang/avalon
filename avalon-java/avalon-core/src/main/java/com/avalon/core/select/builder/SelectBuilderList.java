/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.select.builder;


import com.avalon.core.model.FieldValueStatement;
import com.avalon.core.model.PageParam;

import java.util.ArrayList;

public class SelectBuilderList extends ArrayList<SelectBuilder> {

    public StringBuilder getTableAllSql() {
        StringBuilder sqlBuilder = new StringBuilder();
        for (SelectBuilder selectBuilder : this) {
            if (sqlBuilder.length() == 0) {
                sqlBuilder.append(selectBuilder.getSql());
            } else {
                sqlBuilder.append("union all");
            }
            sqlBuilder.append(selectBuilder.getSql());
        }
        return sqlBuilder;
    }

    public StringBuilder getTablePageSql(PageParam pageParam) {
        StringBuilder tableAllSql = getTableAllSql();
        tableAllSql.append(" LIMIT ")
                .append(pageParam.getPageSize())
                .append(" OFFSET ")
                .append((pageParam.getPageNum() - 1) * pageParam.getPageSize());

        return tableAllSql;
    }


    public FieldValueStatement getTableFieldValueStatement() {
        FieldValueStatement fieldValueStatement = new FieldValueStatement();
        for (SelectBuilder selectBuilder : this) {
            fieldValueStatement.addAll(selectBuilder.getFieldValueStatement());
        }
        return fieldValueStatement;
    }
}