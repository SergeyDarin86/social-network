package ru.skillbox.diplom.group40.social.network.impl.utils.technikalUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.JwtDto;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.UUID;
import java.util.function.Supplier;
@Slf4j
@Component
@RequiredArgsConstructor
public class TechnicalUserConfig {
    private final JwtEncoder accessTokenEncoder;

    public <T> T executeByTechnicalUser(Supplier<T> lambda) {
        log.info("TechnicalUserConfig:executeByTechnicalUser start method");
        try {
            createContextForKafka();
            return lambda.get();
        } finally {
            System.out.println(SecurityContextHolder.createEmptyContext());
        }
    }

    private void createContextForKafka() {
        log.info("TechnicalUserConfig:createContextForKafka start method");
        JwtDto jwtDto = new JwtDto();
        jwtDto.setUserId(UUID.randomUUID().toString());
        jwtDto.setEmail("kafka@email");
        jwtDto.setRoles(Collections.singletonList("ROLES_KAFKA"));
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(createToken(jwtDto));
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(jwtAuthenticationToken);
    }

    public Jwt createToken(JwtDto jwtDto) {
        log.info("TechnicalUserConfig:createToken start method");
        Instant now = Instant.now();
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("myApp")
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.MINUTES))
                .subject(jwtDto.getEmail())
                .claim("roles", jwtDto.getRoles())
                .claim("user_id", jwtDto.getUserId())
                .build();
        return accessTokenEncoder.encode(JwtEncoderParameters.from(claimsSet));
    }

}
