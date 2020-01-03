package com.simple.data;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
@Builder
public class CabTripsResponse {
    private String dateRequested;
    private HashMap<String, List<Cab>> cabTrips;
}
