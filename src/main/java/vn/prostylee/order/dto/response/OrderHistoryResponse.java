package vn.prostylee.order.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OrderHistoryResponse implements Serializable {

    private Long id;

    private Long orderId;

    private Long statusId;

    private String statusName;

    private int actCode;

    private Date createdAt;

    private Date updatedAt;

    private Integer step;
}
