package com.simple.data;


import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Optional;

@Data
@Builder
public class Cab implements Serializable {
    private String medallion;
    private String hackLicense;
    private String vendorId;
    private int rateCode;
    private String storeAndForwardFlag;
    private LocalDate pickupDateTime;
    private LocalDate dropoffDateTime;
    private int passengerCount;
    private double tripTimeSeconds;
    private double tripDistance;

    @Override
    public int hashCode() {
        return this.getMedallion().hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Cab)) {
            return false;
        }
        Cab otherData = (Cab) other;
        return Optional.ofNullable(otherData.getMedallion())
                .filter(medallion -> medallion.equals(this.getMedallion())).isPresent();
    }
}

