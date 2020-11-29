package vn.prostylee.app.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SocialNetwork {

	@NotBlank
	private String name;

	@NotBlank
	private String link;

	@NotBlank
	private String icon;

}
