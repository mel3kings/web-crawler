package com.simple.controller;

import com.simple.cache.Cache;
import com.simple.dao.Dao;
import com.simple.data.Cab;
import com.simple.data.CabTripsRequest;
import com.simple.data.CabTripsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;

import static java.util.stream.Collectors.*;

@RestController
@Slf4j
public class CabController {

    @Autowired
    private Dao storage;

    @Autowired
    private Cache cache;

    @RequestMapping(value = "/fetch", consumes = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.POST)
    public CabTripsResponse fetch(@Valid @RequestBody CabTripsRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid input params");
        }
        clearCacheIfNeeded(request);
        List<Cab> response = getFromCacheOrDb(request);
        HashMap<String, List<Cab>> groupResponseById = response.stream()
                .collect(groupingBy(Cab::getMedallion, HashMap::new, toList()));
        return CabTripsResponse.builder().cabTrips(groupResponseById).dateRequested(request.getDate().toString()).build();
    }

    @RequestMapping(value = "/clearcache", method = RequestMethod.GET)
    public String clearCache() {
        cache.clearAll();
        return "Cleared Cache!";
    }

    private void clearCacheIfNeeded(CabTripsRequest request) {
        Optional.of(request).filter(CabTripsRequest::isFetchNewData).map(CabTripsRequest::getMedallions).ifPresent(
                list -> list.stream().forEach(cache::remove)
        );
    }

    /**
     * We are using medallion as Id, and we are filtering whether the date is already existing in the cache.
     * An alternative would be implementing a compound key of id + date. (See readme.md)
     * or just by fetching all from db without date and just filter in cache by date.
     *
     * @param request
     * @return
     */
    private List<Cab> getFromCacheOrDb(CabTripsRequest request) {
        Map<Boolean, List<String>> cachedCabs = request.getMedallions().stream().collect(
                partitioningBy(key ->
                        cache.peek(key) &&
                                cache.get(key).stream().anyMatch(a -> a.getPickupDateTime().equals(request.getDate()))
                ));
        List<Cab> response = cachedCabs.get(true).stream().map(k -> cache.get(k)).flatMap(List::stream)
                .filter(cab -> cab.getPickupDateTime().equals(request.getDate())).collect(toList());
        List<Cab> database = storage.fetch(cachedCabs.get(false), request.getDate());
        response.addAll(database);
        cache.save(database);
        log.info("response from cache/db size:" + response.size());
        return response;
    }
}
