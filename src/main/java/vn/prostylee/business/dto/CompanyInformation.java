package vn.prostylee.business.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CompanyInformation {

    @NotNull
    private String name;

    @NotNull
    private String address;

    private String businessCode;

    private String legalRepresentative;

    private String position;
}
