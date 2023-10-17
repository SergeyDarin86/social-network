package ru.skillbox.diplom.group40.social.network.impl.security.jwt;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.impl.service.auth.AuthService;

@Component
@RequiredArgsConstructor
public class JwtToAuthTokenConverter implements Converter<Jwt, JwtAuthenticationToken> {
    private final AuthService authService;

    @Override
    public JwtAuthenticationToken convert(@NonNull Jwt jwt) {
        authService.checkIfAccessTokenIsActive(jwt.getClaim("token_id"));
        return new JwtAuthenticationToken(jwt, null);
    }

}
