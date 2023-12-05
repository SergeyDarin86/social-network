package ru.skillbox.diplom.group40.social.network.impl.utils.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.AccessJwtDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.JwtDto;
import ru.skillbox.diplom.group40.social.network.impl.mapper.auth.JwtMapper;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthUtil {
    public static AccessJwtDto getJwtDto(){
        Jwt jwt;
        try {
            jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        }catch (ClassCastException e){
            return null;
        }
        return JwtMapper.JwtDtoFromJwt(jwt);
    }
    public static UUID getUserId(){
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return UUID.fromString(jwt.getClaim("user_id"));
    }
}
