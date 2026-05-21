package org.example.userservice;

//import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
//        Dotenv dotenv = Dotenv.load();
//
//        System.setProperty("POSTGRES_USER", dotenv.get("POSTGRES_USER"));
//        System.setProperty("POSTGRES_PASSWORD", dotenv.get("POSTGRES_PASSWORD"));
//
//        System.setProperty("KEYCLOAK_REALM", dotenv.get("KEYCLOAK_REALM"));
//        System.setProperty("KEYCLOAK_CLIENT_ID", dotenv.get("KEYCLOAK_CLIENT_ID"));
//        System.setProperty("KEYCLOAK_USERNAME", dotenv.get("KEYCLOAK_USERNAME"));
//        System.setProperty("KEYCLOAK_PASSWORD", dotenv.get("KEYCLOAK_PASSWORD"));
//        System.setProperty("KEYCLOAK_TARGET_CLIENT_ID", dotenv.get("KEYCLOAK_TARGET_CLIENT_ID"));

        SpringApplication.run(UserServiceApplication.class, args);
    }

}
