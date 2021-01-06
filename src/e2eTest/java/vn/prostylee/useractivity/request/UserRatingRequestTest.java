package vn.prostylee.useractivity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRatingRequestTest {
    private Long targetId;
    private String targetType;
    private Integer value;
}