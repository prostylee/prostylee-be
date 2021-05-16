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
public class BranchRequest {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Long storeId;

    private Long locationId;

    private Boolean active;

}
