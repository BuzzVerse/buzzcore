package dev.buzzverse.buzzcore.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class ApiKeyInterceptor implements HandlerInterceptor {

    @Value("${chirpstack.events.api_key}")
    private String apiKey;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        boolean hasAnnotation = handlerMethod.hasMethodAnnotation(RequireApiKey.class)
                || handlerMethod.getBeanType().isAnnotationPresent(RequireApiKey.class);

        if (!hasAnnotation) {
            return true;
        }

        String headerValue = request.getHeader("X-API-KEY");
        if (headerValue == null || !headerValue.equals(apiKey)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        return true;
    }
}