package vn.prostylee.store.dto.response;

import lombok.Data;

@Data
public class StoreResponse {

    private Long id;

    private String name;

    private String description;

    private String address;

    private String website;

    private String phone;

    private CompanyResponse company;

    private Long ownerId;

    private Long locationId;

    private Integer status;

}
