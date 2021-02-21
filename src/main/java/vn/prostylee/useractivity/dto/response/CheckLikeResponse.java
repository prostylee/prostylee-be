package vn.prostylee.useractivity.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class CheckLikeResponse {

    private List<Long> targetIds;

}
