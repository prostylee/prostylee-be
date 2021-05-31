package vn.prostylee.useractivity.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserWishListRequest {

    @NotNull
    private Long productId;
}
