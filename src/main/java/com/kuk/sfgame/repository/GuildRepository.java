package com.kuk.sfgame.repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
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

    public void loadSqlQueries(InputStream inputStream) throws IOException {
        sqlQueries.load(inputStream);
    }

    public Optional<Guild> findById(int guildId) {
        String sql = sqlQueries.getProperty("findGuildById");
        List<Guild> guilds = jdbcTemplate.query(sql, new GuildMapper(), guildId);
        return guilds.isEmpty() ? Optional.empty() : Optional.of(guilds.get(0));
    }

    public List<Guild> findAll() {
        String sql = "SELECT * FROM guilds ORDER BY id";
        return jdbcTemplate.query(sql, new GuildMapper());
    }

    public void save(Guild guild) {
        String sql = "UPDATE guilds SET name = ?, xp_bonus_percent = ?, gold_bonus_percent = ?, dmg_bonus_percent = ?, hp_bonus_percent = ?, quest_offer_number = ? WHERE id = ?";
        jdbcTemplate.update(sql, guild.getName(), guild.getXpBonusPercent(), guild.getGoldBonusPercent(), guild.getDmgBonusPercent(), guild.getHpBonusPercent(), guild.getQuestOfferNumber(), guild.getId());
    }

    public Guild findGuildById(int guildId) {
        return findById(guildId).orElse(null);
    }

    public Guild findGuildByPlayerId(int playerId) {
        String sql = sqlQueries.getProperty("findGuildByPlayerId");
        List<Guild> guilds = jdbcTemplate.query(sql, new GuildMapper(), playerId);
        return guilds.isEmpty() ? null : guilds.get(0);
    }
}
