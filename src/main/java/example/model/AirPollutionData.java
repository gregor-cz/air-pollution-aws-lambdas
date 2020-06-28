package example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AirPollutionData {
    @JsonProperty("_id")
    private String id;
    private List<Marker> markers;
}