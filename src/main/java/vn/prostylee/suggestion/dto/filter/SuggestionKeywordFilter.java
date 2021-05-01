package vn.prostylee.suggestion.dto.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;

@Data
@EqualsAndHashCode(callSuper = true)
public class SuggestionKeywordFilter extends BaseFilter {

    @Schema(description = "The suggestion type only accepts one of PRODUCT, CATEGORY, STORE")
    private String type;
}
