package org.example.userservice.service;

import org.example.userservice.data.enums.Role;
import org.example.userservice.dto.SaveUserDto;
import org.example.userservice.dto.UpdateUserPersonalDataDto;
import org.example.userservice.dto.UserDto;
import org.example.userservice.dto.UserPersonalData;

import java.util.List;

/**
 * Service Interface for users operations.
 */
public interface UserService {

    UserDto registerUser(SaveUserDto user);

    UserDto findByEmail(String email);

    List<UserPersonalData> getAllUsers();

    UserPersonalData getUserById(String id);

    UserPersonalData getUserShortInfoByKeycloakId(String id);

    Boolean existByUserId(String userId);

    List<UserPersonalData> getUsersByRole(Role role);

    List<String> getManagersEmails();

    UserPersonalData updatePersonalData(String keycloakId, UpdateUserPersonalDataDto dto);

    void deleteUser(String keycloakId);

    UserPersonalData updateRole(String keycloakId, Role role);

}
