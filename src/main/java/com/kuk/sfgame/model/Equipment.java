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
            throw new IllegalArgumentException("Neexistující slot: " + item.getEquippedSlot());
        }

        items.put(item.getEquippedSlot(), item);
    }


    public void unequip(ItemSlot slot) {
        if (!items.containsKey(slot)) {
            throw new IllegalArgumentException("Neexistující slot: " + slot);
        }
        items.put(slot, null);
    }


    public Item get(ItemSlot slot) {
        return items.get(slot);
    }

    public Map<ItemSlot, Item> getAllMap() {
        return Collections.unmodifiableMap(items); // read-only
    }

    public List<Item> getAllList() {
        return items.values().stream().toList();
    }

    public boolean isEquipped(ItemSlot slot) {
        return items.get(slot) != null;
    }

    public StatsStruct getTotalStats() {
        StatsStruct itemsStats = new StatsStruct(0, 0, 0, 0);

        for (Item item : items.values()) {
            if (item != null) {
                itemsStats = itemsStats.add(item);
            }
        }

        return itemsStats;
    }
}

