package vn.prostylee.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileRequest {

    @NotBlank
    private String fullName;

    private String phoneNumber;

    private Character gender;

    @Email
    private String email;

    private Boolean allowNotification;

    private Boolean allowSaleNotification;

    private Boolean allowSocialNotification;

    private Boolean allowOrderStatusNotification;

    private Boolean allowStockNotification;

    private String bio;

    private Integer date;

    private Integer month;

    private Integer year;

    private String password;
}
