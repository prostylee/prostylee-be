package vn.prostylee.location.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.location.dto.LatLngDto;

@Data
@EqualsAndHashCode(callSuper=true)
public class LocationRequest extends LatLngDto {
    private String address;
}
