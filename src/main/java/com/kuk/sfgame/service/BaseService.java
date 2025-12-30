package com.kuk.sfgame.service;

import java.util.List;
import java.util.Optional;

/**
 * Base service interface for common service operations
 */
public interface BaseService<T, ID> {
    List<T> findAll();
    Optional<T> findById(ID id);
    T save(T entity);
    T update(ID id, T entity);
    void deleteById(ID id);
    boolean existsById(ID id);
}

