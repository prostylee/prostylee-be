package vn.prostylee.business.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactFormRequest {

    @NotBlank
    @Length(max = 128)
    private String fullName;

    @Length(max = 64)
    private String phoneNumber;

    @NotBlank
    @Email
    @Length(max = 128)
    private String email;

    @NotBlank
    @Length(max = 128)
    private String subject;

    @NotBlank
    @Length(max = 2048)
    private String content;

    private String language;
}
