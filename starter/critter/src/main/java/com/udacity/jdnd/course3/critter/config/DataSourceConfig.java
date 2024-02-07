package com.udacity.jdnd.course3.critter.config;

import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

public class DataSourceConfig {
    public DataSource getDatasource(){
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url("jdbc:mysql://localhost:3306/critter");
        return dataSourceBuilder.build();
    }
}
