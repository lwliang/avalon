/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.db;

import com.avalon.core.context.Context;
import com.avalon.core.model.FieldValueStatement;
import com.avalon.core.model.Record;
import com.avalon.core.select.builder.SelectBuilder;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DynamicJdbcTemplate {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private Context context;

    public Record select(String sql) {
        if (context.getApplicationConfig().getDebug()) {
            log.debug("db {} select sql -> {}", context.getBaseName(), sql);
        }
        DataReaderRowDefaultHandler dataReaderHandler = new DataReaderRowDefaultHandler();
        jdbcTemplate.query(sql, dataReaderHandler);

        return dataReaderHandler.getRecord();
    }

    public Record select(AvalonPreparedStatement psc,
                         SelectBuilder selectBuilder) {
        if (context.getApplicationConfig().getDebug()) {
            log.debug("db {} select sql -> {}", context.getBaseName(), psc.toString());
        }
        DataReaderRowHandler dataReaderHandler = new DataReaderRowHandler(selectBuilder);
        jdbcTemplate.query(psc, dataReaderHandler);

        return dataReaderHandler.getRecord();
    }


    public Integer selectCount(StringBuilder sql) {
        String s = sql.toString();
        if (context.getApplicationConfig().getDebug()) {
            log.debug("db {} select sql -> {}", context.getBaseName(), s);
        }
        return jdbcTemplate.queryForObject(s, Integer.class);
    }

    public Integer selectCount(StringBuilder sql, FieldValueStatement fieldValueStatement) {
        if (context.getApplicationConfig().getDebug()) {
            log.debug("db {} select sql -> {} value {}", context.getBaseName(), sql.toString(), fieldValueStatement.toString());
        }

        return jdbcTemplate.queryForObject(sql.toString(),
                fieldValueStatement.flatValue(),
                fieldValueStatement.flatType(),
                Integer.class);
    }

    /**
     * 删除记录
     *
     * @param sql
     * @return 返回受影响的行数
     */
    public Integer delete(StringBuilder sql) {
        String s = sql.toString();
        if (context.getApplicationConfig().getDebug()) {
            log.debug("db {} delete sql -> {}", context.getBaseName(), s);
        }
        return jdbcTemplate.update(s);
    }

    /**
     * 新增记录
     *
     * @param sql
     * @return 返回受影响的行数
     */
    public Integer insert(StringBuilder sql) {
        String s = sql.toString();
        if (context.getApplicationConfig().getDebug()) {
            log.debug("db {} insert sql -> {}", context.getBaseName(), s);
        }
        return jdbcTemplate.update(s);
    }

    /**
     * @param preSql
     * @param keyHolder
     * @return 返回自增主键的id
     */
    public Integer insert(AvalonPreparedStatement preSql, KeyHolder keyHolder) {
        if (context.getApplicationConfig().getDebug()) {
            log.debug("db {} insert sql -> {}", context.getBaseName(), preSql.toString());
        }
        jdbcTemplate.update(preSql, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public void insert(AvalonPreparedStatement preSql) {
        if (log.isDebugEnabled()) {
            log.debug("db {} insert sql -> {}", context.getBaseName(), preSql.toString());
        }
        jdbcTemplate.update(preSql);
    }

    public Integer update(AvalonPreparedStatement preSql) {
        if (context.getApplicationConfig().getDebug()) {
            log.debug("db {} update sql -> {}", context.getBaseName(), preSql.toString());
        }
        return jdbcTemplate.update(preSql);
    }

    /**
     * 更新记录
     *
     * @param sql
     * @return 返回受影响的行数
     */
    public Integer update(StringBuilder sql) {
        String s = sql.toString();
        if (context.getApplicationConfig().getDebug()) {
            log.debug("db {} update sql -> {}", context.getBaseName(), s);
        }
        return jdbcTemplate.update(s);
    }


    public void execute(StringBuilder sql) {
        String s = sql.toString();
        if (context.getApplicationConfig().getDebug()) {
            log.debug("db {} execute sql -> {}", context.getBaseName(), s);
        }
        jdbcTemplate.execute(s);
    }

    public void execute(String sql) {
        execute(new StringBuilder(sql));
    }

    /**
     * 获取第一行第一列的值
     *
     * @param sql
     * @param t
     * @param <T>
     * @return
     */
    public <T> T executeScalar(StringBuilder sql, Class<T> t) {
        return executeScalar(sql.toString(), t);
    }

    /**
     * 获取第一行第一列的值
     *
     * @param sql
     * @param t
     * @param <T>
     * @return
     */
    public <T> T executeScalar(String sql, Class<T> t) {
        if (context.getApplicationConfig().getDebug()) {
            log.debug("db {} execute scalar sql -> {}", context.getBaseName(), sql);
        }
        return jdbcTemplate.queryForObject(sql, t);
    }
}
