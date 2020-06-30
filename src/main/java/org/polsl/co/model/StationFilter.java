package org.polsl.co.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class StationFilter {
    private Set<String> station_id = new HashSet<>();
}
