package com.smusic.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class CredentialManager implements Serializable {
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public void setToken(String token) {
//        this.token = token;
//    }
//
//    private String userName;
//    @Value("${dev.token}")
//    private String token;

    //    public CredentialManager credentialManager(){return new CredentialManager();}
    public CredentialManager() {
    }
//
//    public CredentialManager(String userName, String token) {
////        this.userName = userName;
////        this.token = token;
//    }

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
