/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.db;

import com.avalon.core.config.DataSourceConfig;
import com.avalon.core.context.SpringContextHolder;
import com.avalon.core.util.ObjectUtils;
import com.avalon.core.util.StringUtils;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;


public class DataSourceUtil {
    /**
     * 创建新的数据源，注意：此处只针对 MySQL 数据库
     */
    // jdbc:mysql://rm-bp12orvj2495xci82yo.mysql.rds.aliyuncs.com:3306/fastdb?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=CTT
    public static DataSource makeNewDataSource(DataSourceConfig dataSourceConfig) {
        String url = "jdbc:mysql://" + dataSourceConfig.getHost() + ":" +
                dataSourceConfig.getPort();
        if (ObjectUtils.isNotEmpty(dataSourceConfig.getDatabase())) {
            url += "/" + dataSourceConfig.getDatabase();
        }
        url += "?useSSL=false&characterEncoding=UTF-8&allowPublicKeyRetrieval=true";
        if (ObjectUtils.isNotEmpty(dataSourceConfig.getTimezone())) {
            url += "&serverTimezone=" + dataSourceConfig.getTimezone();
        }

        String driveClassName = StringUtils.isEmpty(dataSourceConfig.getClassType()) ?
                "com.mysql.cj.jdbc.Driver"
                : dataSourceConfig.getClassType();
        DataSource dataSource = DataSourceBuilder.create().url(url)
                .driverClassName(driveClassName)
                .username(dataSourceConfig.getUsername())
                .password(dataSourceConfig.getPassword())
                .build();

        if (dataSource instanceof HikariDataSource) {
            HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
            hikariDataSource.setMaximumPoolSize(dataSourceConfig.getMaxPoolSize());
            hikariDataSource.setMinimumIdle(dataSourceConfig.getMinIdle());
            hikariDataSource.setConnectionTimeout(dataSourceConfig.getConnectionTimeout());
            hikariDataSource.setMaxLifetime(dataSourceConfig.getMaxLifetime());
            hikariDataSource.setIdleTimeout(dataSourceConfig.getIdleTimeout());
        }

        return dataSource;
    }

    /**
     * 创建新的数据源，注意：此处只针对 Postgres 数据库
     *
     * @param dataSourceConfig
     * @return
     */
    public static DataSource makePostgresDataSource(DataSourceConfig dataSourceConfig) {
        String url = "jdbc:postgresql://" + dataSourceConfig.getHost() + ":" +
                dataSourceConfig.getPort() + "/";
        if (ObjectUtils.isNotEmpty(dataSourceConfig.getDatabase())) {
            url += dataSourceConfig.getDatabase();
        }
        String driveClassName = StringUtils.isEmpty(dataSourceConfig.getClassType()) ?
                "org.postgresql.Driver"
                : dataSourceConfig.getClassType();
        DataSource dataSource = DataSourceBuilder.create().url(url)
                .driverClassName(driveClassName)
                .username(dataSourceConfig.getUsername())
                .password(dataSourceConfig.getPassword())
                .build();

        if (dataSource instanceof HikariDataSource) {
            HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
            hikariDataSource.setMaximumPoolSize(dataSourceConfig.getMaxPoolSize());
            hikariDataSource.setMinimumIdle(dataSourceConfig.getMinIdle());
            hikariDataSource.setConnectionTimeout(dataSourceConfig.getConnectionTimeout());
            hikariDataSource.setMaxLifetime(dataSourceConfig.getMaxLifetime());
            hikariDataSource.setIdleTimeout(dataSourceConfig.getIdleTimeout());
        }

        return dataSource;
    }

    /**
     * 添加数据源到动态源中
     */
    public static void addDataSourceToDynamic(String key, DataSource dataSource) {
        DynamicDataSource dynamicDataSource = SpringContextHolder.getContext().getBean(DynamicDataSource.class);
        dynamicDataSource.addDataSource(key, dataSource);
    }

    /**
     * 根据数据库连接信息添加数据源到动态源中
     *
     * @param key
     * @param dataSourceConfig
     */
    public static void addDataSourceToDynamic(String key, DataSourceConfig dataSourceConfig) {
        DataSource dataSource;
        if (ObjectUtils.isNotEmpty(dataSourceConfig.getClassType())
                && dataSourceConfig.getClassType().contains("mysql")) {
            dataSource = makeNewDataSource(dataSourceConfig);
        } else {
            dataSource = makePostgresDataSource(dataSourceConfig);
        }

        addDataSourceToDynamic(key, dataSource);
    }
}