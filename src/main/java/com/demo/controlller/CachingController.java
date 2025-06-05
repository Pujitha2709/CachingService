package com.demo.controlller;

import com.demo.model.CacheEntity;
import com.demo.service.CachingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cache")
public class CachingController {

    private final CachingService cachingService = new CachingService(100); // Example max size

    @PostMapping
    public String add(@RequestBody CacheEntity entity) {
        cachingService.add(entity);
        return "Entity added";
    }

    @GetMapping("/{id}")
    public CacheEntity get(@PathVariable String id, @RequestParam(defaultValue = "false") boolean loadFromDB) {
        CacheEntity entity = new CacheEntity(id, null);
        if (loadFromDB) {
            return cachingService.get(entity, true);
        } else {
            return cachingService.get(entity);
        }
    }

    @DeleteMapping("/{id}")
    public String remove(@PathVariable String id) {
        CacheEntity entity = new CacheEntity(id, null);
        cachingService.remove(entity);
        return "Entity removed";
    }

    @DeleteMapping("/all")
    public String removeAll() {
        cachingService.removeAll();
        return "All entities removed";
    }

    @GetMapping("/cacheSize")
    public int cacheSize() {
        return cachingService.cacheSize();
    }

    @GetMapping("/dbSize")
    public int dbSize() {
        return cachingService.dbSize();
    }
}