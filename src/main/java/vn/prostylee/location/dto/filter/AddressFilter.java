package vn.prostylee.location.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;

@Data
@EqualsAndHashCode(callSuper=true)
public class AddressFilter extends BaseFilter {

    private String code;

    private String parentCode;
}
