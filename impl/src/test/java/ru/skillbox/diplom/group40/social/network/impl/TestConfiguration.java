package ru.skillbox.diplom.group40.social.network.impl;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;


//@Configuration
public class TestConfiguration {
    @Bean(initMethod = "start", destroyMethod = "stop")
    public PostgreSQLContainer<?> postgreSQLContainer(){
        return  new PostgreSQLContainer<>("postgres:15");
    }
    @Bean
    public DataSource dataSource(){
        var hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(postgreSQLContainer().getJdbcUrl());
        hikariDataSource.setUsername(postgreSQLContainer().getUsername());
        hikariDataSource.setPassword(postgreSQLContainer().getPassword());
        return hikariDataSource;
    }
}
