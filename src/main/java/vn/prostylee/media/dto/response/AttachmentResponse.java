package vn.prostylee.media.dto.response;

import lombok.Data;

@Data
public class AttachmentResponse {

	private Long id;

	private String name;

	private String type;

	private String path;

	private String displayName;

	private Long sizeInKb;
}
