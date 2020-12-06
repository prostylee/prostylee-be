package vn.prostylee.auth.dto.response;

import lombok.Data;

import java.util.Map;
import java.util.Optional;

@Data
public class ZaloResponse {

    private String id;

    private String birthDay;

    private String gender;

    private String name;

    private Map<String, Object> picture;

    private String pictureUrl;

    public String getPictureUrl() {
        return Optional.ofNullable(picture)
                .map(pic -> pic.get("data"))
                .map(data -> ((Map<String, Object>)data).get("url")).get().toString();
    }
}
