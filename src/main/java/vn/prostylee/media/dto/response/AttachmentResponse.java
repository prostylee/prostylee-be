package vn.prostylee.media.dto.response;

import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
public class AttachmentResponse {

	private Long id;

	private String name;

	private String type;

	private String path;

	private String displayName;

	private Long sizeInKb;
}
