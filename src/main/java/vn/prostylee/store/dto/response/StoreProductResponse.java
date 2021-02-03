package vn.prostylee.store.dto.response;

import lombok.Data;
import vn.prostylee.product.dto.response.ProductResponse;

import java.util.List;

@Data
public class StoreProductResponse extends StoreResponse {

    private List<ProductResponse> products;
}
