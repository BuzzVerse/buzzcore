package dev.buzzverse.buzzcore.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private final String apiKey;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String headerValue = request.getHeader("X-API-KEY");
        System.out.println("Received API Key: " + headerValue);
        System.out.println("Expected API Key: " + apiKey);
        if (headerValue == null || !headerValue.equals(apiKey)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid or missing API Key");
            return;
        }

        ApiKeyAuthenticationToken token = new ApiKeyAuthenticationToken(headerValue, true);

        SecurityContextHolder.getContext().setAuthentication(token);
        filterChain.doFilter(request, response);
    }
}