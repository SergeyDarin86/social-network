package ru.skillbox.diplom.group40.social.network.impl.security.cookie;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class CookieFilter extends OncePerRequestFilter {
    private byte[] data;
    private HttpServletRequest request;
    private StringBuilder logBuilder;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest incomingRequest,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        this.request = incomingRequest;
        log.debug(getRequestInfo());
        if (data != null) {
            request = new BodyWrapper(request, data);
        }
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        final Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    jwt = cookie.getValue();
                    if (jwt.isBlank()) {
                        break;
                    }
                    filterChain.doFilter(new JwtTokenWrapper(request, jwt), response);
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }


    private String getRequestInfo() {
        logBuilder = new StringBuilder("\n--------------------------------------------------------------\n");
        logBuilder.append("request received:\n");
        addUriAndMethod();
        addParameters();
        addCookies();
        try {
            addBody();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logBuilder.append("--------------------------------------------------------------\n");
        return logBuilder.toString();
    }


    private void addUriAndMethod() {
        logBuilder.append("\tURI:").append(request.getRequestURI()).append("\n");
        logBuilder.append("\tMETHOD:").append(request.getMethod()).append("\n");
    }

    private void addParameters() {
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (!parameterMap.keySet().isEmpty()) {
            logBuilder.append("\t").append("parameters:").append("\n");
            for (String key : parameterMap.keySet()) {
                logBuilder.append("\t\t").append(key).append(": ")
                        .append(Arrays.toString(parameterMap.get(key))).append("\n");
            }
        }
    }

    private void addCookies() {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            logBuilder.append("\tcookies:\n");
            for (Cookie cookie : cookies) {
                logBuilder.append("\t\t").append(cookie.getName()).append(": ").append(cookie.getValue()).append("\n");
            }
        }
    }

    private void addBody() throws IOException {
        if (!request.getInputStream().isFinished()) {
            InputStream originalInputStream = request.getInputStream();
            data = IOUtils.toByteArray(originalInputStream);
            InputStream inputStream = new ByteArrayInputStream(data);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            logBuilder.append("\tjson:\n");
            String line;
            while ((line = reader.readLine()) != null) {
                logBuilder.append("\t\t").append(line).append("\n");
            }
        }
    }
}
