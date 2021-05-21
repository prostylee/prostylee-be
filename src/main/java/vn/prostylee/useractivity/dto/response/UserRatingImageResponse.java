package vn.prostylee.useractivity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRatingImageResponse implements Serializable {

    private Long id;

    private String thumbnail;

    private String original;
}
