package vn.prostylee.useractivity.dto.response;

import java.io.Serializable;

public interface RatingResultCountResponse extends Serializable {

    Long getProductId();

    Double getCount();
}
