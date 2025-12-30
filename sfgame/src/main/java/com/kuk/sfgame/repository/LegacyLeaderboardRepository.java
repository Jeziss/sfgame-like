package com.kuk.sfgame.repository;

import org.springframework.stereotype.Repository;
import com.kuk.sfgame.model.Player;

import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import java.util.Properties;

import com.kuk.sfgame.mapper.PlayerRowMapper;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileInputStream;
import java.io.IOException;


@Repository
public class LegacyLeaderboardRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Properties sqlQueries = new Properties();

    public void setSQLQueriesFileName(String path) throws IOException {
        try (FileInputStream fis = new FileInputStream(path)) {
            sqlQueries.load(fis);
        }
    }

    public List<Player> findAllPlayers() {
        String sql = sqlQueries.getProperty("findAllPlayersSorted");
        return jdbcTemplate.query(sql, new PlayerRowMapper());
    }

}
