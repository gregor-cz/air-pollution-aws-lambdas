package example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
@Getter
public class AirQualityNotification {
    private String stationId;
    private Set<AirQualityData> parameters;
}
