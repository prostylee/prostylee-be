package vn.prostylee.store.dto.response;

import lombok.Data;
import vn.prostylee.location.dto.response.LocationResponseLite;

@Data
public class StoreMiniResponse {

    private Long id;

    private String name;

    private String logoUrl;

    private LocationResponseLite locationLite;
}
