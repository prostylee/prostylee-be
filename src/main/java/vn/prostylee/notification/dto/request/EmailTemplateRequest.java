package vn.prostylee.notification.dto.request;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class EmailTemplateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private String type;

    private String keyword;

}
