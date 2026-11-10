package io.kalenz.bms.security;

import io.kalenz.bms.access.control.service.AccessCheckService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Spring Security permission guard used in {@code @PreAuthorize} SpEL expressions.
 *
 * <p>Usage:
 * <pre>{@code
 * @PreAuthorize("@accessGuard.hasPrivilege('manage.entitlements', 'job-role', 'CREATE')")
 * }</pre>
 *
 * <p>The user identity is taken from the JWT {@code sub} claim (a Keycloak UUID), which
 * {@link KeycloakAuthenticationConverter} stores as the authentication principal name.
 * The service agreement ID is taken from the {@code x-user-context} request header,
 * which {@link UserContextHeaderFilter} stores as a request attribute before Spring
 * Security evaluates the method security expression.
 */
@Slf4j
@Component("accessGuard")
@RequiredArgsConstructor
public class AccessGuard {

    private final AccessCheckService accessCheckService;
    private final HttpServletRequest request;

    /**
     * Returns {@code true} if the currently authenticated user holds the specified
     * privilege within the service agreement identified by the {@code x-user-context}
     * request header.
     *
     * <p>Returns {@code false} (silent deny) in any of the following cases:
     * <ul>
     *   <li>No authenticated principal in the security context</li>
     *   <li>The {@code x-user-context} header is absent or blank</li>
     *   <li>The user has no matching {@link io.kalenz.bms.access.control.entity.UserContext}</li>
     *   <li>The user context is not in ENABLED state</li>
     *   <li>No assigned job role contains the requested privilege triple</li>
     * </ul>
     *
     * @param functionCode business function code (e.g. {@code "manage.entitlements"})
     * @param resourceCode resource type code     (e.g. {@code "job-role"})
     * @param actionCode   action code             (e.g. {@code "CREATE"})
     * @return {@code true} if access is granted; {@code false} to trigger 403
     */
    public boolean hasPrivilege(String functionCode, String resourceCode, String actionCode) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            log.debug("Permission check denied — no authenticated principal");
            return false;
        }

        // KeycloakAuthenticationConverter sets principal name = sub claim (Keycloak UUID)
        String userId = auth.getName();
        String saId = (String) request.getAttribute(UserContextHeaderFilter.USER_CONTEXT_ATTR);

        if (saId == null || saId.isBlank()) {
            log.debug("Permission check denied — x-user-context header missing (user={})", userId);
            return false;
        }

        boolean allowed = accessCheckService.hasPrivilege(userId, saId, functionCode, resourceCode, actionCode);
        log.debug("Permission check: user={} sa={} fn={} res={} action={} → {}",
                userId, saId, functionCode, resourceCode, actionCode, allowed);
        return allowed;
    }
}
