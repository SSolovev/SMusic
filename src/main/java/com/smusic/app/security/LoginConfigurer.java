//package com.smusic.app.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
//import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
//import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.oauth2.client.OAuth2ClientContext;
//import org.springframework.security.oauth2.client.OAuth2RestTemplate;
//import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
//import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
//import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
//import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.Filter;
//
//
//@Configuration
//@EnableOAuth2Client
//public class LoginConfigurer extends WebSecurityConfigurerAdapter {
//
//    @Qualifier("oauth2ClientContext")
//    @Autowired
//    private OAuth2ClientContext oauth2ClientContext;
//
//
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
////        http.antMatcher("/main/**").authorizeRequests().anyRequest()
////                .authenticated();
//        http
//                .antMatcher("/**")
//                .authorizeRequests()
//                .antMatchers("/", "/login**", "/webjars/**")
//                .permitAll().anyRequest()
//                .authenticated().and().exceptionHandling()
//                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/"))
//                .and().logout().logoutSuccessUrl("/").permitAll().and().csrf()
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .and().addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
//    }
//
//    private Filter ssoFilter() {
//        OAuth2ClientAuthenticationProcessingFilter yaFilter =
//                new OAuth2ClientAuthenticationProcessingFilter("/login/yandex");
//
//        OAuth2RestTemplate yaTemplate = new OAuth2RestTemplate(yandex(), oauth2ClientContext);
//        yaFilter.setRestTemplate(yaTemplate);
//
//        UserInfoTokenServices tokenServices =
//                new UserInfoTokenServices(yandexResource().getUserInfoUri(), yandex().getClientId());
//        tokenServices.setRestTemplate(yaTemplate);
//
//        yaFilter.setTokenServices(tokenServices);
//        return yaFilter;
//
//    }
//
//    @Bean
//    public FilterRegistrationBean oauth2ClientFilterRegistration(
//            OAuth2ClientContextFilter filter) {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(filter);
//        registration.setOrder(-100);
//        return registration;
//    }
//
//
//    @Bean
//    @ConfigurationProperties("yandex.client")
//    public AuthorizationCodeResourceDetails yandex() {
//        return new AuthorizationCodeResourceDetails();
//    }
//
//    @Bean
//    @ConfigurationProperties("yandex.resource")
//    public ResourceServerProperties yandexResource() {
//        return new ResourceServerProperties();
//    }
//}