package vn.prostylee.app.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;

@Data
@EqualsAndHashCode(callSuper = true)
public class MasterDataFilter extends BaseFilter {

	@Override
	public String[] getSearchableFields() {
		return null;
	}

	@Override
	public String[] getSortableFields() {
		return null;
	}

}
