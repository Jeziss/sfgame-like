package com.kuk.sfgame.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kuk.sfgame.model.Item;
import com.kuk.sfgame.repository.ItemRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public List<Item> getItemsForPlayer(int playerId) {
        return itemRepository.findByPlayerId(playerId);
    }
}
