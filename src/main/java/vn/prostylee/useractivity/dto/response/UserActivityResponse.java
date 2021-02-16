package vn.prostylee.useractivity.dto.response;

import lombok.Data;
import vn.prostylee.location.dto.response.LocationResponse;

@Data
public class UserActivityResponse {

    private Long id;

    private String username;

    private String fullName;

    private String phoneNumber;

    private Character gender;

    private String email;

    private Boolean active;

    private String avatar;

    private LocationResponse locationResponse;
}
