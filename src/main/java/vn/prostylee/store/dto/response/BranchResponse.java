package vn.prostylee.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.prostylee.location.dto.response.LocationResponse;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BranchResponse implements Serializable {

    private Long id;

    private String name;

    private String description;

    private Long storeId;

    private Long locationId;

    private LocationResponse location;

    private Boolean active;

    private Date updatedAt;

    private Long createdBy;

    private String fullAddress;

}
