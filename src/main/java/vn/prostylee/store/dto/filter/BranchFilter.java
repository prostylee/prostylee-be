package vn.prostylee.store.dto.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;

@Data
@EqualsAndHashCode(callSuper = true)
public class BranchFilter extends BaseFilter  {

    private Long storeId;

    private String cityCode;

    private Boolean active;

    private Double latitude;

    private Double longitude;

    @Override
    public String[] getSearchableFields() {
        return new String[] {
                "name",
        };
    }

    @Override
    public String[] getSortableFields() {
        return new String[] {
                "name",
                "description",
                "active",
                "createdAt",
                "updatedAt",
                "cityCode",
                "districtCode"
        };
    }

    @Schema(name = "sorts", example = "sorts=+name&-cityCode&districtCode")
    @Override
    public String[] getSorts() {
        return super.getSorts();
    }
}
