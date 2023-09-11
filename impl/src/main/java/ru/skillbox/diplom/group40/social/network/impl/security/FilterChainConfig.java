package ru.skillbox.diplom.group40.social.network.impl.security;

import lombok.RequiredArgsConstructor;
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

    private final JwtToAuthTokenConverter jwtToAuthTokenConverter;
    private final JwtDecoder jwtDecoder;
    private final CookieFilter cookieFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http

                .authorizeHttpRequests()
                .requestMatchers("/**").permitAll()
                .anyRequest().authenticated()
//                .anyRequest().hasAuthority()
//                .anyRequest().hasRole()
                .and()
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(cookieFilter, BearerTokenAuthenticationFilter.class)
                .oauth2ResourceServer()
                .jwt().decoder(jwtDecoder)
                .jwtAuthenticationConverter(jwtToAuthTokenConverter);


        return http.build();
    }

}
