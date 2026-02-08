package com.kuk.sfgame.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.kuk.sfgame.dto.ShopItemDto;
import com.kuk.sfgame.model.Player;
import com.kuk.sfgame.service.impl.ItemGenerationService;
import com.kuk.sfgame.service.impl.PlayerService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class ShopController {
    
    @Autowired
    private ItemGenerationService itemGenerationService;

    @Autowired
    private PlayerService playerService;

    @GetMapping("/weapon-shop")
    public String getWeaponShop(@RequestParam(required = false) Integer playerId, Model model, RedirectAttributes redirectAttributes ) {
        if (playerId == null) {
            redirectAttributes.addFlashAttribute(
                "errorMessage", "Select a player first"
            );
            return "redirect:/player?target=SHOP";
        }

        Player player = playerService.getPlayerById(playerId);
        model.addAttribute("player", player);
        
        List<ShopItemDto> items = itemGenerationService.generateItems(player.getLevel(), 10); // Generate 10 items for the shop
        model.addAttribute("items", items);

        return "shops/weapon-shop";
    }


    
}
