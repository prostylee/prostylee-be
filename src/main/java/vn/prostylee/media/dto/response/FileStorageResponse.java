package vn.prostylee.media.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class FileStorageResponse implements Serializable {

	private Integer type;

	private String filePath;

	private String displayName;

	private String fileUrl;

	private LocalDateTime createdDate;

}
