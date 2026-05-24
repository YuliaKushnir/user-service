package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import org.example.userservice.data.User;
import org.example.userservice.data.enums.Role;
import org.example.userservice.dto.SaveUserDto;
import org.example.userservice.dto.UpdateUserPersonalDataDto;
import org.example.userservice.dto.UserDto;
import org.example.userservice.dto.UserPersonalData;
import org.example.userservice.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final KeycloakAdminService keycloakAdminService;

    public UserDto registerUser(SaveUserDto user) {
        if (repository.existsByEmail(user.getEmail())) {
            User existingUser = repository.findByEmail(user.getEmail()).get();
            return mapUserToUserDto(existingUser);
        }

        User registeredUser = new User();
        registeredUser.setEmail(user.getEmail());
        registeredUser.setPassword(user.getPassword());
        registeredUser.setKeycloakId(user.getKeycloakId());
        registeredUser.setFirstName(user.getFirstName());
        registeredUser.setLastName(user.getLastName());
        registeredUser.setRole(Role.CLIENT);

        User savedUser = repository.save(registeredUser);

        return mapUserToUserDto(savedUser);
    }

    public UserPersonalData getUserById(String userId) {
        User user = repository.getUserByKeycloakId(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        return mapUserToUserPersonalData(user);
    }

    public UserPersonalData getUserShortInfoByKeycloakId(String userId) {
        User user = repository.getUserByKeycloakId(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        return mapUserToUserPersonalData(user);
    }

    public Boolean existByUserId(String userId) {
        return repository.existsByKeycloakId(userId);
    }

    public UserDto findByEmail(String email) {
        User user = repository.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found"));
        return mapUserToUserDto(user);
    }

    public List<UserPersonalData> getAllUsers(){
        List<User> users = repository.findAll();
        List<UserPersonalData> userDtos = new ArrayList<>();
        for (User user : users) {
            userDtos.add(mapUserToUserPersonalData(user));
        }
        return userDtos;
    }

    @Override
    public List<UserPersonalData> getUsersByRole(Role role) {
        List<User> users = repository.findAllByRole(role);
        List<UserPersonalData> result = new ArrayList<>();

        for (User user : users) {
            result.add(mapUserToUserPersonalData(user));
        }

        return result;
    }

    private UserDto mapUserToUserDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setKeycloakId(user.getKeycloakId());
        userDto.setPassword(user.getPassword());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setUpdatedAt(user.getUpdatedAt());

        return userDto;
    }

    private UserPersonalData mapUserToUserPersonalData(User user) {
        UserPersonalData userPersonalData = new UserPersonalData();
        userPersonalData.setId(user.getId());
        userPersonalData.setKeycloakId(user.getKeycloakId());
        userPersonalData.setFirstName(user.getFirstName());
        userPersonalData.setLastName(user.getLastName());
        userPersonalData.setEmail(user.getEmail());
        userPersonalData.setTelephone(user.getTelephone());
        userPersonalData.setCompany(user.getCompany());
        userPersonalData.setAddress(user.getAddress());
        userPersonalData.setRole(user.getRole());

        return userPersonalData;
    }

    public List<String> getManagersEmails() {
        return repository.findAllByRole(Role.MANAGER)
                .stream()
                .map(User::getEmail)
                .toList();
    }

    @Override
    public UserPersonalData updatePersonalData(String keycloakId, UpdateUserPersonalDataDto dto) {
        String currentUserId = getCurrentUserId();
        checkSelf(currentUserId, keycloakId);

        User user = repository.getUserByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setTelephone(dto.getTelephone());
        user.setCompany(dto.getCompany());
        user.setAddress(dto.getAddress());

        User saved = repository.save(user);

        return mapUserToUserPersonalData(saved);
    }

    @Override
    public void deleteUser(String keycloakId) {
        User user = repository
                .getUserByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        repository.delete(user);
    }

    // update role by admin
    @Override
    public UserPersonalData updateRole(String keycloakId, Role role) {
        keycloakAdminService.removeAllClientRoles(keycloakId);
        keycloakAdminService.assignRole(keycloakId, role);

        User user = repository.getUserByKeycloakId(keycloakId).orElseThrow();
        user.setRole(role);
        repository.save(user);

        return mapUserToUserPersonalData(user);
    }

    private void checkSelf(String currentUserId, String targetUserId) {
        if (!currentUserId.equals(targetUserId)) {
            throw new RuntimeException("Access denied");
        }
    }

    private String getCurrentUserId() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }
}

