package example.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

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

    public Map<String, Double> getMeasurementsMap(){
        Map<String, Double> measurementsMap = new HashMap<>();
        measurementsMap.put("pm25", measurement.getPm25());
        measurementsMap.put("pm10", measurement.getPm10());
        return measurementsMap;
    }
}
