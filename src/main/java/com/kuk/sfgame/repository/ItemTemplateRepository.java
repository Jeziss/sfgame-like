package com.kuk.sfgame.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kuk.sfgame.model.ItemTemplate;



@Repository
public interface ItemTemplateRepository extends JpaRepository<ItemTemplate, Integer> {
    // Spring automaticky implementuje findAll()
}
