package vn.prostylee.location.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LocationResponse {

    private Long id;

    private String address;

    private Double latitude;

    private Double longitude;

    private String state;

    private String city;

    private String country;

    private String zipcode;

    private Double distance;

}
