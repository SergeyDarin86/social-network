package ru.skillbox.diplom.group40.social.network.impl.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.JwtDto;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenGenerator {
    private final JwtEncoder accessTokenEncoder;

    public String createToken(JwtDto jwtDto) {
        Instant now = Instant.now();
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("myApp")
                .issuedAt(now)
                .expiresAt(now.plus(365, ChronoUnit.DAYS))
                .subject(jwtDto.getEmail())
                .claim("roles", jwtDto.getRoles())
                .claim("user_id", jwtDto.getId())
                .build();

        return accessTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }
}
