package io.kalenz.bms.security;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimAccessor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class KeycloakAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt source) {
        return new JwtAuthenticationToken(source, Set.of(), getSubject(source));
    }

    private String getSubject(@NonNull Jwt source) {
        return Optional.of(source)
                .map(JwtClaimAccessor::getSubject)
                .orElseThrow(() -> new IllegalStateException("Unable to detect subject"));
    }
}
