package vn.prostylee.core.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class UrlUtil {

	private UrlUtil() {
		super();
	}

	/**
	 * Get base url
	 * 
	 * @param request HttpServletRequest
	 * @return http(s)://servername:port/contextPath
	 */
	public static String getBaseUrl(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();
	}

	/**
	 * Get value of the given query param
	 * 
	 * @param param The expected param
	 * @param url The url will be extracted
	 * @return
	 */
	public static String getValueOfQueryParam(String param, String url) {
		Map<String, List<String>> params = getQueryParams(url);
		if (params.containsKey(param)) {
			return params.get(param).get(0);
		}
		return "";
	}

	/**
	 * Get the query params from the given url
	 *
	 * @param url The url will be extracted
	 * @return
	 */
	public static Map<String, List<String>> getQueryParams(String url) {
		Map<String, List<String>> params = new HashMap<>();
		try {
			String[] urlParts = url.split("\\?");
			if (urlParts.length > 1) {
				String query = urlParts[1];
				for (String param : query.split("&")) {
					String[] pair = param.split("=");
					String key = URLDecoder.decode(pair[0], "UTF-8");
					String value = "";
					if (pair.length > 1) {
						value = URLDecoder.decode(pair[1], "UTF-8");
					}
					List<String> values = params.get(key);
					if (values == null) {
						params.put(key, new ArrayList<>());
					}
					params.get(key).add(value);
				}
			}

			return params;
		} catch (UnsupportedEncodingException ex) {
			log.error("Can't extract params form URL", ex);
		}
		return params;
	}

}
