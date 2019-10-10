package com.warn.chron.chronjobs.application.common;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;

import javax.sql.DataSource;

public @Data class DatabaseInformation {

    private String name;
    private String db;
    private String jdbcUrl;
    private String user;
    private String password;
    private Boolean cachePrepStmts;
    private Integer prepStmtCacheSize;
    private Integer prepStmtCacheSqlLimit;
    private Boolean useServerPrepStmts;
    private Integer minimumIdle;
    private Integer maximumPoolSize;

    public HikariConfig getHikariConfig() {

        HikariConfig config = new HikariConfig();
        config.setPoolName(getName());
        config.setJdbcUrl(getJdbcUrl());
        config.setUsername(getUser());
        config.setPassword(getPassword());
        config.setMinimumIdle(getMinimumIdle());
        config.setMaximumPoolSize(getMaximumPoolSize());
        config.addDataSourceProperty("cachePrepStmts", getCachePrepStmts());
        config.addDataSourceProperty("prepStmtCacheSize", getPrepStmtCacheSize());
        config.addDataSourceProperty("prepStmtCacheSqlLimit", getPrepStmtCacheSqlLimit());
        config.addDataSourceProperty("useServerPrepStmts", getUseServerPrepStmts());
        config.setAutoCommit(true);

        return config;

    }

    public DataSource getDataSource() {
        return new HikariDataSource(getHikariConfig());
    }
    
}
