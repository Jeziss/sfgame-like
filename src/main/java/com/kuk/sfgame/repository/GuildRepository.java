package com.kuk.sfgame.repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.kuk.sfgame.model.Guild;
import com.kuk.sfgame.mapper.GuildMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GuildRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Properties sqlQueries = new Properties();

    public void setSQLQueriesFileName(String path) throws IOException {
        try (FileInputStream fis = new FileInputStream(path)) {
            sqlQueries.load(fis);
        }
    }

    public Guild findGuildById(int guildId) {
        String sql = sqlQueries.getProperty("findGuildById");
        List<Guild> guilds = jdbcTemplate.query(sql, new GuildMapper(), guildId);
        return guilds.isEmpty() ? null : guilds.get(0);
    }

    public Guild findGuildByPlayerId(int playerId) {
        String sql = sqlQueries.getProperty("findGuildByPlayerId");
        List<Guild> guilds = jdbcTemplate.query(sql, new GuildMapper(), playerId);
        return guilds.isEmpty() ? null : guilds.get(0);
    }
}
