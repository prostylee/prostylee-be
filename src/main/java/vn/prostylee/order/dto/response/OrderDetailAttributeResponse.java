package vn.prostylee.order.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderDetailAttributeResponse implements Serializable {

    private String key;

    private String label;

    private String value;

}
