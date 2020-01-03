package com.simple.cache;

import com.simple.data.Cab;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LocalCache implements Cache {

    private static HashMap<String, List<Cab>> CACHE_MAP = new HashMap<>();

    public List<Cab> get(String key) {
        log.info("Checking key from CACHE : " + key);
        return CACHE_MAP.get(key);
    }

    public void save(List<Cab> results) {
        HashMap<String, List<Cab>> insert = results.stream().collect(Collectors.groupingBy(Cab::getMedallion, HashMap::new, Collectors.toList()));
        insert.forEach((k, v) -> {
            if (CACHE_MAP.get(k) != null) {
                List<Cab> cabs = CACHE_MAP.get(k);
                cabs.addAll(v);
                CACHE_MAP.put(k, cabs);
            } else {
                CACHE_MAP.put(k, v);
            }
        });
    }

    public boolean peek(String key) {
        return CACHE_MAP.containsKey(key);
    }

    public void remove(String key) {
        log.info("removing key from CACHE : " + key);
        CACHE_MAP.remove(key);
    }

    public void clearAll() {
        log.info("Clearing all CACHE!");
        CACHE_MAP.clear();
    }

}
