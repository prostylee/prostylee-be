package vn.prostylee.business.dto.filter;

import lombok.Data;
import vn.prostylee.core.dto.filter.BaseFilter;

@Data
public class MasterDataFilter extends BaseFilter {

	private String language;

	@Override
	public String[] getSearchableFields() {
		return null;
	}

	@Override
	public String[] getSortableFields() {
		return null;
	}

}
