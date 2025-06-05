package com.demo.dao;

import com.demo.dao.MockDatabaseDao;
import com.demo.model.CacheEntity;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
/** Unit tests for the MockDatabaseDao class.
 * This class tests the basic CRUD operations of the MockDatabaseDao.
 */
public class MockDatabaseDaoTest {
    private MockDatabaseDao dao;
    private CacheEntity entity;
    /**
     * Sets up the test environment by initializing the MockDatabaseDao and a sample CacheEntity.
     */
    @Before
    public void setUp() {
        dao = new MockDatabaseDao();
        entity = new CacheEntity("1", "TestData");
    }
    /**
     * Tests the save and get operations of the MockDatabaseDao.
     * It verifies that an entity can be saved and then retrieved correctly.
     */
    @Test
    public void testSaveAndGet() {
        dao.save(entity);
        CacheEntity fetched = dao.get("1");
        assertNotNull(fetched);
        assertEquals("TestData", fetched.getData());
    }
    /**
     * Tests the get operation for a non-existent entity.
     * It verifies that attempting to retrieve an entity that does not exist returns null.
     */
    @Test
    public void testRemove() {
        dao.save(entity);
        dao.remove("1");
        assertNull(dao.get("1"));
    }
    /**
     * Tests the removeAll operation of the MockDatabaseDao.
     * It verifies that all entities can be removed from the in-memory store.
     */
    @Test
    public void testRemoveAll() {
        dao.save(entity);
        dao.save(new CacheEntity("2", "Data2"));
        dao.removeAll();
        assertEquals(0, dao.size());
    }
    /**
     * Tests the size operation of the MockDatabaseDao.
     * It verifies that the size of the in-memory store is correctly reported.
     */
    @Test
    public void testSize() {
        assertEquals(0, dao.size());
        dao.save(entity);
        assertEquals(1, dao.size());
    }
}