package vn.prostylee.media.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class AttachmentResponse implements Serializable {

	private Long id;

	private String name;

	private String type;

	private String path;

	private String url;

	private String displayName;

	private Long sizeInKb;
}
