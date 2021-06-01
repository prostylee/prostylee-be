package vn.prostylee.store.dto.response;

import lombok.Data;
import vn.prostylee.location.dto.response.LocationResponse;
import vn.prostylee.product.dto.response.ProductResponse;
import vn.prostylee.product.dto.response.ProductResponseLite;

import java.io.Serializable;
import java.util.List;

@Data
public class StoreResponse implements Serializable {

    private Long id;

    private String name;

    private String description;

    private String address;

    private String website;

    private String phone;

    private Long companyId;

    private CompanyResponse company;

    private Long ownerId;

    private Long locationId;

    private LocationResponse location;

    private Integer status;

    private List<ProductResponseLite> products;

    private Long logo;

    private String logoUrl;

    private Boolean isAdvertising;

    private List<StoreBannerResponse> storeBannerResponses;

}
