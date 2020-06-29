package example.service;

import example.model.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class AirPollutionService {

    private final Map<String, ThresholdParameter> airPollutionThresholds = new ThresholdsProvider().getThresholds();

    public Optional<AirPollutionNotification> process(SensorData data) {

        Set<AirPollutionParameter> parameters = new HashSet<>();

        String stationId = data.getId();

        data.getMeasurementsMap().entrySet().forEach(entry -> {

            ThresholdParameter thresholdParameter = airPollutionThresholds.get(entry.getKey());


            if (entry.getValue() > thresholdParameter.getThresholdValue()) {
                parameters.add(new AirPollutionParameter(thresholdParameter.getName(), entry.getValue(), thresholdParameter.getThresholdValue()));
            }


        });

        return parameters.isEmpty() ? Optional.empty() : Optional.of(new AirPollutionNotification(stationId, parameters));
    }
}
