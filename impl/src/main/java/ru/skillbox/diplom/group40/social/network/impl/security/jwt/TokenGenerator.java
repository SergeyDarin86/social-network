package ru.skillbox.diplom.group40.social.network.impl.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.JwtDto;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class TokenGenerator {
    private final JwtEncoder tokenEncoder;
    @Value("${security.accessTokenLifetime}")
    private Integer accessTokenLifeTime;
    @Value("${security.refreshTokenLifetime}")
    private Integer refreshTokenLifeTime;

    public String createAccessToken(JwtDto accessJwtDto, String tokenId, ZonedDateTime now, String refreshTokenId){
        Instant instant = now.toInstant();
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("myApp")
                .issuedAt(instant)
                .expiresAt(instant.plus(accessTokenLifeTime*24*365*3, ChronoUnit.MINUTES))
                .subject(accessJwtDto.getEmail())
                .claim("roles", accessJwtDto.getRoles())
                .claim("user_id", accessJwtDto.getUserId())
                .claim("token_id", tokenId)
                .claim("refresh_token_id", refreshTokenId)
                .build();
        return tokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    public String createRefreshToken(JwtDto accessJwtDto, String tokenId, ZonedDateTime now, String accessTokenId){
        Instant instant = now.toInstant();
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("myApp")
                .issuedAt(instant)
                .expiresAt(instant.plus(refreshTokenLifeTime, ChronoUnit.MINUTES))
                .subject(accessJwtDto.getEmail())
                .claim("roles", accessJwtDto.getRoles())
                .claim("user_id", accessJwtDto.getUserId())
                .claim("token_id", tokenId)
                .claim("access_token_id", accessTokenId)
                .build();
        return tokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

}
