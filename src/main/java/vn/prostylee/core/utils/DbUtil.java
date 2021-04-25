package vn.prostylee.core.utils;

import org.apache.commons.lang3.StringUtils;

public final class DbUtil {

	private DbUtil() {}

	public static String buildSearchLikeQuery(String str) {
		return "%" + trimAndStripAccents(str).toLowerCase() + "%";
	}
	
	private static String trimAndStripAccents(String str) {
        return StringUtils.stripAccents(StringUtils.trimToEmpty(str)
				.replaceAll("\\s+", " "))
				.replace("_", "\\_")
				.replace("%", "\\%")
				;
    }
}
