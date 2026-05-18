package org.example.userservice.dto;

import lombok.Data;
import org.example.userservice.data.enums.Role;

@Data
public class UpdateRoleDto {
    private Role role;
}