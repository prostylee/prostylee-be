package vn.prostylee.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressResponse {

    private Long id;

    private String cityCode;

    private String districtCode;

    private String wardCode;

    private String address;

    private String fullAddress;

    private Boolean priority;

    private String contactName;

    private String contactPhone;

}
