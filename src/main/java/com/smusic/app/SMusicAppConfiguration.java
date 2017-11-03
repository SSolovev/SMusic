package com.smusic.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.client.RestTemplate;

import javax.servlet.Filter;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Configuration
@EnableOAuth2Client
public class SMusicAppConfiguration  extends WebSecurityConfigurerAdapter {

    @Value("${global.pool.threads}")
    private int threadsNum;

    @Qualifier("oauth2ClientContext")
    @Autowired
    private OAuth2ClientContext oauth2ClientContext;

    @Bean
    @ConfigurationProperties("yandex.client")
    public AuthorizationCodeResourceDetails yandex() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("yandex.resource")
    public ResourceServerProperties yandexResource() {
        return new ResourceServerProperties();
    }


    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.setMessageConverters(getConverters());
        return restTemplate;
    }

    @Bean
    public ExecutorService globalExecutorPool() {
        return Executors.newFixedThreadPool(threadsNum);
    }



    @Override
    public void configure(HttpSecurity http) throws Exception {
//        http.antMatcher("/main/**").authorizeRequests().anyRequest()
//                .authenticated();
        http
                .antMatcher("/**")
                .authorizeRequests()
//                .antMatchers("/", "/login**", "/webjars/**")
                .antMatchers("/login/main","/login**", "/js/**", "/css/**")
                .permitAll().anyRequest()
                .authenticated().and().exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login/main"))
                .and().logout().logoutSuccessUrl("/login/main").permitAll().and().csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and().addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
    }

    private Filter ssoFilter() {
        OAuth2ClientAuthenticationProcessingFilter yaFilter =
                new OAuth2ClientAuthenticationProcessingFilter("/login/yandex");

        OAuth2RestTemplate yaTemplate = new OAuth2RestTemplate(yandex(), oauth2ClientContext);
        yaFilter.setRestTemplate(yaTemplate);

        UserInfoTokenServices tokenServices =
                new UserInfoTokenServices(yandexResource().getUserInfoUri(), yandex().getClientId());
        tokenServices.setRestTemplate(yaTemplate);

        yaFilter.setTokenServices(tokenServices);
        return yaFilter;

    }

    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(
            OAuth2ClientContextFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }



//    @Bean
//    @Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
//    public OAuth2RestOperations restTemplate() {
//        OAuth2RestTemplate template = new OAuth2RestTemplate(resource(), new DefaultOAuth2ClientContext(accessTokenRequest));
//        AccessTokenProviderChain provider = new AccessTokenProviderChain(Arrays.asList(new AuthorizationCodeAccessTokenProvider()));
//        provider.setClientTokenServices(clientTokenServices());
//        return template;
//    }
//@Autowired
//private OAuth2ClientContext oauth2Context;
//
//    @Bean
//    public OAuth2RestTemplate sparklrRestTemplate() {
//        return new OAuth2RestTemplate(sparklr(), oauth2Context);
//    }

//    @Bean
//    public FilterRegistrationBean someFilterRegistration() {
//
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(someFilter());
//        registration.addUrlPatterns("/*");
//        registration.addInitParameter("paramName", "paramValue");
//        registration.setName("someFilter");
//        registration.setOrder(1);
//        return registration;
//    }


//    public Filter someFilter() {
//        return new AuthFilter();
//    }
}
