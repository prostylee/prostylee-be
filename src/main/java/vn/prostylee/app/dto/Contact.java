package vn.prostylee.app.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Contact {

	@NotNull
	private String address;

	private String googleMapLink;

	@NotNull
	private String phoneNumber1;

	private String phoneNumber2;

	private String phoneNumber3;

	@NotNull
	private String email;

}
