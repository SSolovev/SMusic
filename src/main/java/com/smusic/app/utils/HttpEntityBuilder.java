package com.smusic.app.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class HttpEntityBuilder {

    private HttpHeaders httpHeaders;
    private MultiValueMap<String, String> body;

    //        postBody.add("grant_type","authorization_code");
    private HttpEntityBuilder() {
        httpHeaders = new HttpHeaders();
        body = new LinkedMultiValueMap<>();

    }

    public static HttpEntityBuilder getInstance() {

        return new HttpEntityBuilder();
    }

    public HttpEntityBuilder addHeaderValue(String headerName, String headerValue) {
        httpHeaders.add(headerName, headerValue);
        return this;
    }

    public HttpEntityBuilder addBodyValue(String bodyValName, String bodyValue) {
        body.add(bodyValName, bodyValue);
        return this;
    }

    public HttpEntity<MultiValueMap<String, String>> build() {
//        if(body.isEmpty()){
//            return new HttpEntity<>(httpHeaders);
//        }else{
            return new HttpEntity<>(body, httpHeaders);
//        }

    }
}
