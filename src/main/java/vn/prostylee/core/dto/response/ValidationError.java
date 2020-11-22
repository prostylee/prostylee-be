package vn.prostylee.core.dto.response;

import lombok.Data;

@Data
public class ValidationError {

    private String code;

    private String message;
}
