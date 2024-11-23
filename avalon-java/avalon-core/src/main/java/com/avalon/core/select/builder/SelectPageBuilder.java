/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.select.builder;

import com.avalon.core.model.Record;
import com.avalon.core.service.AbstractService;

/**
 * 分页查询构建器 生存sql语句
 */

public class SelectPageBuilder extends SelectBuilder {
    private Integer pageNum;
    private Integer pageSize;

    public SelectPageBuilder(AbstractService service) {
        super(service);
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public QueryStatement getSql() {
        QueryStatement sql = super.getSql();
        if (pageNum != null && pageSize != null) {
            sql.getSql().append(" LIMIT ").append(pageSize).append(" OFFSET ").append((pageNum - 1) * pageSize);
        }
        return sql;
    }

    @Override
    public QueryStatement getMasterSql() {
        QueryStatement masterSql = super.getMasterSql();
        if (pageNum != null && pageSize != null) {
            masterSql.getSql().append(" LIMIT ").append(pageSize).append(" OFFSET ").append((pageNum - 1) * pageSize);
        }
        return masterSql;
    }

    @Override
    public QueryStatement getMasterIds() {
        QueryStatement masterSql = super.getMasterIds();
        if (pageNum != null && pageSize != null) {
            masterSql.getSql().append(" LIMIT ").append(pageSize).append(" OFFSET ").append((pageNum - 1) * pageSize);
        }
        return masterSql;
    }

    public SelectPageBuilder(SelectBuilder selectBuilder, Integer pageNum, Integer pageSize) {
        super();
        setService(selectBuilder.getService());
        setAliasSupport(selectBuilder.getAliasSupport());
        setFieldValueStatement(selectBuilder.getFieldValueStatement());
        setPageNum(pageNum);
        setPageSize(pageSize);
        setDistinct(selectBuilder.getDistinct());
        setQueryRoot(selectBuilder.getQueryRoot());
        setOrderBy(selectBuilder.getOrderBy());
        setCondition(selectBuilder.getCondition());
    }
}
