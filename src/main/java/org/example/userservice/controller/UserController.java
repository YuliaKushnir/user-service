package org.example.userservice.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.userservice.data.enums.Role;
import org.example.userservice.dto.SaveUserDto;
import org.example.userservice.dto.UpdateRoleDto;
import org.example.userservice.dto.UpdateUserPersonalDataDto;
import org.example.userservice.dto.UserDto;
import org.example.userservice.dto.UserPersonalData;
import org.example.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto register(@Valid @RequestBody SaveUserDto user){
        return userService.registerUser(user);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserPersonalData> findAll() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}/validate")
    @ResponseStatus(HttpStatus.OK)
    public Boolean validateUser(@PathVariable String userId){
        return userService.existByUserId(userId);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserPersonalData getUserById(@PathVariable String userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/{userId}/short-info")
    @ResponseStatus(HttpStatus.OK)
    public UserPersonalData getUserShortInfo(@PathVariable String userId) {
        return userService.getUserShortInfoByKeycloakId(userId);
    }

    @GetMapping("/managers/emails")
    public List<String> getManagersEmails() {
        return userService.getManagersEmails();
    }

    @PutMapping("/{keycloakId}/personal-data")
    @ResponseStatus(HttpStatus.OK)
    public UserPersonalData updatePersonalData(@PathVariable String keycloakId, @RequestBody UpdateUserPersonalDataDto dto) {
        return userService.updatePersonalData(keycloakId, dto);
    }

    @DeleteMapping("/{keycloakId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String keycloakId) {
        userService.deleteUser(keycloakId);
    }

    @PutMapping("/{keycloakId}/role")
    @ResponseStatus(HttpStatus.OK)
    public UserPersonalData updateRole(@PathVariable String keycloakId, @RequestBody UpdateRoleDto dto) {
        return userService.updateRole(keycloakId, dto.getRole());
    }

    @GetMapping("/role/{role}")
    @ResponseStatus(HttpStatus.OK)
    public List<UserPersonalData> getUsersByRole(@PathVariable Role role) {
        return userService.getUsersByRole(role);
    }


}
