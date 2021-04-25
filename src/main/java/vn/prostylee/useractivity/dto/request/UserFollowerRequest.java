package vn.prostylee.useractivity.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserFollowerRequest {

    @NotNull
    private Long targetId;

    @NotBlank
    private String targetType;

    @Schema(description = "If the targetType is PRODUCT, this field is a categoryId value")
    private Long customFieldId1;

    @Schema(description = "If the targetType is PRODUCT, this field is a storeId value")
    private Long customFieldId2;

    @Schema(description = "Reserved value for further using")
    private Long customFieldId3;
}
