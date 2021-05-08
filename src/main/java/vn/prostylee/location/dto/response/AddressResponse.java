package vn.prostylee.location.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class AddressResponse implements Serializable {

    private String code;

    private String parentCode;

    private String name;

    private Long order;
}
