package vn.prostylee.auth.configure.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthOncePerRequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated();
        if (!isAuthenticated) {
            isAuthenticated = setAuthIfTokenValid(request);
        }

        if (!isAuthenticated) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    abstract boolean setAuthIfTokenValid(HttpServletRequest request);
}
