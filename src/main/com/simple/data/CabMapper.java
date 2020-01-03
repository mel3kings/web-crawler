package com.simple.data;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CabMapper implements RowMapper<Cab> {

    @Override
    public Cab map(ResultSet rs, StatementContext ctx) throws SQLException {
        return Cab.builder().medallion(rs.getString("medallion"))
                .hackLicense(rs.getString("hack_license"))
                .vendorId(rs.getString("vendor_id"))
                .rateCode(rs.getInt("rate_code"))
                .storeAndForwardFlag(rs.getString("store_and_fwd_flag"))
                .pickupDateTime(rs.getDate("pickup_datetime").toLocalDate())
                .dropoffDateTime(rs.getDate("dropoff_datetime").toLocalDate())
                .passengerCount(rs.getInt("passenger_count"))
                .tripTimeSeconds(rs.getDouble("trip_distance")).build();
    }
}

