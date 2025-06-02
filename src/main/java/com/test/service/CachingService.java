package com.test.service;

import com.test.dao.MockDatabaseDao;
import com.test.model.CacheEntity;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CachingService {
    private static final Logger logger = LoggerFactory.getLogger(CachingService.class);

    private final Map<String, CacheEntity> cache; // Thread-safe map
    private final MockDatabaseDao database;


    public CachingService(int maxSize) {
        this.database = new MockDatabaseDao();

        // Access-order LinkedHashMap for LRU eviction policy, wrapped with Collections.synchronizedMap for thread safety
        this.cache = Collections.synchronizedMap(new LinkedHashMap<>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, CacheEntity> eldest) {
                boolean shouldEvict = size() > maxSize;
                if (shouldEvict) {
                    logger.info("Evicting entity with ID: {}", eldest.getKey());
                }
                return shouldEvict;
            }
        });
    }


    public void add(CacheEntity cacheEntity) {
        try {
            if (cacheEntity == null || cacheEntity.getId() == null) {
                logger.warn("Attempted to add a null entity or entity with null ID.");
                return;
            }
            cache.put(cacheEntity.getId(), cacheEntity);
            database.save(cacheEntity); // Always write-through
            logger.info("Added entity with ID: {}", cacheEntity.getId());
        } catch (Exception e) {
            logger.error("Failed to add entity with ID: {}", cacheEntity != null ? cacheEntity.getId() : "null", e);
        }
    }

    public CacheEntity get(CacheEntity cacheEntity) {
        try {
            if (cacheEntity == null || cacheEntity.getId() == null) {
                logger.warn("Attempted to get a null entity or entity with null ID.");
                return null;
            }
            // Check cache first
            CacheEntity result = cache.get(cacheEntity.getId());
            if (result == null) {
                // Do not reload evicted entities into the cache
                logger.info("Entity with ID: {} not found in cache.", cacheEntity.getId());
                result = database.get(cacheEntity.getId()); // Retrieve directly from the database
                if (result != null) {
                    logger.info("Entity with ID: {} found in database but not reloaded into cache.", cacheEntity.getId());
                }
            }
            return result;
        } catch (Exception e) {
            logger.error("Failed to retrieve entity with ID: {}", cacheEntity != null ? cacheEntity.getId() : "null", e);
            return null;
        }
    }


    public CacheEntity get(CacheEntity cacheEntity, boolean loadFromDB) {
        try {
            if (cacheEntity == null || cacheEntity.getId() == null) {
                logger.warn("Attempted to get a null entity or entity with null ID.");
                return null;
            }
            CacheEntity result = cache.get(cacheEntity.getId());
            if (result == null && loadFromDB) {
                result = database.get(cacheEntity.getId());
                if (result != null) {
                    cache.put(cacheEntity.getId(), result); // Load from DB into cache
                    logger.info("Loaded entity with ID: {} from database into cache.", cacheEntity.getId());
                }
            }
            return result;
        } catch (Exception e) {
            logger.error("Failed to retrieve entity with ID: {}", cacheEntity != null ? cacheEntity.getId() : "null", e);
            return null;
        }
    }


    public void remove(CacheEntity cacheEntity) {
        try {
            if (cacheEntity == null || cacheEntity.getId() == null) {
                logger.warn("Attempted to remove a null entity or entity with null ID.");
                return;
            }
            cache.remove(cacheEntity.getId());
            database.remove(cacheEntity.getId());
            logger.info("Removed entity with ID: {}", cacheEntity.getId());
        } catch (Exception e) {
            logger.error("Failed to remove entity with ID: {}", cacheEntity != null ? cacheEntity.getId() : "null", e);
        }
    }


    public void removeAll() {
        try {
            cache.clear();
            database.removeAll();
            logger.info("Removed all entities from cache and database.");
        } catch (Exception e) {
            logger.error("Failed to remove all entities.", e);
        }
    }


    public void clear() {
        try {
            cache.clear();
            logger.info("Cleared all entities from cache.");
        } catch (Exception e) {
            logger.error("Failed to clear cache.", e);
        }
    }


    public int cacheSize() {
        return cache.size();
    }


    public int dbSize() {
        return database.size();
    }
}
