package vn.prostylee.media.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class FileStorageRequest {

	private List<FileStorageItemRequest> types;

}
