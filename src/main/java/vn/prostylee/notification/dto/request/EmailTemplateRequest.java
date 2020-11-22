package vn.prostylee.notification.dto.request;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class EmailTemplateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private String type;

    private String keyword;

    @Pattern(regexp = "^(en)|(vi)$", message = "Only en or vi are accepted.")
    private String language;

}
