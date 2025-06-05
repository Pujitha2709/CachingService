package com.demo.controlller;

import com.demo.model.CacheEntity;
import com.demo.service.CachingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cache")
/**
 * CachingController provides endpoints to manage cache entities.
 * It allows adding, retrieving, removing entities, and checking cache and database sizes.
 */
public class CachingController {

    private final CachingService cachingService = new CachingService(2); // Example max size
    /**
     * Constructor for CachingController.
     * Initializes the caching service with a specified maximum size.
     */
    @PostMapping
    public String add(@RequestBody CacheEntity entity) {
        cachingService.add(entity);
        return "Entity added";
    }
    /**
     * Adds a CacheEntity to the cache and database.
     * If the entity is null or has a null ID, it logs a warning and does not add it.
     * The cache uses a write-through strategy, meaning it writes to the database immediately.
     *
     * @param entity the CacheEntity to add
     */
    @GetMapping("/{id}")
    public CacheEntity get(@PathVariable String id, @RequestParam(defaultValue = "false") boolean loadFromDB) {
        CacheEntity entity = new CacheEntity(id, null);
        if (loadFromDB) {
            return cachingService.get(entity, true);
        } else {
            return cachingService.get(entity,false);
        }
    }
    /**
     * Retrieves a CacheEntity by its ID.
     * If loadFromDB is true, it fetches the entity from the database if not found in cache.
     * Otherwise, it returns null if the entity is not in cache.
     *
     * @param id the identifier of the entity to retrieve
     * @param loadFromDB whether to load from database if not found in cache
     * @return the CacheEntity if found, otherwise null
     */
    @DeleteMapping("/{id}")
    public String remove(@PathVariable String id) {
        CacheEntity entity = new CacheEntity(id, null);
        cachingService.remove(entity);
        return "Entity removed";
    }
    /**
     * Deletes a CacheEntity by its ID.
     * It removes the entity from both the cache and the database.
     *
     * @param id the identifier of the entity to delete
     * @return a confirmation message
     */
    @DeleteMapping("/all")
    public String removeAll() {
        cachingService.removeAll();
        return "All entities removed";
    }
    /**
     * Deletes all CacheEntity objects from both the cache and the database.
     * It clears the in-memory store and the cache.
     *
     * @return a confirmation message
     */
    @GetMapping("/cacheSize")
    public int cacheSize() {
        return cachingService.cacheSize();
    }
    /**
     * Returns the current size of the cache.
     * It counts the number of entities currently stored in the cache.
     *
     * @return the size of the cache
     */
    @GetMapping("/dbSize")
    public int dbSize() {
        return cachingService.dbSize();
    }
}