package vn.prostylee.auth.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import vn.prostylee.auth.converter.ZaloConverter;

@Data
public class ZaloResponse {

    private String id;

    private String birthday;

    private String gender;

    private String name;

    @JsonDeserialize(using = ZaloConverter.Deserialize.class)
    private String picture;
}
