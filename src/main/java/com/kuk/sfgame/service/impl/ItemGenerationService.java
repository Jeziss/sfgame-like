package com.kuk.sfgame.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.kuk.sfgame.dto.ShopItemDto;
import com.kuk.sfgame.model.ItemSlot;
import com.kuk.sfgame.model.ItemTemplate;
import com.kuk.sfgame.repository.ItemTemplateRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kuk.sfgame.util.Calculation;

import jakarta.annotation.PostConstruct;

@Service
public class ItemGenerationService {
    
    @Autowired
    private ItemTemplateRepository itemTemplateRepository;

    private List<ItemTemplate> ALL_ITEM_TEMPLATES;
    
    @PostConstruct
    public void init() {
        ALL_ITEM_TEMPLATES = itemTemplateRepository.findAll();
    }
    
    private int gaussianSoftRange(int min, int max) {
        return gaussianSoftRange(min, max, 12.0); // Default divisor for moderate spread
    }

    private int gaussianSoftRange(int min, int max, double stdDevDivisor) {
        double mean = (min + max) / 2.0;
        double stdDev = (max - min) / stdDevDivisor; 

        double value = ThreadLocalRandom.current().nextGaussian() * stdDev + mean;
        int result = (int) Math.round(value);

        return Math.max(min, Math.min(max, result));
    }


    public ShopItemDto generateItem(int playerLevel) {
        
        int strength = 0;
        int constitution = 0;
        int luck = 0;

        if (ThreadLocalRandom.current().nextBoolean()) { // Single stat item, 50% chance
            
            int statValue = Calculation.calculateItemStats(playerLevel);
            int statType = ThreadLocalRandom.current().nextInt(3); // 0 = strength, 1 = constitution, 2 = luck

            strength = (statType == 0) ? statValue : 0;
            constitution = (statType == 1) ? statValue : 0;
            luck = (statType == 2) ? statValue : 0;
        } else { // Double stat items, 50% chance
            
            int sumStats = Calculation.calculateItemStats(playerLevel);
            int minStatValue = Math.max(1, (int) Math.sqrt(sumStats));
            int maxStatValue = sumStats - minStatValue;
            int statValue1 = gaussianSoftRange(minStatValue, maxStatValue);     // First stat gets between 1 and sum-1
            int statValue2 = sumStats - statValue1;                             // Second stat gets the remainder

            int statType1 = ThreadLocalRandom.current().nextInt(3);// 0 = strength, 1 = constitution, 2 = luck

            int statType2 = ThreadLocalRandom.current().nextInt(3); // 0 = strength, 1 = constitution, 2 = luck
            while (statType2 == statType1) {
                statType2 = ThreadLocalRandom.current().nextInt(3); // Ensure different stats
            }

            strength =     (statType1 == 0 ? statValue1 : 0) + (statType2 == 0 ? statValue2 : 0);
            constitution = (statType1 == 1 ? statValue1 : 0) + (statType2 == 1 ? statValue2 : 0);
            luck =         (statType1 == 2 ? statValue1 : 0) + (statType2 == 2 ? statValue2 : 0);
        }

        // Strength, constitution and luck are set at this point.

        //TODO: solve price
        int price = Calculation.calculateItemPrice(playerLevel);

        ItemTemplate template = ALL_ITEM_TEMPLATES.get(ThreadLocalRandom.current().nextInt(ALL_ITEM_TEMPLATES.size()));
        
        Integer minDamage = null;
        Integer maxDamage = null;

        if (template.getSlot() == ItemSlot.WEAPON) {

            int rangeMedian = playerLevel * 2 + 2; // Base median damage on player level

            int minRange = rangeMedian / 2; // Minimum damage is half the median
            int maxRange = minRange * 3;    // Maximum damage is three times the minimum

            minDamage = gaussianSoftRange(minRange, maxRange, 4);
            maxDamage = rangeMedian + (rangeMedian - minDamage); // Ensure max is above median and min, median is between min and max
            
            // Final check to ensure minDamage is not greater than maxDamage
            if (minDamage > maxDamage) {
                int temp = minDamage;
                minDamage = maxDamage;
                maxDamage = temp;   
            }
        }
        
        return new ShopItemDto(template, price, template.getSlot(), strength, constitution, luck, minDamage, maxDamage);
    }

    public List<ShopItemDto> generateItems(int playerLevel, int count) {
        List<ShopItemDto> items = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            items.add(generateItem(playerLevel));
        }
        return items;
    }
}

