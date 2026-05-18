package org.example.userservice.config;

import lombok.Data;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Bean
    @ConfigurationProperties(prefix = "keycloak")
    public KeycloakProps keycloakProps() {
        return new KeycloakProps();
    }

    @Bean
    public Keycloak keycloak(KeycloakProps props) {
        return KeycloakBuilder.builder()
                .serverUrl(props.getServerUrl())
                .realm("master")
                .clientId(props.getClientId())
                .username(props.getUsername())
                .password(props.getPassword())
                .grantType(OAuth2Constants.PASSWORD)
                .build();
    }

    @Data
    public static class KeycloakProps {
        private String serverUrl;
        private String realm;
        private String clientId;
        private String username;
        private String password;
        private String targetClientId;
    }
}