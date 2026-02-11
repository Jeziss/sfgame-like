package com.kuk.sfgame.repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EquipmentRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Properties sqlQueries = new Properties();

    public void setSQLQueriesFileName(String path) throws IOException {
        try (FileInputStream fis = new FileInputStream(path)) {
            sqlQueries.load(fis);
        }
    }
}
