package com.test.service;

import com.test.model.CacheEntity;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class CacheServiceTest {
    private CachingService cache;

    @Before
    public void setup() {
        cache = new CachingService(2);
    }

    @Test
    public void testAddWritesToCacheAndDB() {
        CacheEntity e = new CacheEntity("1", "Alpha");
        cache.add(e);


        assertEquals("Alpha", cache.get(e).getData());

        assertEquals(1, cache.dbSize());
    }


    @Test
    public void testOverwriteEntity() {
        cache.add(new CacheEntity("1", "OldValue"));
        cache.add(new CacheEntity("1", "NewValue"));

        // The latest value should overwrite the old one
        assertEquals("NewValue", cache.get(new CacheEntity("1", "")).getData());
    }


    @Test
    public void testEvictionKeepsDataInDB() {
        cache.add(new CacheEntity("1", "A"));
        cache.add(new CacheEntity("2", "B"));
        cache.add(new CacheEntity("3", "C"));  // "1" should be evicted from cache

        assertNull(cache.get(new CacheEntity("1", ""),false));
        assertEquals("A", cache.get(new CacheEntity("1", "")).getData()); // Load from DB
    }

    @Test
    public void testRemoveFromCacheAndDB() {
        CacheEntity e = new CacheEntity("1", "Z");
        cache.add(e);
        cache.remove(e);

        assertNull("Entity should be removed from both cache and DB", cache.get(e));
    }


    @Test
    public void testClearOnlyAffectsCache() {
        cache.add(new CacheEntity("1", "Z"));
        cache.clear();

        assertEquals("Z", cache.get(new CacheEntity("1", "")).getData()); // Should load from DB
    }


    @Test
    public void testRemoveAllClearsAll() {
        cache.add(new CacheEntity("1", "X"));
        cache.add(new CacheEntity("2", "Y"));
        cache.removeAll();

        assertNull(cache.get(new CacheEntity("1", "")));
        assertEquals(0, cache.cacheSize());
        assertEquals(0, cache.dbSize());
    }


    @Test
    public void testRemoveNonExistingEntity() {
        cache.remove(new CacheEntity("999", "DoesNotExist"));
        assertEquals(0, cache.cacheSize());  // Nothing removed
    }


    @Test
    public void testMultipleEvictionsAndDBSync() {
        cache.add(new CacheEntity("1", "A"));
        cache.add(new CacheEntity("2", "B"));
        cache.add(new CacheEntity("3", "C")); // evict 1
        cache.add(new CacheEntity("4", "D")); // evict 2

        // Only last two in cache
        assertEquals(2, cache.cacheSize());

        // All four in DB
        assertEquals(4, cache.dbSize());
    }


    @Test
    public void testDuplicateAddsSameData() {
        CacheEntity e = new CacheEntity("1", "Data");
        cache.add(e);
        cache.add(e);

        // Assert that the data is correctly stored and no duplicates exist in the DB
        assertEquals("Data", cache.get(new CacheEntity("1", "")).getData());
        assertEquals("DB size should remain 1 after duplicate adds", 1, cache.dbSize());
    }


    @Test
    public void testGetNonExistentKey() {
        // Assert that a non-existent key returns null
        assertNull("Non-existent key should return null", cache.get(new CacheEntity("999", "")));
    }


    @Test
    public void testAddNullEntity() {
        cache.add(null);

        // Assert that cache and DB remain unaffected
        assertEquals("Cache size should remain 0 after adding null", 0, cache.cacheSize());
        assertEquals("DB size should remain 0 after adding null", 0, cache.dbSize());
    }


    @Test
    public void testRemoveNullEntity() {
        cache.remove(null);

        // Assert that cache and DB remain unaffected
        assertEquals("Cache size should remain 0 after removing null", 0, cache.cacheSize());
        assertEquals("DB size should remain 0 after removing null", 0, cache.dbSize());
    }


    @Test
    public void testGetNullEntity() {
        // Assert that getting a null entity returns null
        assertNull("Getting a null entity should return null", cache.get(null));
    }


    @Test
    public void testAddEntityWithNullID() {
        CacheEntity e = new CacheEntity(null, "Data");
        cache.add(e);

        assertEquals("Cache size should remain 0 after adding entity with null ID", 0, cache.cacheSize());
        assertEquals("DB size should remain 0 after adding entity with null ID", 0, cache.dbSize());
    }


    @Test
    public void testAddEntityWithNullData() {
        CacheEntity e = new CacheEntity("1", null);
        cache.add(e);

        assertEquals("Cache size should be 1 after adding entity with null data", 1, cache.cacheSize());
        assertEquals("DB size should be 1 after adding entity with null data", 1, cache.dbSize());
        assertNull("Data for the entity should be null", cache.get(new CacheEntity("1", "")).getData());
    }
}
