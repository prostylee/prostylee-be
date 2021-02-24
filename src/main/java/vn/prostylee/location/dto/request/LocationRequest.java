package vn.prostylee.location.dto.request;

import lombok.Data;
import vn.prostylee.location.dto.LatLngDto;

@Data
public class LocationRequest extends LatLngDto {
    private String address;
}
