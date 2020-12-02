package vn.prostylee.core.configuration.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import java.io.IOException;
import java.time.LocalDateTime;

// TODO Implement more logic for tracing
@Slf4j
@Order(0)
public class EventLogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("init EventLogFilter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.debug("doFilter EventLogFilter");
        request.setAttribute("startTime", LocalDateTime.now());
        // TODO Put MDC values
        chain.doFilter(request, response);
        // TODO Clear MDC values
    }

    @Override
    public void destroy() {
        log.debug("destroy EventLogFilter");
    }
}
