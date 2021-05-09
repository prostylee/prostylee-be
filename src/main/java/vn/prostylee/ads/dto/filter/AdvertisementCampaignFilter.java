package vn.prostylee.ads.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdvertisementCampaignFilter extends BaseFilter {

    private Long groupId;
    private String targetType;
    private String position;
    private Boolean active;

    @Override
    public String[] getSearchableFields() {
        return new String[] {
                "name",
                "description",
                "position",
        };
    }

    @Override
    public String[] getSortableFields() {
        return new String[] {
                "name",
                "description",
                "position",
        };
    }
}
