package vn.prostylee.order.dto.response;

import lombok.Data;
import vn.prostylee.store.dto.response.StoreResponseLite;

@Data
public class OrderDetailResponse {

    private Long id;

    private StoreResponseLite store;

    private Double productPrice;

    private Integer amount;

    private String productName;

    private String productImage;

    private String productColor;

    private String productSize;

    private String productData;

}
