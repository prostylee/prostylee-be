package vn.prostylee.auth.dto.response;

import lombok.Data;

@Data
public class AppClientResponse {

	private Long id;

	private String name;

	private String secretKey;

	private String description;

	private Boolean active;

	private String type;

}
