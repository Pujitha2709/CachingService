package com.demo.dao;

import com.demo.dao.MockDatabaseDao;
import com.demo.model.CacheEntity;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MockDatabaseDaoTest {
    private MockDatabaseDao dao;
    private CacheEntity entity;

    @Before
    public void setUp() {
        dao = new MockDatabaseDao();
        entity = new CacheEntity("1", "TestData");
    }

    @Test
    public void testSaveAndGet() {
        dao.save(entity);
        CacheEntity fetched = dao.get("1");
        assertNotNull(fetched);
        assertEquals("TestData", fetched.getData());
    }

    @Test
    public void testRemove() {
        dao.save(entity);
        dao.remove("1");
        assertNull(dao.get("1"));
    }

    @Test
    public void testRemoveAll() {
        dao.save(entity);
        dao.save(new CacheEntity("2", "Data2"));
        dao.removeAll();
        assertEquals(0, dao.size());
    }

    @Test
    public void testSize() {
        assertEquals(0, dao.size());
        dao.save(entity);
        assertEquals(1, dao.size());
    }
}