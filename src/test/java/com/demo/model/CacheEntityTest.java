package com.demo.model;

import com.demo.model.CacheEntity;
import org.junit.Test;
import static org.junit.Assert.*;

public class CacheEntityTest {

    @Test
    public void testConstructorAndGetters() {
        CacheEntity entity = new CacheEntity("1", "TestData");
        assertEquals("1", entity.getId());
        assertEquals("TestData", entity.getData());
    }

}