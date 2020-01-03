package com.simple.dao;

import com.simple.data.Cab;

import java.time.LocalDate;
import java.util.List;

public interface Dao {

    List<Cab> fetch(List<String> id, LocalDate date);
}
