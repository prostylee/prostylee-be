package vn.prostylee.useractivity.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class CheckFollowResponse {

    private List<Long> targetIds;

}
