package ru.skillbox.diplom.group40.social.network.impl.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import ru.skillbox.diplom.group40.social.network.impl.security.cookie.CookieFilter;
import ru.skillbox.diplom.group40.social.network.impl.security.jwt.JwtToAuthTokenConverter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class FilterChainConfig {
    @Value("${security.enabled}")
    private Boolean enabled;
    private final JwtToAuthTokenConverter jwtToAuthTokenConverter;
    private final JwtDecoder jwtDecoder;
    private final CookieFilter cookieFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String allowedPath = enabled?"/api/v1/auth/**":"/**";

        http
                .authorizeHttpRequests()
                .requestMatchers(allowedPath).permitAll()
                .requestMatchers("/api/v1/actuator/**").permitAll()
                 .requestMatchers("/api/v1/sw/**").permitAll()
                 //.requestMatchers("/api/v1/account/**").permitAll()
                .anyRequest().authenticated()
//                .anyRequest().hasAuthority()
//                .anyRequest().hasRole()
                .and()
                .csrf().disable().httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(cookieFilter, BearerTokenAuthenticationFilter.class)
                .oauth2ResourceServer()
                .jwt().decoder(jwtDecoder)
                .jwtAuthenticationConverter(jwtToAuthTokenConverter);
        return http.build();
    }

}
