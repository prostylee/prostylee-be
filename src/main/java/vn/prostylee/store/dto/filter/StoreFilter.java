package vn.prostylee.store.dto.filter;

import com.google.firebase.database.annotations.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.location.dto.LatLngDto;

import javax.validation.Valid;

@Data
@EqualsAndHashCode(callSuper = true)
public class StoreFilter extends BaseFilter  {

    private Long companyId;

    private Integer status;

    private Long ownerId;

    private Boolean bestSeller;

    private Double latitude;

    private Double longitude;

    private int numberOfProducts;

    @Override
    public String[] getSearchableFields() {
        return new String[] {
                "name",
                "address",
                "website",
                "phone"
        };
    }

    @Override
    public String[] getSortableFields() {
        return new String[] {
                "name",
                "address",
                "website",
                "phone",
                "status",
        };
    }
}
