package vn.prostylee.order.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.auth.dto.response.UserResponse;
import vn.prostylee.core.dto.response.AuditResponse;
import vn.prostylee.product.dto.response.ProductResponse;
import vn.prostylee.product.dto.response.ProductResponseLite;
import vn.prostylee.shipping.dto.response.ShippingAddressResponse;
import vn.prostylee.shipping.dto.response.ShippingProviderResponse;
import vn.prostylee.store.dto.response.BranchResponse;
import vn.prostylee.store.dto.response.StoreResponse;
import vn.prostylee.store.dto.response.StoreResponseLite;
import vn.prostylee.story.dto.response.UserResponseLite;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class OrderAtStoreResponse {

    private ProductResponseLite productResponseLite;

    private StoreResponseLite storeResponseLite;

    private BranchResponse branchResponse;

    private UserResponseLite userResponseLite;

    private Boolean data;

}
