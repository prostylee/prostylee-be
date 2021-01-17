package vn.prostylee.store.dto.response;

import lombok.Data;

@Data
public class CompanyResponse {

    private Long id;

    private String name;

    private String description;

    private Long ownerId;

    private Boolean active;

}
