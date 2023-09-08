package ru.skillbox.diplom.group40.social.network.impl.mapper.auth;


import org.springframework.security.oauth2.jwt.Jwt;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.JwtDto;

import java.util.UUID;

public class JwtMapper {
    public static JwtDto JwtDtoFromJwt(Jwt jwt){
        return JwtDto.builder()
                .id(UUID.fromString(jwt.getClaim("user_id")))
                .email(jwt.getSubject())
                .roles(jwt.getClaim("roles"))
                .build();
    }
}
