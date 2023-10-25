package ru.skillbox.diplom.group40.social.network.impl.mapper.auth;


import org.springframework.security.oauth2.jwt.Jwt;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.AccessJwtDto;

public class JwtMapper {
    public static AccessJwtDto JwtDtoFromJwt(Jwt jwt){
        AccessJwtDto accessJwtDto = new AccessJwtDto();
        accessJwtDto.setTokenId(jwt.getClaim("token_id"));
        accessJwtDto.setRefreshTokenId(jwt.getClaim("refresh_token_id"));
        accessJwtDto.setUserId(jwt.getClaim("user_id"));
        accessJwtDto.setEmail(jwt.getSubject());
        accessJwtDto.setRoles(jwt.getClaim("roles"));
        return accessJwtDto;
    }
}
