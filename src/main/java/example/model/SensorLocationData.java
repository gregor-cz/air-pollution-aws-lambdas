package example.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SensorLocationData {

    private final String id;
    private final Location location;
}
