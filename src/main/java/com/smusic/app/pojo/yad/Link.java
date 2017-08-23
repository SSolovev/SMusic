package com.smusic.app.pojo.yad;

import org.springframework.http.HttpMethod;


public class Link {
    private String href;
    private HttpMethod method;
    private boolean template;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public boolean isTemplate() {
        return template;
    }

    public void setTemplate(boolean template) {
        this.template = template;
    }
}
