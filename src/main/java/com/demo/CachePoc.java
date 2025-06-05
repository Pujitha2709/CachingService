package com.demo;
import com.demo.model.CacheEntity;
import com.demo.service.CachingService;

/**
 * Main class to demonstrate the caching service functionality.
 * It initializes a cache, adds entities, and retrieves them.
 */
public class CachePoc {
    public static void main(String[] args) {

        CachingService cache = new CachingService(2);

        CacheEntity e1 = new CacheEntity("1", "Entity1");
        CacheEntity e2 = new CacheEntity("2", "Entity2");
        CacheEntity e3 = new CacheEntity("3", "Entity3");


        cache.add(e1);
        cache.add(e2);
        cache.add(e3);


        System.out.println("Fetched from DB: " + cache.get(e1).getData());
    }
}
