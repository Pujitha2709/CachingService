package com.demo.model;

import com.demo.model.CacheEntity;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Unit tests for the CacheEntity class.
 * This class tests the constructor and getter methods of CacheEntity.
 */
public class CacheEntityTest {
    /**
     * Tests the constructor and getter methods of CacheEntity.
     * It verifies that the ID and data are correctly set and retrieved.
     */
    @Test
    public void testConstructorAndGetters() {
        CacheEntity entity = new CacheEntity("1", "TestData");
        assertEquals("1", entity.getId());
        assertEquals("TestData", entity.getData());
    }

}