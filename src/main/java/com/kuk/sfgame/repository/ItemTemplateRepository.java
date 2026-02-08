package com.kuk.sfgame.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.kuk.sfgame.model.ItemTemplate;
import com.kuk.sfgame.mapper.ItemTemplateMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;


@Repository
public interface ItemTemplateRepository extends JpaRepository<ItemTemplate, Integer> {
    // Spring automaticky implementuje findAll()
}

/*
@Repository
public class ItemTemplateRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Properties sqlQueries = new Properties();

    public void setSQLQueriesFileName(String path) throws IOException {
        try (FileInputStream fis = new FileInputStream(path)) {
            sqlQueries.load(fis);
        }
    }

    public List<ItemTemplate> findAll() {
        String sql = sqlQueries.getProperty("findAllItemTemplates");
        List<ItemTemplate> templates = jdbcTemplate.query(sql, new ItemTemplateMapper());
        return templates;
    }
}
*/