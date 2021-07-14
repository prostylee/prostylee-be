package vn.prostylee.useractivity.dto.response;

import lombok.Data;
import vn.prostylee.auth.dto.response.UserAddressResponse;
import vn.prostylee.location.dto.response.LocationResponse;

import java.io.Serializable;

@Data
public class UserActivityResponse implements Serializable {

    private Long id;

    private String username;

    private String fullName;

    private String phoneNumber;

    private Character gender;

    private String email;

    private Boolean active;

    private String avatar;

    private UserAddressResponse userAddressResponse;

    private Boolean followStatusOfUserLogin;

    private LocationResponse locationResponse;
}
