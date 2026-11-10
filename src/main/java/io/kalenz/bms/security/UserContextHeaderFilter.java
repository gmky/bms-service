package io.kalenz.bms.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Reads the {@code x-user-context} header (service agreement ID) once per request
 * and stores it as a request attribute so downstream components — in particular
 * {@link AccessGuard} — can access it without holding a direct reference to
 * {@link HttpServletRequest}.
 */
@Component
public class UserContextHeaderFilter extends OncePerRequestFilter {

    /** Name of the HTTP request header carrying the service agreement ID. */
    public static final String USER_CONTEXT_HEADER = "x-user-context";

    /** Request attribute key under which the extracted SA ID is stored. */
    public static final String USER_CONTEXT_ATTR = "bms.userContext.saId";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String saId = request.getHeader(USER_CONTEXT_HEADER);
        if (saId != null) {
            request.setAttribute(USER_CONTEXT_ATTR, saId);
        }
        filterChain.doFilter(request, response);
    }
}
