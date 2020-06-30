package org.polsl.co.model;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class StationFilterPolicy {
    private Set<String> station_id = new HashSet<>();

    public void add(String stationId) {
        station_id.add(stationId);
    }
}
