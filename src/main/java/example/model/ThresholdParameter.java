package example.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ThresholdParameter {
    private String id;
    private Double thresholdValue;
    private String name;
}
