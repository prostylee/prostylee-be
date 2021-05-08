package vn.prostylee.payment.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class PaymentResponse implements Serializable {
    private Long id;
    private String description;
    private String name;
}
