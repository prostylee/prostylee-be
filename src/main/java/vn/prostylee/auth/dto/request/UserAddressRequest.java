package vn.prostylee.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressRequest {

    @NotBlank
    private String cityCode;

    @NotBlank
    private String districtCode;

    @NotBlank
    private String wardCode;

    @NotBlank
    private String address;

    @NotNull
    private Boolean priority;

}
