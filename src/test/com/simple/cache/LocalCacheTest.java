package com.simple.cache;

import com.simple.data.Cab;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class LocalCacheTest {

    private Cache cache;
    private List<Cab> result;

    @Before
    public void init() {
        cache = new LocalCache();
        LocalDate date = LocalDate.of(2001, 01, 01);
        LocalDate date2 = LocalDate.of(2001, 01, 02);
        Cab cab1 = Cab.builder().medallion("1234").pickupDateTime(date).build();
        Cab cab2 = Cab.builder().medallion("1234").pickupDateTime(date2).build();
        result = Arrays.asList(cab1, cab2);
    }

    @After
    public void clear() {
        cache.clearAll();
    }

    @Test
    public void testInsertAndPeek() {
        cache.save(result);
        assertTrue(cache.peek("1234"));
    }

    @Test
    public void testDelete() {
        cache.save(result);
        assertTrue(cache.peek("1234"));
        cache.remove("1234");
        assertFalse(cache.peek("1234"));
    }

    @Test
    public void testAllDatesInKey() {
        cache.save(result);
        List dummy = cache.get("1234");
        assertTrue(dummy.size() == 2);
    }

    @Test
    public void testAddCacheWithDifferentDate() {
        cache.save(result);
        LocalDate date3 = LocalDate.of(2001, 01, 01);
        Cab cab3 = Cab.builder().medallion("1234")
                .pickupDateTime(date3).build();
        cache.save(Arrays.asList(cab3));
        List<Cab> results = cache.get("1234");
        List<Cab> cabsWithDate3 = results.stream()
                .filter(cab -> cab.getPickupDateTime().equals(date3)).collect(Collectors.toList());
        assertEquals(results.size(), 3);
        assertEquals(cabsWithDate3.size(), 2);
    }

    @Test
    public void testRemoveAll() {
        cache.save(result);
        List dummy = cache.get("1234");
        assertTrue(dummy.size() == 2);
        cache.clearAll();
        assertFalse(cache.peek("1234"));
    }
}
