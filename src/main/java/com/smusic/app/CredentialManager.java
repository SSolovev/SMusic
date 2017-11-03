package com.smusic.app;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class CredentialManager implements Serializable {


    public CredentialManager() {
    }

    public String getUserName() {
        return getAuth().getPrincipal().toString();
    }

    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public String getToken() {
        OAuth2AuthenticationDetails userDetails = (OAuth2AuthenticationDetails) getAuth().getDetails();
        return userDetails.getTokenValue();
    }

}
