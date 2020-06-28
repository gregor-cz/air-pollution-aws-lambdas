package example.service;

import example.model.AirPollutionNotification;
import example.model.AirPollutionParameter;
import example.model.AirPollutionThreshold;
import example.model.SensorData;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class AirPollutionService {

    private final AirPollutionThreshold airPollutionThreshold = new ThresholdsProvider().getThreshold();

    public Optional<AirPollutionNotification> process(SensorData data) {

        Set<AirPollutionParameter> parameters = new HashSet<>();

        String stationId = data.getId();
        Double pm10 = data.getMeasurement().getPm10();
        Double pm25 = data.getMeasurement().getPm25();

        if (pm10 > airPollutionThreshold.getPm10()) {
            parameters.add(new AirPollutionParameter("PM10", pm10, airPollutionThreshold.getPm10()));
        }

        if (pm25 > airPollutionThreshold.getPm25()) {
            parameters.add(new AirPollutionParameter("PM25", pm25, airPollutionThreshold.getPm25()));
        }

        return parameters.isEmpty() ? Optional.empty() : Optional.of(new AirPollutionNotification(stationId, parameters));
    }
}
