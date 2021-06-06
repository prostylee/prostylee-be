package vn.prostylee.ads.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdvertisementBannerFilter extends BaseFilter {

    private Long groupId;
    private String position;

    @Override
    public String[] getSearchableFields() {
        return new String[] {
                "name",
                "description",
        };
    }

    @Override
    public String[] getSortableFields() {
        return new String[] {
                "name",
                "description",
                "order",
        };
    }
}
