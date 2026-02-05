package com.kuk.sfgame.mapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.kuk.sfgame.model.Item;

public class ItemMapper implements RowMapper<Item> {

    @Override
    public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
        Item item = new Item();
        item.setId(rs.getInt("id"));

        item.setConstitution(rs.getInt("constitution"));
        item.setStrength(rs.getInt("strength"));
        item.setLuck(rs.getInt("luck"));

        return item;
    }
    
}