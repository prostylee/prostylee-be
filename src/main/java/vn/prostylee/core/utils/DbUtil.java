package vn.prostylee.core.utils;

import org.apache.commons.lang3.StringUtils;

public class DbUtil {

	public static final String getSearchLikeQueryValue(String strValue) {
		return "%" + trimAndStripAccents(strValue).toLowerCase() + "%";
	}
	
	private static String trimAndStripAccents(String string) {
        return StringUtils.stripAccents(string.trim().replaceAll("\\s+", " "));
    }
}
