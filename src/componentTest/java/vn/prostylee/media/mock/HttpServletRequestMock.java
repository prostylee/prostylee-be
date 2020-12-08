package vn.prostylee.media.mock;

import javax.servlet.http.HttpServletRequest;

/**
 * http(s)://servername:port/contextPath
 */
public abstract class HttpServletRequestMock implements HttpServletRequest {

    @Override
    public String getScheme() {
        return "http";
    }

    @Override
    public int getServerPort() {
        return 8090;
    }

    @Override
    public String getServerName() {
        return "localhost";
    }

    @Override
    public String getContextPath() {
        return "/api";
    }
}
