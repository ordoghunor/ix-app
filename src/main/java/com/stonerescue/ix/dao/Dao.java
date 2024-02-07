package com.stonerescue.ix.dao;

import com.stonerescue.ix.model.BaseEntity;

import java.util.Collection;

public interface Dao<T extends BaseEntity> {
    T saveAndFlush(T entity);
    Collection<T> findAll();
    T getById(Long id);
    boolean existsById(Long id);
    void delete(T entity);
}
