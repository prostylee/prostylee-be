package vn.prostylee.location.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddressDto {

    @JsonProperty("DonViHanhChinhVietNam")
    private AdministrativeDivision administrativeDivision;

}
