package vn.prostylee.order.dto.response;

import java.io.Serializable;

public interface ProductSoldCountResponse extends Serializable {

    Long getProductId();

    Long getCount();
}
