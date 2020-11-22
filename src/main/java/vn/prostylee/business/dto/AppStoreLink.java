package vn.prostylee.business.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AppStoreLink {

    @NotNull
    private String googlePlayStore;

    @NotNull
    private String appleStore;
}
