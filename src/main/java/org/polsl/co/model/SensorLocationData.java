package org.polsl.co.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SensorLocationData {

    private String id;
    private Location location;
    
    public static SensorLocationData from(SensorData sensorData) {
        return new SensorLocationData(sensorData.getId(), sensorData.getLocation());
    }
}
