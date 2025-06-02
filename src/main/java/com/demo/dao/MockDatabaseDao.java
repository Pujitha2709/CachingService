package com.demo.dao;

import com.demo.model.CacheEntity;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class acts as a mock database, storing CacheEntity objects in memory.
 * It provides thread-safe methods for basic data operations.
 */
public class MockDatabaseDao {
    // Stores CacheEntity objects in a thread-safe map, using their ID as the key
    private final ConcurrentHashMap<String, CacheEntity> inMemoryDataStore = new ConcurrentHashMap<>();

    /**
     * Inserts or updates a CacheEntity in the in-memory store.
     * @param entity the CacheEntity to be added or updated
     */
    public void save(CacheEntity entity) {
        inMemoryDataStore.put(entity.getId(), entity);
    }

    /**
     * Fetches a CacheEntity by its unique identifier.
     * @param id the identifier of the entity to retrieve
     * @return the corresponding CacheEntity, or null if not found
     */
    public CacheEntity get(String id) {
        return inMemoryDataStore.get(id);
    }

    /**
     * Deletes a CacheEntity from the store using its ID.
     * @param id the identifier of the entity to delete
     */
    public void remove(String id) {
        inMemoryDataStore.remove(id);
    }

    /**
     * Clears all entries from the in-memory data store.
     */
    public void removeAll() {
        inMemoryDataStore.clear();
    }

    /**
     * Returns the current number of CacheEntity objects in the store.
     * @return the count of stored entities
     */
    public int size() {
        return inMemoryDataStore.size();
    }
}