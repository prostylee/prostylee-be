package vn.prostylee.location.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.constant.TargetType;
import vn.prostylee.core.dto.filter.BaseFilter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class NearestLocationFilter extends BaseFilter {

    @NotNull
    private double latitude;

    @NotNull
    private double longitude;

    @NotNull
    private TargetType targetType;

    @Override
    public String[] getSearchableFields() {
        return new String[]{
                "address",
                "state",
                "city",
                "country",
                "zipcode"
        };
    }
}
