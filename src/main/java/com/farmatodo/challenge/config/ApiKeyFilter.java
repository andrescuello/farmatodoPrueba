
package com.farmatodo.challenge.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${app.security.apiKeyHeader:X-API-KEY}")
    private String headerName;

    @Value("${app.security.allowedApiKeys:dev-123456}")
    private String allowedApiKeys;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.startsWith("/ping") || path.startsWith("/actuator")) {
            filterChain.doFilter(request, response); return;
        }
        List<String> allowed = Arrays.asList(allowedApiKeys.split(","));
        String apiKey = Optional.ofNullable(request.getHeader(headerName)).orElse("");
        if (!allowed.contains(apiKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("invalid api key");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
