package vn.prostylee.location.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class LocationResponseLite implements Serializable {

    private String city;

    private String country;
}
