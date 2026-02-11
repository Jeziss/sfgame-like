package com.kuk.sfgame.model;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;


public class Equipment {
    private final Map<ItemSlot, Item> items;

    public Equipment() {
        items = new EnumMap<>(ItemSlot.class); 
        for (ItemSlot slot : ItemSlot.values()) {
            items.put(slot, null); 
        }
    }

    public void equip(Item item) {

        if (!items.containsKey(item.getEquippedSlot())) {
            throw new IllegalArgumentException("Nonexistent slot: " + item.getEquippedSlot());
        }

        items.put(item.getEquippedSlot(), item);
    }


    public void unequip(ItemSlot slot) {
        if (!items.containsKey(slot)) {
            throw new IllegalArgumentException("Nonexistent slot: " + slot);
        }
        items.put(slot, null);
    }


    public Item get(ItemSlot slot) {
        return items.get(slot);
    }

    public Item get(String slotName) {
        if (slotName == null || items == null) return null;
        try {
            // Converts the String "WEAPON" back to the Enum ItemSlot.WEAPON
            ItemSlot slot = ItemSlot.valueOf(slotName.toUpperCase());
            return items.get(slot);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Weapon getWeapon() {
        Item item = items.get(ItemSlot.WEAPON);
        return item != null ? item.getWeapon() : null;
    }

    public Map<ItemSlot, Item> getItems() {
        return Collections.unmodifiableMap(items); // read-only
    }

    public List<Item> getItemsList() {
        return items.values().stream().toList();
    }

    public boolean isEquipped(ItemSlot slot) {
        return items.get(slot) != null;
    }

    public StatsStruct getTotalStats() {
        StatsStruct itemsStats = new StatsStruct(0, 0, 0);

        for (Item item : items.values()) {
            if (item != null) {
                itemsStats = itemsStats.add(item);
            }
        }

        return itemsStats;
    }
}

