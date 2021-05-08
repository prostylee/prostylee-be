package vn.prostylee.location.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LocationResponse implements Comparable<LocationResponse>, Serializable {

    private Long id;

    private String address;

    private Double latitude;

    private Double longitude;

    private String state;

    private String city;

    private String country;

    private String zipcode;

    private Double distance;

    @Override
    public int compareTo(LocationResponse o) {
        if (o == null) {
            return 0;
        }
        if (this.distance == null && o.getDistance() == null) {
            return 0;
        }
        if (this.distance == null) {
            return 1;
        }
        if (o.getDistance() == null) {
            return -1;
        }
        return (this.distance - o.getDistance()) > 0 ? 1 : -1;
    }
}
