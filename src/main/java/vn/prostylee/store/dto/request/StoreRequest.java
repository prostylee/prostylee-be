package vn.prostylee.store.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreRequest {

    @NotBlank
    private String name;

    private String description;

    private String address;

    private String website;

    private String phone;

    @NotNull
    private Long companyId;

    private Long ownerId;

    private Long locationId;

    private Integer status;

}
