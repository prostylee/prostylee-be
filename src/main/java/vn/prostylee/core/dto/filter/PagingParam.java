package vn.prostylee.core.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PagingParam {

    public static final int NUMBER_OF_RECORD_DEFAULT = 30;

    @Min(value = 0)
    private int page = 0;

    @Min(value = 1)
    private int limit = NUMBER_OF_RECORD_DEFAULT;
}
