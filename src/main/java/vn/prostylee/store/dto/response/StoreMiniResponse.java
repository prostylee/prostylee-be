package vn.prostylee.store.dto.response;

import lombok.Data;
import vn.prostylee.location.dto.response.LocationResponseLite;

import java.io.Serializable;

@Data
public class StoreMiniResponse implements Serializable {

    private Long id;

    private String name;

    private String logoUrl;

    private LocationResponseLite locationLite;
}
