package vn.prostylee.location.dto.response;

import lombok.Data;

@Data
public class AddressResponse {

    private String code;

    private String parentCode;

    private String name;

    private Long order;
}
