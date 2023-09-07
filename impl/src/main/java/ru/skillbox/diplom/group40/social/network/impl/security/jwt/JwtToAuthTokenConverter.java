package ru.skillbox.diplom.group40.social.network.impl.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtToAuthTokenConverter implements Converter<Jwt, JwtAuthenticationToken> {

    @Override
    public JwtAuthenticationToken convert(Jwt jwt) {
        List<String> rolesNames = jwt.getClaim("roles");
        List<GrantedAuthority> authorities = getAuthorities(rolesNames);
        return new JwtAuthenticationToken(jwt, authorities);
    }

    private List<GrantedAuthority> getAuthorities(List<String> roles) {

        ArrayList<GrantedAuthority> list = new ArrayList<>();
        for(String role: roles){
            list.add(new SimpleGrantedAuthority(role));
        }
        return list;
    }
}
