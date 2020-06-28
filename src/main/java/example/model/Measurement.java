
package example.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode
@ToString
public class Measurement {
    private Boolean error;
    private Double pm1;
    private Double pm25;
    private Double pm10;
    private Double hcho;
    private Double no2;
    private Double o3;
    private Double so2;
    private Double co;
    private Double temperature;
    private Double humidity;
    private Double pressure;
    private Double windSpeed;
    private Double windBearing;
}
