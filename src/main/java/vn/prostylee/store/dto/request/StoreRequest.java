package vn.prostylee.store.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.prostylee.media.dto.request.MediaRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreRequest {

    @NotBlank
    @Size(max = 512)
    private String name;

    @Size(max = 4096)
    private String description;

    @Size(max = 512)
    private String address;

    @Size(max = 512)
    private String website;

    @Size(max = 32)
    private String phone;

    @NotNull
    private Long companyId;

    private Long ownerId;

    private Long locationId;

    private Integer status;

    private MediaRequest logoImage;

}
