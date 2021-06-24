package vn.prostylee.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusMstRequest {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    @Schema(allowableValues = {"CREATE_ORDER", "RECEIVE_ORDER", "GOOD_ISSUE", "DELIVERY", "CANCEL_ORDER", "COMPLETE"})
    private int actCode;

    private Integer step;

    private Boolean active;

    private String languageCode;

    private String group;

}
