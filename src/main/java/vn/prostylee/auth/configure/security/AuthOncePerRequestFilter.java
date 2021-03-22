package vn.prostylee.auth.configure.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.prostylee.auth.exception.AuthenticationException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public abstract class AuthOncePerRequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated();
        if (!isAuthenticated) {
            try {
                isAuthenticated = setAuthIfTokenValid(request);
            } catch (AuthenticationException e) {
                log.error("Could not auth user", e);
            }
        }

        if (!isAuthenticated) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    abstract boolean setAuthIfTokenValid(HttpServletRequest request);
}
