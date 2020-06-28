package example.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class SensorData extends Marker {
    private String timestamp;

    public SensorData(String timestamp, Marker marker) {
        this.timestamp = timestamp;
        this.id = marker.getId();
        this.name = marker.getName();
        this.level = marker.getLevel();
        this.hex = marker.getHex();
        this.comment = marker.getComment();
        this.location = marker.getLocation();
        this.measurement = marker.getMeasurement();
    }
}
