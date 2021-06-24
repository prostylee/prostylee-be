package vn.prostylee.order.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import vn.prostylee.product.dto.response.AttributeResponse;

import java.io.Serializable;
import java.util.Set;

@Data
public class OrderStatusMstResponse implements Serializable {
    private Long id;
    private String name;
    private String description;
    private int actCode;
    private Integer step;
    private Boolean active;
    private String languageCode;
    private String group;
}
