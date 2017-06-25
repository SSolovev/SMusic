package com.smusic.app.filter;

import com.smusic.app.CredentialManager;
import org.apache.catalina.connector.RequestFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by sergey on 23.06.17.
 */
@Component
public class AuthFilter implements Filter {

    @Autowired
    private CredentialManager credentialManager;

    @Value("${yandex.authorize.url}")
    private String authorizeUrl;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                filterConfig.getServletContext());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        if (!((HttpServletRequest) servletRequest).getRequestURI().equals("/callback") && credentialManager.isEmptyToken()) {
//            ((HttpServletResponse) servletResponse).sendRedirect(authorizeUrl);
//        } else {
            filterChain.doFilter(servletRequest, servletResponse);
//        }

    }

    @Override
    public void destroy() {

    }
}
