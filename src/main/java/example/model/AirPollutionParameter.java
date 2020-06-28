package example.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AirPollutionParameter {
    private String name;
    private Double currentValue;
    private int percentChange;

    public AirPollutionParameter(String name, Double currentValue, Double thresholdsValue) {
        this.name = name;
        this.currentValue = currentValue;
        this.percentChange = (int)(currentValue/thresholdsValue*100);
    }
}
