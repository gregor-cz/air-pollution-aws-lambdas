package example.service;

import example.model.AirQualityNotification;
import example.model.AirQualityData;
import example.model.SensorData;
import example.model.ThresholdParameter;

import java.util.*;
import java.util.stream.Collectors;

public class AirQualityService {

    private final ThresholdProvider thresholdProvider = new ThresholdProvider();

    private Map<String, Set<String>> activeParameters = new HashMap<>();

    public Optional<AirQualityNotification> process(SensorData data) {

        String stationId = data.getId();

        Set<AirQualityData> parameters = data.getMeasurementsMap().entrySet().stream()
                .filter(param -> parameterAboveThreshold(stationId, param))
                .map(this::toAirQualityData)
                .collect(Collectors.toSet());

        return parameters.isEmpty() ? Optional.empty() : Optional.of(new AirQualityNotification(stationId, parameters));
    }

    private AirQualityData toAirQualityData(Map.Entry<String, Double> parameterEntry) {
        ThresholdParameter threshold = thresholdProvider.getThreshold(parameterEntry.getKey());
        return new AirQualityData(threshold.getName(), parameterEntry.getValue(), threshold.getThresholdValue());
    }

    private boolean parameterAboveThreshold(String stationId, Map.Entry<String, Double> parameterEntry) {
        ThresholdParameter threshold = thresholdProvider.getThreshold(parameterEntry.getKey());

        boolean isActive = checkIfParamAlreadyActive(stationId, parameterEntry.getKey());
        boolean isValueExceeded =  parameterEntry.getValue() > threshold.getThresholdValue();
        if (isValueExceeded) {
            setParamAsActive(stationId, parameterEntry.getKey());
        } else {
            setParamAsNotActive(stationId, parameterEntry.getKey());
        }
        return isValueExceeded && !isActive;
    }

    private boolean checkIfParamAlreadyActive(String stationId, String parameterName){
        return activeParameters.compute(stationId,
                (key, value) -> Objects.isNull(value) ? new HashSet<>() : value).contains(parameterName);
    }

    private void setParamAsActive(String stationId, String parameterName){
        activeParameters.compute(stationId, (key, value) -> Objects.isNull(value) ? new HashSet<>() : value).add(parameterName);
    }

    private void setParamAsNotActive(String stationId, String parameterName){
        activeParameters.compute(stationId, (key, value) -> Objects.isNull(value) ? new HashSet<>() : value).remove(parameterName);
    }
}
