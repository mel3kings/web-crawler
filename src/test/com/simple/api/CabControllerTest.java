package com.simple.api;

import com.simple.cache.Cache;
import com.simple.controller.CabController;
import com.simple.dao.Dao;
import com.simple.data.Cab;
import com.simple.data.CabTripsRequest;
import com.simple.data.CabTripsResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CabControllerTest {

    @InjectMocks
    private CabController cb;

    @Mock
    private Dao storage;

    @Mock
    private Cache cache;

    private List<Cab> dummy;
    private ArrayList<String> cabs;
    private LocalDate date = LocalDate.of(2001, 01, 01);


    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        dummy = Arrays.asList(Cab.builder().medallion("dummy").pickupDateTime(date).build());
        cabs = new ArrayList<>(Arrays.asList("1234"));
    }


    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCalls() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        CabTripsRequest request = CabTripsRequest.builder().medallions(null).date(null).build();
        CabController c = new CabController();
        c.fetch(request, result);
    }

    @Test
    public void testFetchFromCache() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        when(cache.get(any())).thenReturn(dummy);
        when(cache.peek(any())).thenReturn(true);


        CabTripsRequest request = CabTripsRequest.builder().medallions(cabs).date(date).build();
        CabTripsResponse response = cb.fetch(request, result);
        assertEquals(response.getCabTrips().size(), dummy.size());
    }

    @Test
    public void testFetchFromDatabase() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        when(cache.get(any())).thenReturn(null);
        when(storage.fetch(any(List.class), any())).thenReturn(dummy);

        CabTripsRequest request = CabTripsRequest.builder().medallions(cabs).date(LocalDate.now()).build();
        CabTripsResponse response = cb.fetch(request, result);
        assertEquals(response.getCabTrips().size(), dummy.size());
    }

    @Test
    public void testNotFoundAtAll() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        when(cache.get(any())).thenReturn(null);
        when(cache.peek(any())).thenReturn(false);
        when(storage.fetch(any(List.class), any())).thenReturn(Collections.emptyList());

        CabTripsRequest request = CabTripsRequest.builder().medallions(cabs).date(date).build();
        CabTripsResponse response = cb.fetch(request, result);
        assertEquals(response.getCabTrips().size(), 0);
    }
}
