package dev.buzzverse.buzzcore.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {

    private final String apiKey;

    public ApiKeyAuthenticationToken(String apiKey, boolean authenticated) {
        super(List.of(new SimpleGrantedAuthority("ROLE_APIKEY")));
        this.apiKey = apiKey;
        setAuthenticated(authenticated);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.apiKey;
    }
}