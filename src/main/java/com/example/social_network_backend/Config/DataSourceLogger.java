package com.example.social_network_backend.Config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;

@Component
public class DataSourceLogger {
    @Autowired
    private DataSource dataSource;

    @Autowired
    Environment environment;

    @PostConstruct
    public void logDataSource() throws SQLException {

        System.out.println("Active profiles: " + Arrays.toString(environment.getActiveProfiles()));
        System.out.println("DataSource class: " + dataSource.getClass().getName());
        System.out.println("URL: " + dataSource.getConnection().getMetaData().getURL());
    }
}

