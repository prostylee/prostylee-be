package vn.prostylee.auth.dto.filter;

import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;
import lombok.Data;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserFilter extends BaseFilter {

	List<String> roleCodes;
	String email;
    String username;

    @Override
    public String[] getSearchableFields() {
        return new String[] {
                "username",
                "fullName",
                "phoneNumber",
                "email"
        };
    }

    @Override
    public String[] getSortableFields() {
        return new String[] {
                "username",
                "fullName",
                "gender",
                "phoneNumber",
                "email",
                "active"
        };
    }
    
    
}