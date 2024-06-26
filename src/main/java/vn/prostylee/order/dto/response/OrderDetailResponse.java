package vn.prostylee.order.dto.response;

import lombok.Data;
import vn.prostylee.product.dto.response.ProductResponseLite;
import vn.prostylee.store.dto.response.BranchResponse;
import vn.prostylee.store.dto.response.StoreResponseLite;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderDetailResponse implements Serializable {

    private Long id;

    private StoreResponseLite store;

    private BranchResponse branch;

    private Double productPrice;

    private Integer amount;

    private String productName;

    private String productImage;

    private ProductResponseLite productData;

    private List<OrderDetailAttributeResponse> orderDetailAttributes;

}
