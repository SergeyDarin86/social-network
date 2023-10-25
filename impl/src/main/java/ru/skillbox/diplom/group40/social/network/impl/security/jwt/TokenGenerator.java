package ru.skillbox.diplom.group40.social.network.impl.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.AccessJwtDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.JwtDto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class TokenGenerator {
    private final JwtEncoder tokenEncoder;
    @Value("${security.accessTokenLifetime}")
    private Integer accessTokenLifeTime;
    @Value("${security.refreshTokenLifetime}")
    private Integer refreshTokenLifeTime;

    public String createAccessToken(JwtDto accessJwtDto, String tokenId, LocalDateTime now, String refreshTokenId){
        Instant instant = now.atZone(ZoneId.systemDefault()).toInstant();
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("myApp")
                .issuedAt(instant)
                .expiresAt(instant.plus(accessTokenLifeTime, ChronoUnit.MINUTES))
                .subject(accessJwtDto.getEmail())
                .claim("roles", accessJwtDto.getRoles())
                .claim("user_id", accessJwtDto.getUserId())
                .claim("token_id", tokenId)
                .claim("refresh_token_id", refreshTokenId)
                .build();
        return tokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    public String createRefreshToken(JwtDto accessJwtDto, String tokenId, LocalDateTime now){
        Instant instant = now.atZone(ZoneId.systemDefault()).toInstant();
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("myApp")
                .issuedAt(instant)
                .expiresAt(instant.plus(refreshTokenLifeTime, ChronoUnit.MINUTES))
                .subject(accessJwtDto.getEmail())
                .claim("roles", accessJwtDto.getRoles())
                .claim("user_id", accessJwtDto.getUserId())
                .claim("token_id", tokenId)
                .build();
        return tokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

}
