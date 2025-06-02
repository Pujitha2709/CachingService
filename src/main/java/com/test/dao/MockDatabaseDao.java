package com.test.dao;

import com.test.model.CacheEntity;
import java.util.concurrent.ConcurrentHashMap;


public class MockDatabaseDao {
    private final ConcurrentHashMap<String, CacheEntity> inMemoryDataStore = new ConcurrentHashMap<>();

    public void save(CacheEntity entity) {
        inMemoryDataStore.put(entity.getId(), entity);
    }


    public CacheEntity get(String id) {
        return inMemoryDataStore.get(id);
    }


    public void remove(String id) {
        inMemoryDataStore.remove(id);
    }


    public void removeAll() {
        inMemoryDataStore.clear();
    }

    public int size() {
        return inMemoryDataStore.size();
    }
}
