/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.db;

import com.avalon.core.context.Context;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

public class DynamicDataSource extends AbstractRoutingDataSource {
    private Map<Object, Object> backupTargetDataSources;

    /**
     * 自定义构造函数
     */
    public DynamicDataSource(Map<Object, Object> targetDataSource) {
        backupTargetDataSources = targetDataSource;
        super.setTargetDataSources(backupTargetDataSources);
        super.afterPropertiesSet();
    }

    /**
     * 添加新数据源
     */
    public void addDataSource(String key, DataSource dataSource) {
        this.backupTargetDataSources.put(key, dataSource);
        super.setTargetDataSources(this.backupTargetDataSources);
        super.afterPropertiesSet();
    }

    public void removeDataSource(String db) {
        DataSource remove = (DataSource)backupTargetDataSources.remove(db);
        if (remove instanceof HikariDataSource) {
            ((HikariDataSource) remove).close();
        }
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return Context.getDatabaseName();
    }


    /**
     * 判断是否存在相同数据库
     * @param key
     * @return
     */
    public Boolean contains(String key) {
        return backupTargetDataSources.containsKey(key);
    }
}