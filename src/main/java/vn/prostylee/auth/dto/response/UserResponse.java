package vn.prostylee.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long id;

    private String username;

    private String fullName;

    private String phoneNumber;

    private Character gender;

    private String email;

    private Long locationId;

    private Set<String> roles;

    private Boolean active;

    private Date createdAt;

    private Date lastModifiedAt;

    private Boolean allowNotification;

    private String avatar;

    private String bio;

    private Integer date;

    private Integer month;

    private Integer year;
}
