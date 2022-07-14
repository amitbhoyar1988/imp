package com.ps.RESTful.security;


import org.jboss.logging.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static java.util.Objects.requireNonNull;

public final class JwtAuthentication
implements Authentication {
	Logger logger = Logger.getLogger(JwtAuthentication.class);
    private final JwtToken rawToken;
    private final JwtUserDetails user;

    public JwtAuthentication(JwtToken rawToken, JwtUserDetails user) {
        this.rawToken = requireNonNull(rawToken);
        this.user     = user;
    }

    @Override
    public JwtToken getCredentials() {
        return rawToken;
    }

    @Override
    public JwtUserDetails getPrincipal() {
        return user;
    }

    @Override
    public String getName() {
        return user.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		logger.error("Authentication must be done using privileges." );

        throw new UnsupportedOperationException(
            "Authentication must be done using privileges."
        );
    }
}
