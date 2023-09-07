package ru.skillbox.diplom.group40.social.network.impl.security.cookie;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class JwtTokenWrapper extends HttpServletRequestWrapper {

    private final String jwtToken;

    public JwtTokenWrapper(HttpServletRequest request, String jwtToken) {
        super(request);
        this.jwtToken = jwtToken;
    }

    @Override
    public String getHeader(String name) {
        if ("Authorization".equalsIgnoreCase(name)) {
            return "Bearer " + jwtToken;
        }
        return super.getHeader(name);
    }
}
