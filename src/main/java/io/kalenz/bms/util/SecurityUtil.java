package io.kalenz.bms.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;

@UtilityClass
public class SecurityUtil {
    private static final String SUBJECT = "preferred_username";

    public static String getCurrentUsername() {
        var ctxHolder = SecurityContextHolder.getContext();
        var authentication = ctxHolder.getAuthentication();
        if (authentication == null) return null;
        var principal = authentication.getPrincipal();
        if (principal instanceof String username) {
            return username;
        } else if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        } else if (principal instanceof Jwt jwt) {
            return jwt.getClaimAsString(SUBJECT);
        }
        return null;
    }
}
