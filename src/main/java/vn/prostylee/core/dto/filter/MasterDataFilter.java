package vn.prostylee.core.dto.filter;

public class MasterDataFilter extends BaseFilter {

    public String[] getSearchableFields() {
        return new String[]{
                "name"
        };
    }

    public String[] getSortableFields() {
        return new String[]{
                "id",
                "name",
                "description",
                "createdAt"
        };
    }
}
