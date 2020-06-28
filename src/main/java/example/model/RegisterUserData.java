package example.model;

import lombok.Data;

@Data
public class RegisterUserData {
    private String userId;
    private String stationId;
    private String phoneNumber;
    private String email;
}
