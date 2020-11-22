package vn.prostylee.auth.dto.response;

import lombok.Data;

@Data
public class FeatureResponse {

    private String name;

    private String apiPath;

    private String code;

    private String group;

    private Integer order;
}
