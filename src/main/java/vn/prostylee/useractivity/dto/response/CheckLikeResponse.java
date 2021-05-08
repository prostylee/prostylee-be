package vn.prostylee.useractivity.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CheckLikeResponse implements Serializable {

    private List<Long> targetIds;

}
