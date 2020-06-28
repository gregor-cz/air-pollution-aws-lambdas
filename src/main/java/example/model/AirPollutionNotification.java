package example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
@Getter
public class AirPollutionNotification {
    private String stationId;
    private Set<AirPollutionParameter> parameters;
}
