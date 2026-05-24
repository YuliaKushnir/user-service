package org.example.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;

import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
//                                .anyRequest().permitAll()

                        .requestMatchers("/api/users/register").permitAll()
                        .requestMatchers("/api/users/*/validate").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/users/**")
                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_EXECUTOR", "ROLE_ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/users/role/**")
                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_EXECUTOR", "ROLE_ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/users/managers/emails")
                        .hasAnyAuthority("ROLE_MANAGER", "ROLE_EXECUTOR", "ROLE_ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/api/users/*/personal-data")
                        .authenticated()

                        .requestMatchers(HttpMethod.PUT, "/api/users/*/role")
                        .hasAuthority("ROLE_ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/api/users/**")
                        .hasAuthority("ROLE_ADMIN")

                        .requestMatchers("/actuator/**").permitAll()

                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Map<String, Object> resourceAccess = jwt.getClaim("resource_access");

            if (resourceAccess == null) {
                return List.of();
            }
            Map<String, Object> client = (Map<String, Object>) resourceAccess.get("order-processing");

            if (client == null || client.get("roles") == null) {
                return List.of();
            }
            List<String> roles = (List<String>) client.get("roles");
            return roles.stream()
                    .map(role -> (GrantedAuthority) new SimpleGrantedAuthority(role))
                    .toList();
        });

        return converter;
    }

    @Bean
    public JwtDecoder jwtDecoder() throws Exception {

        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() { return null; }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                }
        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

        return NimbusJwtDecoder
                .withIssuerLocation("https://34.116.235.108/realms/garment_print")
                .build();
    }
}