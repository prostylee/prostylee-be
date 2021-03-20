package vn.prostylee.core.dto.filter;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class PagingParam {

    public static final int NUMBER_OF_RECORD_DEFAULT = 30;

    @Min(value = 0)
    private int page = 0;

    @Min(value = 1)
    private int limit = NUMBER_OF_RECORD_DEFAULT;
}
