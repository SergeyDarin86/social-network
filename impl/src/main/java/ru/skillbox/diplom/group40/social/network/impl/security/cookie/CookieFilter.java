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

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        if (log.isDebugEnabled()) {
            byte[] data = getDataFromRequest(request);
            log.debug(getRequestInfo(request, data));
            if (data!=null) {
                request = new BodyWrapper(request, data);
            }
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

    private byte[] getDataFromRequest(HttpServletRequest incomingRequest) throws IOException {
        if (!incomingRequest.getInputStream().isFinished()) {
            return null;
        }
        InputStream originalInputStream = incomingRequest.getInputStream();
        return IOUtils.toByteArray(originalInputStream);
    }


    private String getRequestInfo(HttpServletRequest request, byte[] data) {
        StringBuilder logBuilder= new StringBuilder("\n--------------------------------------------------------------\n");
        logBuilder.append("request received:\n");
        addUriAndMethod(request, logBuilder);
        addParameters(request, logBuilder);
        if(log.isTraceEnabled()){
            addHeaders(request, logBuilder);
        }
        addCookies(request, logBuilder);
        if (data == null) {
            return logBuilder.toString();
        }
        try {
            addBody(logBuilder, data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logBuilder.append("--------------------------------------------------------------\n");
        return logBuilder.toString();
    }


    private void addUriAndMethod(HttpServletRequest request, StringBuilder logBuilder) {
        logBuilder.append("\tURI:").append(request.getRequestURI()).append("\n");
        logBuilder.append("\tMETHOD:").append(request.getMethod()).append("\n");
    }

    private void addParameters(HttpServletRequest request, StringBuilder logBuilder) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap.keySet().isEmpty()) {
            return;
        }
        logBuilder.append("\t").append("parameters:").append("\n");
        for (String key : parameterMap.keySet()) {
            logBuilder.append("\t\t").append(key).append(": ")
                    .append(Arrays.toString(parameterMap.get(key))).append("\n");
        }
    }

    private void addHeaders(HttpServletRequest request, StringBuilder logBuilder) {
        Enumeration<String> headers = request.getHeaderNames();
        if (!headers.hasMoreElements()) {
            return;
        }
        logBuilder.append("\t").append("headers:").append("\n");
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            logBuilder.append("\t\t").append(header).append(": ")
                    .append(request.getHeader(header)).append("\n");
        }
    }

    private void addCookies(HttpServletRequest request, StringBuilder logBuilder) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            logBuilder.append("\tcookies:\n");
            for (Cookie cookie : cookies) {
                logBuilder.append("\t\t").append(cookie.getName()).append(": ").append(cookie.getValue()).append("\n");
            }
        }
    }

    private void addBody(StringBuilder logBuilder, byte[] data) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(data);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        logBuilder.append("\tjson:\n");
        String line;
        while ((line = reader.readLine()) != null) {
            logBuilder.append("\t\t").append(line).append("\n");
        }

    }
}
