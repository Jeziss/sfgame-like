package com.kuk.sfgame.mapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.kuk.sfgame.model.ItemSlot;
import com.kuk.sfgame.model.ItemTemplate;

public class ItemTemplateMapper implements RowMapper<ItemTemplate> {

    @Override
    public ItemTemplate mapRow(ResultSet rs, int rowNum) throws SQLException {
        ItemTemplate template = new ItemTemplate();
        template.setId(rs.getInt("id"));

        template.setName(rs.getString("name"));
        template.setIcon(rs.getString("icon"));
        

        String slotStr = rs.getString("slot");
        template.setSlot(ItemSlot.valueOf(slotStr));

        return template;
    }
    
}