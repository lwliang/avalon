/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.select.builder;

import com.avalon.core.condition.Condition;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import com.avalon.core.service.AbstractTreeService;
import com.avalon.core.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 树状结构 分页查询构建器 生存sql语句
 */
public class SelectTreeBuilder extends SelectPageBuilder {

    public SelectTreeBuilder(AbstractService service) {
        super(service);
    }

    public SelectTreeBuilder(SelectBuilder selectBuilder, Integer pageNum, Integer pageSize) {
        super(selectBuilder, pageNum, pageSize);
    }

    @Override
    public QueryStatement getSql() {
        return super.getSql();
    }

    /**
     * 增加父id的值
     *
     * @param parentIdRecord 里面需要有id的值
     */
    public void addParentPathCondition(Record parentIdRecord) {
    }

    /**
     * 获取第几页的主键列表
     *
     * @return
     */
    public StringBuilder getPageParentId() {
        StringBuilder sql = new StringBuilder();
        if (!(getService() instanceof AbstractTreeService)) {
            throw new AvalonException("不支持树状图显示");
        }
        AbstractTreeService service = (AbstractTreeService) getService();
        sql.append("select distinct id from ( ");
        sql.append("select id from ( ");
        sql.append(super.getAllSql());
        sql.append(" ) as p ");
        sql.append("union ");
        sql.append("select ").append(service.getParentIdField().getFieldName()).append(" from ( ");
        sql.append(super.getAllSql());
        sql.append(" ) as y where y.").append(service.getParentIdField().getFieldName()).append(" is not null ");
        sql.append(" ) as z ");

        if (getPageNum() != null && getPageSize() != null) {
            sql.append(" LIMIT ").append(getPageSize()).append(" OFFSET ")
                    .append((getPageNum() - 1) * getPageSize());
        }
        return sql;
    }
}
