package com.instaworkflow.resumeapi.security;

import com.instaworkflow.resumeapi.model.Tenant;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {

    private final String apiKey;
    private final Tenant tenant;

    public ApiKeyAuthenticationToken(String apiKey, Tenant tenant, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.apiKey = apiKey;
        this.tenant = tenant;
        setAuthenticated(true);
    }

    public ApiKeyAuthenticationToken(String apiKey) {
        super(null);
        this.apiKey = apiKey;
        this.tenant = null;
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return apiKey;
    }

    @Override
    public Object getPrincipal() {
        return tenant;
    }
}
