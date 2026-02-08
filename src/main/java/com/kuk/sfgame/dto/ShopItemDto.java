package com.kuk.sfgame.dto;

import com.kuk.sfgame.model.ItemSlot;
import com.kuk.sfgame.model.ItemTemplate;

public record ShopItemDto(
    ItemTemplate template,
    int price,
    ItemSlot slot,
    int strength,
    int constitution,
    int luck,
    Integer minDamage, // null if not weapon
    Integer maxDamage // null if not weapon
) {} 
