package vn.prostylee.store.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyRequest {

    @NotBlank
    private String name;

    private String description;

    private Boolean active;

}
