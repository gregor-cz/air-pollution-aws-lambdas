package org.polsl.co.model;

import lombok.Getter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@ToString
public class AirPollutionThresholds {

    private Set<ThresholdParameter> parameters = new HashSet<>();

    public void add(ThresholdParameter parameter) {
        parameters.add(parameter);
    }
}
