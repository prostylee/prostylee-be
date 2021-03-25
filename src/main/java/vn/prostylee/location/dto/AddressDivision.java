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
public class AddressDivision {

    @JsonProperty("MaDVHC")
    private String code;

    @JsonProperty("CapTren")
    private String parentCode;

    @JsonProperty("Cap")
    private String level;

    @JsonProperty("Ten")
    private String name;

    @JsonProperty("TenTA")
    private String englishName;
}
