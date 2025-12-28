package com.kuk.sfgame.mapper;

import java.util.List;

/**
 * Base mapper interface for entity-DTO conversions
 */
public interface BaseMapper<Entity, Dto> {
    Dto toDto(Entity entity);
    Entity toEntity(Dto dto);
    List<Dto> toDtoList(List<Entity> entities);
    List<Entity> toEntityList(List<Dto> dtos);
}

