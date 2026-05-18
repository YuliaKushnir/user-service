package org.example.userservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private String id;
    private String keycloakId;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
