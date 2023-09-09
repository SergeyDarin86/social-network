package ru.skillbox.diplom.group40.social.network.impl.security.cookie;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

@Slf4j
@Component
@RequiredArgsConstructor
public class CookieFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException
    {
        log.info(soutRequestInfo(request));
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        final Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) { // Название вашей куки
                    jwt = cookie.getValue();
                    request.setAttribute("Authorization", "Bearer " + jwt);
                    System.out.println("переложили токен");
                    filterChain.doFilter(new JwtTokenWrapper(request, jwt), response);
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);

    }


    private String soutRequestInfo(HttpServletRequest request) throws IOException {
        StringBuilder builder = new StringBuilder("\n--------------------------------------------------------------\nrequest received:\n");
        Enumeration<String> headers = request.getHeaderNames();
        builder.append("\t").append("headers:").append("\n");
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            builder.append("\t\t").append(header).append(": ").append(request.getHeader(header)).append("\n");
        }
        builder.append("\t").append("cookies:").append("\n");
        Cookie[] cookies =  request.getCookies();
        for(Cookie cookie : cookies){
            builder.append("\t\t").append(cookie.getName()).append(": ").append(cookie.getValue()).append("\n");
        }
        if(request.getContentType()!=null && request.getContentType().equals("application/json")){
            builder.append("\t").append("json:").append("\n");
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append("\t\t").append(line).append("\n");
            }
        }

        builder.append("--------------------------------------------------------------\n");
        return builder.toString();
    }

}
