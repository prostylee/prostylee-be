package vn.prostylee.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.prostylee.shipping.dto.request.ShippingAddressRequest;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderAtStoreRequest {

    @NotNull
    private Long productId;

    @NotNull
    private Long storeId;

    @NotNull
    private Long branchId;

    @NotNull
    private Long buyerId;

}
