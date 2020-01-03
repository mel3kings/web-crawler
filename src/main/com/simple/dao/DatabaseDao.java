package com.simple.dao;

import com.simple.data.Cab;
import com.simple.data.CabMapper;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;


@Slf4j
@Repository
public class DatabaseDao implements Dao {

    @Value("${jdbc.url}")
    private String url;
    private Jdbi jdbi;

    @PostConstruct
    public void init() {
        jdbi = Jdbi.create(url);
    }

    public List<Cab> fetch(List<String> id, LocalDate date) {
        if (null == id || id.size() == 0) {
            return Collections.emptyList();
        }
        log.info("Fetching from DATABASE::" + id + date);
        return jdbi.withHandle(handle ->
                handle.createQuery("select * from cab_trip_data " +
                        "where medallion in (<medallions>) and DATE(pickup_datetime)= :pickup_datetime")
                        .bindList("medallions", id)
                        .bind("pickup_datetime", date.toString())
                        .map(new CabMapper())
                        .list()
        );
    }
}
