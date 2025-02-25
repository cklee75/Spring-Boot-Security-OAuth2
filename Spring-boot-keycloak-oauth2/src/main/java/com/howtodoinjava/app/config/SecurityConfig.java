package com.howtodoinjava.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Value;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
    private String issuerUri;

    @Value("${domain-url}")
    private String domainUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, OAuth2AuthorizedClientService clientService) throws Exception {
        http.authorizeHttpRequests(authorise -> authorise
                .requestMatchers("/public").permitAll()
                .anyRequest().authenticated())
            .oauth2Login(withDefaults())
            .logout(logout -> logout
                .logoutSuccessHandler((request, response, authentication) -> {
                    if (authentication != null && authentication.getPrincipal() instanceof OidcUser oidcUser) {
                        String idToken = oidcUser.getIdToken().getTokenValue();
                        String logoutUrl = issuerUri + "/protocol/openid-connect/logout" +
                                "?id_token_hint=" + idToken +
                                "&post_logout_redirect_uri=" + domainUrl + "/public";
                        
                        response.sendRedirect(logoutUrl);
                        return;
                        
                    }
                    response.sendRedirect("/public");
                })                
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID"))
            .headers(headers -> headers.cacheControl(cache -> cache.disable()));

       return  http.build();
    }
}
