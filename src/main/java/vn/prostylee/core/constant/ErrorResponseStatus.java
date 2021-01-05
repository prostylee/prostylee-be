package vn.prostylee.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ErrorResponseStatus {

    FILE_UPLOAD_ERROR("5001", "Uploading file to S3 bucket was failed"),
    FILE_DELETE_ERROR("5002", "Deleting file from S3 bucket was failed"),
    ;

    @Getter
    private String code;
    @Getter
    private String message;
}
