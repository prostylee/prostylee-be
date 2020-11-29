package vn.prostylee.app.dto;

import lombok.Data;

import java.util.List;

@Data
public class AppConfiguration {

    private Contact contact;

    private AppStoreLink appStoreLink;

    private CompanyInformation companyInformation;

    private List<SocialNetwork> socialNetworks;
}
