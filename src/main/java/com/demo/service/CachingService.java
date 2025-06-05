package com.demo.service;

import com.demo.dao.MockDatabaseDao;
import com.demo.model.CacheEntity;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CachingService provides a thread-safe caching mechanism with LRU eviction policy.
 * It uses a write-through cache strategy, ensuring that all writes are immediately persisted to the database.
 * The cache is implemented using a synchronized LinkedHashMap to maintain order and thread safety.
 */
public class CachingService {
    private static final Logger logger = LoggerFactory.getLogger(CachingService.class);

    private final Map<String, CacheEntity> cache; // Thread-safe map
    private final MockDatabaseDao database;

    // Constructor initializes the cache with a specified maximum size
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

    /**
     * Adds a CacheEntity to the cache and database.
     * If the entity is null or has a null ID, it logs a warning and does not add it.
     * The cache uses a write-through strategy, meaning it writes to the database immediately.
     *
     * @param cacheEntity the CacheEntity to add
     */
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

    /**
     * Retrieves a CacheEntity from the cache or database based on the loadFromDB flag.
     * If the entity is not found in the cache and loadFromDB is true, it retrieves it from the database.
     * If the entity is evicted from the cache, it will be reloaded into the cache if found in the database.
     *
     * @param cacheEntity the CacheEntity to retrieve
     * @param loadFromDB  whether to load from the database if not found in cache
     * @return the retrieved CacheEntity, or null if not found
     */
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

    /**
     * Removes a CacheEntity from both the cache and the database.
     * If the entity is null or has a null ID, it logs a warning and does not attempt to remove it.
     *
     * @param cacheEntity the CacheEntity to remove
     */
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

    /**
     * Removes all CacheEntity objects from both the cache and the database.
     * This method clears the cache and removes all entries from the database.
     */
    public void removeAll() {
        try {
            cache.clear();
            database.removeAll();
            logger.info("Removed all entities from cache and database.");
        } catch (Exception e) {
            logger.error("Failed to remove all entities.", e);
        }
    }

    /**
     * Clears all entries from the cache.
     * This method does not affect the database; it only clears the in-memory cache.
     */
    public void clear() {
        try {
            cache.clear();
            logger.info("Cleared all entities from cache.");
        } catch (Exception e) {
            logger.error("Failed to clear cache.", e);
        }
    }

    /**
     * Returns the current size of the cache.
     * This method provides the number of entries currently stored in the cache.
     *
     * @return the size of the cache
     */
    public int cacheSize() {
        return cache.size();
    }

    /**
     * Returns the current size of the database.
     * This method provides the number of entries currently stored in the database.
     *
     * @return the size of the database
     */
    public int dbSize() {
        return database.size();
    }
}
