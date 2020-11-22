package vn.prostylee.auth.dto.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class AppClientRequest {

	@NotBlank
	@Length(max = 128)
	private String name;

	@NotBlank
	@Length(max = 45)
	private String secretKey;

	@Length(max = 512)
	private String description;

	private Boolean active;

	@NotBlank
	private String type;

}
