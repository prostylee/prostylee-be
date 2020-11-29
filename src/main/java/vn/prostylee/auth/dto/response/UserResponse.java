package vn.prostylee.auth.dto.response;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class UserResponse {

    private Long id;

    private String username;

    private String fullName;

    private String phoneNumber;

    private Character gender;

    private String email;

    private Set<String> roles;

    private Boolean active;

    private Date createdAt;

    private Date lastModifiedAt;

    private Boolean allowNotification;
}
