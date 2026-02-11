package com.kuk.sfgame.service.impl;


import java.util.Optional;

import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuk.sfgame.exception.NotEnoughGoldException;
import com.kuk.sfgame.model.Item;
import com.kuk.sfgame.model.ItemSlot;
import com.kuk.sfgame.model.ItemTemplate;
import com.kuk.sfgame.model.Player;
import com.kuk.sfgame.model.Weapon;
import com.kuk.sfgame.repository.ItemRepository;
import com.kuk.sfgame.repository.ItemTemplateRepository;

@Service
public class ShopService {

    @Autowired
    private PlayerService playerService;

    @Autowired 
    private ItemRepository itemRepository;

    @Autowired
    private ItemTemplateRepository itemTemplateRepository;


    public void buyItem(int playerId, int itemStrength, int itemConstitution, int itemLuck,
         int itemTemplateId, String itemSlot, Integer itemMaxDamage, Integer itemMinDamage, int itemPrice) {

        Player player = playerService.getPlayerWithGearById(playerId);
        
        if (player == null) {
            throw new IllegalArgumentException("Player not found with id: " + playerId);
        }

        if (player.getGold() < itemPrice) {
            throw new NotEnoughGoldException("Player does not have enough gold to buy this item.");
        }

        player.spendGold(itemPrice);
        playerService.save(player);


        Optional<ItemTemplate> itemTemplate = itemTemplateRepository.findById(itemTemplateId);
        if (!itemTemplate.isPresent()) {
            throw new IllegalArgumentException("Item template not found with id: " + itemTemplateId);
        }

        Item item = new Item(itemConstitution, itemStrength, itemLuck);

        item.setTemplate(itemTemplate.get());
        ItemSlot itemSlotEnum = ItemSlot.valueOf(itemSlot);
        item.setEquippedSlot(itemSlotEnum);

        item.setPlayer(player);

        // Remove currently equipped item from the database if one exists
        Item currentItem = player.getEquipment().get(itemSlotEnum);
        if (currentItem != null && currentItem.getId() != null) {
            if (currentItem.isWeapon()) {
                Weapon currentWeapon = currentItem.getWeapon();
                // Removing the referances to avoid Hibernate issues with cascading deletes
                currentWeapon.setPlayerItem(null);
                currentItem.setWeapon(null);      
            }

            itemRepository.deleteById(currentItem.getId());
        }

        if (itemSlotEnum == ItemSlot.WEAPON) {
            Weapon weapon = new Weapon();
            weapon.setMinDamage(itemMinDamage);
            weapon.setMaxDamage(itemMaxDamage);
            item.setWeapon(weapon);
            weapon.setPlayerItem(item);
        }

        player.getEquipment().equip(item);

        // Save the item to the database
        itemRepository.save(item);
    }
}
