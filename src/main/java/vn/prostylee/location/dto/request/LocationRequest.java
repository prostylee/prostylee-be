package vn.prostylee.location.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;
import vn.prostylee.location.dto.LatLngDto;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper=true)
public class LocationRequest extends LatLngDto {

    private String address;

    private String state;

    private String city;

    private String country;

    private String zipcode;

    private String targetType;
}
