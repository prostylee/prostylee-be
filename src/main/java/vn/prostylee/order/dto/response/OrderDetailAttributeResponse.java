package vn.prostylee.order.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderDetailAttributeResponse implements Serializable {

    private String attrKey;

    private String attrValue;

}
