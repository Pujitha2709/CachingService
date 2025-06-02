package com.demo.model;

/**
 * Represents a basic data entity that has a unique ID and associated data.
 */
public class CacheEntity {
    private final String id;
    private final String data;

    /**
     * Constructs an CacheEntity object.
     * @param id unique identifier for the entity
     * @param data payload or value associated with the entity
     */
    public CacheEntity(String id, String data) {
        this.id = id;
        this.data = data;
    }

    /**
     * @return the ID of the entity
     */
    public String getId() {
        return id;
    }

    /**
     * @return the data of the entity
     */
    public String getData() {
        return data;
    }
}
