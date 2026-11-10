package io.kalenz.bms.security;

import io.kalenz.bms.access.control.service.AccessCheckService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccessGuardTest {

    @Mock
    AccessCheckService accessCheckService;

    @Mock
    HttpServletRequest request;

    AccessGuard accessGuard;

    // Simulates KeycloakAuthenticationConverter: principal name = Keycloak sub UUID
    private static final String USER_SUB = "550e8400-e29b-41d4-a716-446655440000";
    private static final String SA_ID = "sa-agreement-123";

    @BeforeEach
    void setUp() {
        accessGuard = new AccessGuard(accessCheckService, request);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void returnsTrue_whenUserHasPrivilege() {
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(USER_SUB, null, List.of()));
        when(request.getAttribute(UserContextHeaderFilter.USER_CONTEXT_ATTR)).thenReturn(SA_ID);
        when(accessCheckService.hasPrivilege(USER_SUB, SA_ID, "manage.entitlements", "job-role", "CREATE"))
                .thenReturn(true);

        assertThat(accessGuard.hasPrivilege("manage.entitlements", "job-role", "CREATE")).isTrue();
    }

    @Test
    void returnsFalse_whenUserLacksPrivilege() {
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(USER_SUB, null, List.of()));
        when(request.getAttribute(UserContextHeaderFilter.USER_CONTEXT_ATTR)).thenReturn(SA_ID);
        when(accessCheckService.hasPrivilege(USER_SUB, SA_ID, "manage.entitlements", "job-role", "CREATE"))
                .thenReturn(false);

        assertThat(accessGuard.hasPrivilege("manage.entitlements", "job-role", "CREATE")).isFalse();
    }

    @Test
    void returnsFalse_whenHeaderMissing() {
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(USER_SUB, null, List.of()));
        when(request.getAttribute(UserContextHeaderFilter.USER_CONTEXT_ATTR)).thenReturn(null);

        assertThat(accessGuard.hasPrivilege("manage.entitlements", "job-role", "CREATE")).isFalse();
        verifyNoInteractions(accessCheckService);
    }

    @Test
    void returnsFalse_whenHeaderBlank() {
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(USER_SUB, null, List.of()));
        when(request.getAttribute(UserContextHeaderFilter.USER_CONTEXT_ATTR)).thenReturn("   ");

        assertThat(accessGuard.hasPrivilege("manage.entitlements", "job-role", "CREATE")).isFalse();
        verifyNoInteractions(accessCheckService);
    }

    @Test
    void returnsFalse_whenNotAuthenticated() {
        SecurityContextHolder.clearContext();
        // header present but no auth
        assertThat(accessGuard.hasPrivilege("manage.entitlements", "job-role", "CREATE")).isFalse();
        verifyNoInteractions(accessCheckService);
    }
}
