package vn.prostylee.store.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class CompanyResponse implements Serializable {

    private Long id;

    private String name;

    private String description;

    private Long ownerId;

    private Boolean active;

}
