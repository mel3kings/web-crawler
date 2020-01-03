package com.simple.cache;

import com.simple.data.Cab;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface Cache {
    List<Cab> get(String key);

    void save(List<Cab> result);

    boolean peek(String key);

    void remove(String key);

    void clearAll();
}
