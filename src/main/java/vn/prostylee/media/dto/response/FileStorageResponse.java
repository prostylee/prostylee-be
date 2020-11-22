package vn.prostylee.media.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileStorageResponse {

	private Integer type;

	private String filePath;

	private String displayName;

	private String fileUrl;

	private LocalDateTime createdDate;

}
