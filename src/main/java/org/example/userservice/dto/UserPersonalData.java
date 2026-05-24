package org.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.userservice.data.enums.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPersonalData {
    private String id;
    private String keycloakId;
    private String firstName;
    private String lastName;
    private String email;
    private String telephone;
    private String company;
    private String address;
    private Role role;
}
